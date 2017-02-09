package com.fblog.core.lucene;

import com.fblog.core.WebConstants;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.core.utils.LogUtils;
import com.fblog.core.utils.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 搜索引擎服务提供类
 */
public final class SearchEngine {
    private static final String TAG = "SearchEngine";

    //用于近空间里挨过
    private final TrackingIndexWriter nrtWriter;
    private final IndexWriter indexWriter;
    private final ReferenceManager<IndexSearcher> manager;
    private final Analyzer analyzer;
    //用于负责周期性打开ReferenceManager该线程类控制打开间隔比较灵活，
    // 当有外部用户在等待重新打开Searcher时就按最小时间间隔等待，如果没有用户着急获取新Searcher，则等待最大时间间隔后再打开。
    private final ControlledRealTimeReopenThread<IndexSearcher> nmrt;
    /* 每次IndexReader的reopen都会导致generation增1 */
    private volatile long reopenToken;
    private static SearchEngine instance;

    //使用懒汉式单例模式
    private static SearchEngine getInstance() {
        if (instance == null) {
            synchronized (SearchEngine.class) {
                if (instance == null) {
                    instance = new SearchEngine("post/index");
                }
            }
        }
        return instance;
    }

    public static SearchEngine postEnginer() {
        return getInstance();
    }

    private SearchEngine(String indexDir) {
        try {
            Directory directory = FSDirectory.open(new File(WebConstants.APPLICATION_PATH, indexDir));
            //使用中文的最细粒度分词
            analyzer = new IKAnalyzer(false);
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            //对给定的段列表顺序归并
            LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
            //达到5个文件时就合并，默认为10个
            mergePolicy.setMergeFactor(5);
            iwc.setMergePolicy(mergePolicy);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter = new IndexWriter(directory, iwc);
            nrtWriter = new TrackingIndexWriter(indexWriter);
            manager = new SearcherManager(indexWriter, true, null);
            //设定最大和最小时间间隔
            nmrt = new ControlledRealTimeReopenThread<>(nrtWriter, manager, 5.0, 0.05);
            nmrt.setName("reopen thread");
            nmrt.start();
        } catch (IOException e) {
            LogUtils.e(TAG, "error when Lucene index create:" + e);
            throw new IllegalStateException("Lucene index could not be create:" + e.getMessage());
        }
    }

