package com.fblog.core.lucene;

import com.fblog.core.utils.LogUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * 查询构建器
 */
public class QueryBuilder {
    private static final String TAG = "QueryBuilder";
    private static int MAX_QUERY_TOKENS = 5;
    //最大查询字符串长度
    private static int MAX_QUERY_LENGTH = 20;
    //分析器
    private Analyzer analyzer;
    //搜索域
    private List<Term> should = new LinkedList<>();
    //高亮字段
    private List<String> lighters = new LinkedList<>();

    private Filter filter;

    public QueryBuilder(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public QueryBuilder(Filter filter) {
        this.filter = filter;
    }

    public QueryBuilder addShould(String field, String value) {
        String[] tokens = token(value);
        if (tokens != null && tokens.length > 0) {
            for (String token : tokens) {
                should.add(new Term(field, token));
            }
        } else {
            LogUtils.e(TAG, "查询域的单词没有被拆分成分词:" + value);
        }
        return this;
    }

    /**
     * 添加高亮字段
     *
     * @param fields 需要高亮的字段
     * @return
     */
    public QueryBuilder addLighters(String... fields) {
        lighters.addAll(Arrays.asList(fields));
        return this;
    }

    public List<String> getLighters() {
        return lighters;
    }

    public Filter getFilter() {
        return filter;
    }

    /**
     * 获取查询语句
     *
     * @return query
     */
    public Query getQuery() {
        BooleanQuery query = new BooleanQuery();
        for (Term term : should) {
            query.add(new TermQuery(term), BooleanClause.Occur.SHOULD);
        }
        return query;
    }

    /**
     * 使用分词器获取分词数组
     *
     * @param str 原数据
     * @return 拆分后的分词数组
     */
    private String[] token(String str) {
        str = str.length() > MAX_QUERY_LENGTH ? str.substring(0, MAX_QUERY_LENGTH) : str;
        TokenStream stream = null;
        try {
            List<String> list = new ArrayList<>();
            stream = analyzer.tokenStream("any", new StringReader(str));
            CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while (stream.incrementToken() && list.size() < MAX_QUERY_TOKENS) {
                list.add(cta.toString());
            }
            stream.end();
            //若没有分词结果时，直接返回null
            if (list.isEmpty()) {
                return null;
            }
            list = reSubset(list);
            String[] result = new String[list.size()];
            return list.toArray(result);
        } catch (IOException e) {
            LogUtils.e(TAG, "Analyzer " + str + " error:" + e);
        } finally {
            LuceneUtils.closeQuietly(stream);
        }
        return null;
    }

    /**
     * 剔除重复子集，(例:[视频网站、视频、网站、购物]剔除后变为[视频网站、购物])
     * 其实和最大匹配分词效果一样(IKAnalyzer.setUseSmart(true)),但在添加文档必须使用最细粒度分词
     *
     * @param list 原集
     * @return 子集
     */
    private List<String> reSubset(List<String> list) {
        List<String> result = new ArrayList<>();
        //生成一个堆
        for (int i = 0; i < list.size(); i++) {
            buildMinHeap(list, list.size() - i - 1);
            Collections.swap(list, 0, list.size() - i - 1);
        }
        //只在该堆中处理
        result.add(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            String item = list.get(i);
            boolean hasSub = false;
            //只保存结果集里没有的分词
            for (String temp : result) {
                if (temp.contains(item)) {
                    hasSub = true;
                    break;
                }
            }
            if (!hasSub) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 建立最小顶堆
     *
     * @param list .
     * @param last .
     */
    private void buildMinHeap(List<String> list, int last) {
        // 从最后一个非叶子节点起
        for (int i = (last - 1) / 2; i >= 0; i--) {
            int current = i; // 当前非叶节点
            while ((2 * current + 1) <= last) { // 如果current存在子节点
                int big = 2 * current + 1;
                if (big < last) { // 如果current节点右子节点存在
                    if (list.get(big).length() > list.get(big + 1).length())
                        big++;
                }

                if (list.get(current).length() > list.get(big).length()) { // 如果current节点小于它的最大子节点
                    Collections.swap(list, current, big);
                    // 将big赋值给current，因为修改了big节点值，开始下一层循环
                    current = big;
                } else
                    break;
            }
        }
    }

}
