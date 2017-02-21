package com.fblog.core.dao.mapper;

import com.fblog.core.dao.entity.Post;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.service.vo.PageVO;
import com.fblog.service.vo.PostVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper extends BaseMapper {

    /**
     * 获取指定帖子的上一篇
     */
    Post getPrevPost(String postid);

    /**
     * 获取指定帖子的下一篇
     */
    Post getNextPost(String postid);

    @Override
    PostVO loadById(String id);

    /**
     * 获取页面（只包含id和title）
     */
    List<PageVO> listPage(boolean onlyParent);

    /**
     * 列出文章归档
     */
    List<MapContainer> listArchive();

    /**
     * 增加阅读数
     */
    int addRCount(@Param("postid") String postId, @Param("count") int count);

    /**
     * 增加评论数
     */
    int addCCount(@Param("commentid") String postId, @Param("count") int count);

    /**
     * 获取最近发表的文章
     */
    List<String> listRecent(@Param("nums") int nums, @Param("creator") String creator);

    /**
     * 最近一个月的文章
     */
    List<String> listByMouth(PageModel<String> model);

    /**
     * 取出同类别下的文章
     */
    List<String> listByCategory(PageModel<String> model);

    /**
     * 按标签查询文章
     */
    List<String> listByTag(PageModel<String> model);

    void updateCategory(@Param("oldCategoryIds")List<String> oldCategoryIds,@Param("newCategoryId")String newCategoryId);

    int getTotalRCount();
}