    /**
     * 此方法返回切换Directory<br>
     * 要关闭复合文件格式(除段信息文件，锁文件，以及删除的文件外，其他的一系列索引文件压缩一个后缀名为cfs的文件,默认为关闭)
     * 生成复合文件将消耗更多的时间,但它有更好的查询效率,适合查询多更新少的场合<br>
     * IndexWriterConfig. setUseCompoundFile(false);
     *
     * @param path
     * @return
     * @throws IOException
     */
    Directory initDirectory(File path) throws IOException {
        // 添加放置在nio文件里的索引文件,由主索引负责打开的文件
        // .fdt文件用于存储具有Store.YES属性的Field的数据；.fdx是一个索引，用于存储Document在.fdt中的位置
        Set<String> files = new HashSet<String>();
        files.add("fdt");
        files.add("fdx");

        Directory dir = FSDirectory.open(path);// 装载磁盘索引
    /* RAMDirectory来访问索引其速度和效率都是非常优异的 */
        RAMDirectory map = new RAMDirectory(dir, IOContext.READ);
        NIOFSDirectory nio = new NIOFSDirectory(path);// 基于并发大文件的NIO索引
        // 组合不同Directory的优点
        FileSwitchDirectory fsd = new FileSwitchDirectory(files, nio, map, true);

        return fsd;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * 添加文档，创建索引
     *
     * @param doc 单个文档
     * @throws LuceneException
     */
    public void insert(Document doc) throws LuceneException {
        try {
            reopenToken = nrtWriter.addDocument(doc);
        } catch (IOException e) {
            LogUtils.e(TAG, "Error while in Lucene index operation" + e);
            throw new LuceneException("Error while Lucene insert");
        } finally {
            commit();
        }
    }

    /**
     * 批量添加文档
     *
     * @param docs 文档集
     * @throws LuceneException
     */
    public void insert(Collection<Document> docs) throws LuceneException {
        try {
            for (Document doc : docs) {
                nrtWriter.addDocument(doc);
            }
            reopenToken = nrtWriter.getGeneration();
        } catch (IOException e) {
            LogUtils.e("Error while in Lucene index operation: {}", e);
            throw new LuceneException("Error while add index", e);
        } finally {
            commit();
        }
    }

    /**
     * 更新搜索域
     *
     * @param term     .
     * @param document .
     * @throws LuceneException
     */
    public void update(Term term, Document document) throws LuceneException {
        try {
            reopenToken = nrtWriter.updateDocument(term, document);
        } catch (IOException e) {
            LogUtils.e("Error in lucene re-indexing operation: {}", e);
            throw new LuceneException("Error while update index", e);
        } finally {
            commit();
        }
    }

    /**
     * 删除索引
     *
     * @param term 索引
     * @throws LuceneException
     */
    public void delete(Term term) throws LuceneException {
        try {
            reopenToken = nrtWriter.deleteDocuments(term);
        } catch (IOException e) {
            LogUtils.e("Error in lucene re-indexing operation: {}", e);
            throw new LuceneException("Error while remove index", e);
        } finally {
            commit();
        }
    }

    private void commit() {
        try {
            indexWriter.commit();
        } catch (IOException e) {
            LogUtils.e(TAG, "error when index writer commit " + e);
        }
    }

    private void release(IndexSearcher searcher) {
        if (searcher != null) {
            try {
                manager.release(searcher);
            } catch (IOException e) {
                LogUtils.e(TAG, "got IOException when manager release searcher:" + e);
            }
        }
    }

    /**
     * 获取所有文档数,不包含删除文档数
     *
     * @return
     */
    public int docCount() {
    /* 注:numDocs()返回为包含删除文档数 */
        return indexWriter.maxDoc();
    }

    /**
     * 清空索引
     *
     * @throws LuceneException
     */
    public void truncate() throws LuceneException {
        try {
            reopenToken = nrtWriter.deleteAll();
        } catch (IOException e) {
            LogUtils.e("Error truncating lucene index: {}", e);
            throw new LuceneException("Error while tuncate index", e);
        } finally {
            commit();
        }
    }

    /**
     * 与指定文档相似搜索
     *
     * @param docNum 指定文档的编号
     * @param fields 查找的域
     * @return
     * @throws IOException
     */
    public List<MapContainer> like(int docNum, String[] fields) throws IOException {
        MoreLikeThis mlt = new MoreLikeThis(DirectoryReader.open(indexWriter, false));
        mlt.setFieldNames(fields);//设定查找域
        mlt.setMinTermFreq(2);//一篇文档中一个词语至少出现次数，小于这个值的词将被忽略,默认值是2
        mlt.setMinDocFreq(3);//一个词语最少在多少篇文档中出现，小于这个值的词会将被忽略，默认值是5
        mlt.setAnalyzer(getAnalyzer());
        Query query = mlt.like(docNum);

        return search(query, 5, null);
    }

    /**
     * 搜索指定域的文档
     *
     * @param query
     * @param max
     * @param fields
     * @return
     */
    private List<MapContainer> search(Query query, int max, Set<String> fields) {
        List<MapContainer> result = new LinkedList<>();
        IndexSearcher searcher = null;
        try {
            nmrt.waitForGeneration(reopenToken);
            searcher = manager.acquire();
            TopDocs docs = searcher.search(query, max);
            ScoreDoc[] sd = docs.scoreDocs;

            for (ScoreDoc scoreDoc : sd) {
                Document doc = null;
                if (fields == null || fields.isEmpty()) {
                    doc = searcher.doc(scoreDoc.doc);
                } else {
                    doc = searcher.doc(scoreDoc.doc, fields);
                }
                MapContainer mc = DocConverter.convert(doc);
                result.add(mc);
            }
        } catch (InterruptedException e) {
            LogUtils.e(TAG, "got InterruptedException when  Lucene search" + e);
        } catch (IOException e) {
            LogUtils.e(TAG, "got IOException when  Lucene search" + e);
        } finally {
            release(searcher);
        }
        return result;
    }

    public void searchHighlight(QueryBuilder builder, PageModel<MapContainer> model) {
        searchHighlight(builder, model, null);
    }

    private ScoreDoc lastScoreDoc(IndexSearcher searcher, QueryBuilder builder, PageModel<MapContainer> model)
            throws IOException {
        if (model.getPageIndex() == 1)
            return null;

        int num = model.getPageSize() * (model.getPageIndex() - 1);
        TopDocs tds = searcher.search(builder.getQuery(), builder.getFilter(), num);
        return tds.scoreDocs[num - 1];
    }

    /**
     * 高亮查询(适合只做下一页搜索)
     *
     * @param builder
     * @param model
     * @param fields
     */
    public void searchAfterHighlight(QueryBuilder builder, PageModel<MapContainer> model, Set<String> fields) {
        IndexSearcher searcher = null;
        try {
            nmrt.waitForGeneration(reopenToken);
            searcher = manager.acquire();
            // 先获取上一页的最后一个元素
            ScoreDoc last = lastScoreDoc(searcher, builder, model);
            // 通过最后一个元素搜索下页的pageSize个元素
            TopDocs docs = searcher.searchAfter(last, builder.getQuery(), model.getPageSize());
            model.setTotalCount(docs.totalHits);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = null;
                if (fields == null || fields.isEmpty()) {
                    doc = searcher.doc(sd.doc);
                } else {
                    doc = searcher.doc(sd.doc, fields);
                }
                MapContainer mc = DocConverter.convert(doc, builder.getLighters());
                for (String filter : builder.getLighters()) {
                    String content = doc.get(filter);
                    if (content == null || content.trim().length() == 0) {
                        LogUtils.d("TAG", "field " + filter + " is null");
                        continue;
                    }
                    mc.put(filter, getBestFragment(builder.getQuery(), content, filter));
                }
                model.addContent(mc);
            }

        } catch (Exception e) {
            LogUtils.e(TAG, e);
        } finally {
            release(searcher);
        }
    }


