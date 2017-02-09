package com.fblog.biz.aop;

import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.core.dao.entity.Post;
import com.fblog.core.lucene.LuceneUtils;
import com.fblog.core.lucene.QueryBuilder;
import com.fblog.core.lucene.SearchEngine;
import com.fblog.core.utils.JsoupUtils;
import com.fblog.core.utils.LogUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.springframework.stereotype.Component;

/**
 * 文件Lucene索引管理器
 * 使用Aop在文章的增删查改后切入相应的方式，执行对应的操作到lucene的索引表中
 */
@Component
public class IndexManager {
    private static final String TAG = "IndexManager";

    /**
     * 在文章插入的时候插入索引
     *
     * @param post 文章
     */
    public void insert(Post post) {
        if (PostConstants.TYPE_POST.equals(post.getType())) {
            LogUtils.d(TAG, "add post index：" + post.getTitle());
            SearchEngine.postEnginer().insert(convert(post));
        }
    }

    /**
     * 在文章更新时更新Lucene索引
     *
     * @param post
     * @param affect
     */
    public void update(Post post, boolean affect) {
        if (PostConstants.TYPE_POST.equals(post.getType()) && affect) {
            SearchEngine.postEnginer().update(new Term("id", post.getId()), convert(post));
        }
    }

    public void remove(String postId, String postType) {
        if (PostConstants.TYPE_POST.equals(postType)) {
            SearchEngine.postEnginer().delete(new Term("id", postId));
        }
    }

    /**
     * 文章对应转换为Lucene下的Doc
     *
     * @param post 文章
     * @return
     */
    private Document convert(Post post) {
        Document document = new Document();
        document.add(new Field("id", post.getId() + "", LuceneUtils.directType()));
        document.add(new Field("title", post.getTitle(), LuceneUtils.searchType()));
        //需要剔除html标签
        document.add(new Field("excerpt", JsoupUtils.plainText(post.getContent()), LuceneUtils.storeType()));
        return document;
    }

    /**
     * 搜索并高度字段
     *
     * @param word      关键字
     * @param pageIndex 页数
     * @return
     */
    public PageModel<MapContainer> search(String word, int pageIndex) {
        PageModel<MapContainer> result = new PageModel<MapContainer>(pageIndex, PostConstants.MAX_POST_SHOW);
        QueryBuilder builder = new QueryBuilder(SearchEngine.postEnginer().getAnalyzer());
        builder.addShould("title", word).addShould("excerpt", word);
        builder.addLighters("title", "excerpt");
        SearchEngine.postEnginer().searchHighlight(builder, result);
        return result;
    }
}