    /**
     * 高亮再查询
     *
     * @param builder 查询构建器
     * @param model   分页数据模型
     * @param fields  需要从Document中获取的字段,为空时为全部获取
     */
    public void searchHighlight(QueryBuilder builder, PageModel<MapContainer> model, Set<String> fields) {
        IndexSearcher searcher = null;
        try {
            nmrt.waitForGeneration(reopenToken);
            searcher = manager.acquire();
            TopDocs docs = searcher.search(builder.getQuery(), builder.getFilter(), Integer.MAX_VALUE);
            model.setTotalCount(docs.totalHits);
            ScoreDoc[] sd = docs.scoreDocs;

            int start = (model.getPageIndex() - 1) * model.getPageSize();
            int end = model.getPageIndex() * model.getPageSize();
            for (int i = start; i < end && i < sd.length; i++) {
                Document doc = null;
                if (fields == null || fields.isEmpty())
                    doc = searcher.doc(sd[i].doc);
                else
                    doc = searcher.doc(sd[i].doc, fields);

                MapContainer mc = DocConverter.convert(doc, builder.getLighters());
                for (String filter : builder.getLighters()) {
                    String content = doc.get(filter);
                    if (content == null || content.trim().length() == 0) {
                        LogUtils.e(TAG, "field " + filter + " is null");
                        continue;
                    }
                    mc.put(filter, getBestFragment(builder.getQuery(), content, filter));
                }
                model.addContent(mc);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        } finally {
            release(searcher);
        }
    }

    /**
     * 获取高亮文本
     *
     * @param query
     * @param target
     * @param fieldName
     * @return
     * @throws Exception
     */
    private String getBestFragment(Query query, String target, String fieldName) throws Exception {
        org.apache.lucene.search.highlight.Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        org.apache.lucene.search.highlight.Scorer scorer = new QueryScorer(query);
        Highlighter hlighter = new Highlighter(formatter, scorer);

        // 设置文本摘要大小
        Fragmenter fg = new SimpleFragmenter(200);
        hlighter.setTextFragmenter(fg);

        String result = hlighter.getBestFragment(analyzer, fieldName, target);
        result = StringUtils.isEmpty(result) ? target.substring(0, Math.min(200, target.length())) : result;
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        shutdwon();
        super.finalize();
    }

    public void shutdwon() {
        try {
            nmrt.interrupt();
            nmrt.close();
            indexWriter.commit();
            indexWriter.close();
        } catch (Exception e) {
            LogUtils.e(TAG, "Error while closing lucene index: " + e);
        }
    }

}
