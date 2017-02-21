package com.fblog.service;

import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.dao.entity.Category;
import com.fblog.core.dao.entity.Post;
import com.fblog.core.dao.mapper.BaseMapper;
import com.fblog.core.dao.mapper.PostMapper;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.service.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostService extends BaseService {
    @Autowired
    private PostMapper postMapper;

    @Override
    protected BaseMapper getMapper() {
        return postMapper;
    }

    public int addRCount(String postId, int count) {
        return postMapper.addRCount(postId, count);
    }

    public int addCCount(String postId, int count) {
        return postMapper.addCCount(postId, count);
    }

    public Post getPrevPost(String postid){
        return postMapper.getPrevPost(postid);
    }

    public Post getNextPost(String postid){
        return postMapper.getNextPost(postid);
    }

    /**
     * 获取当前作者最近的文章
     */
    public List<String> listRecent(int nums, String creator) {
        return postMapper.listRecent(nums, creator);
    }

    /**
     * 根据当前页数加载指定页数的文章，
     *
     * @return PageModel<String> 已经完成分页查询，
     * 包含文章总数totalCount和从pageIndex到pageIndex+pageSize的所有文章的id list
     */
    public PageModel<String> listPost(int pageIndex, int pageSize) {
        PageModel<String> page = new PageModel<String>(pageIndex, pageSize);
        page.insertQuery("type", PostConstants.TYPE_POST);
        super.list(page);
        //page是已经在super中完成查询，包含所有文章id的一个id list
            /* 由于分页标签会根据query产生,这里删除掉无用query,下同 */
        page.removeQuery("type");
        return page;
    }

    public PageModel<String> listByCategory(Category category, int pageIndex, int pageSize) {
        PageModel<String> model = new PageModel<String>(pageIndex, pageSize);
        model.insertQuery("category", category);
        List<String> content = postMapper.listByCategory(model);
        //未使用super，需手动处理
        model.setContent(content);
        model.removeQuery("category");
        return model;
    }

    public PageModel<String> listByTag(String tagName, int pageIndex, int pageSize) {
        PageModel<String> model = new PageModel<String>(pageIndex, pageSize);
        model.insertQuery("tagName", tagName);
        List<String> content = postMapper.listByTag(model);
        model.setContent(content);
        model.removeQuery("tagName");
        return model;
    }

    public PageModel<String> listByMonth(Date yearMonth, int pageIndex, int pageSize) {
        PageModel<String> model = new PageModel<String>(pageIndex, pageSize);
        model.insertQuery("yearMonth", yearMonth);
        List<String> content = postMapper.listByMouth(model);
        model.setContent(content);
        model.removeQuery("yearMonth");
        return model;
    }

    public List<String> listBySiteMap() {
        //查询所有
        PageModel<String> model = new PageModel<String>(1, -1);
        model.insertQuery("type", PostConstants.TYPE_POST);
        super.list(model);
        model.removeQuery("type");
        return model.getContent();
    }

    public PageModel<String> listPage(int pageIndex, int pageSize) {
        PageModel<String> model = new PageModel<String>(pageIndex, pageSize);
        model.insertQuery("type", PostConstants.TYPE_PAGE);
        super.list(model);
        model.removeQuery("type");
        return model;
    }

    /**
     * 获取所有页面（只包含id和title）
     */
    public List<PageVO> listPage(boolean onlyParent) {
        return postMapper.listPage(onlyParent);
    }

    /**
     * 列出所有文章归档
     */
    public List<MapContainer> listArchive() {
        return postMapper.listArchive();
    }

    public void updateCategory(List<String> oldCategoryIds, String newCategoryId) {
        postMapper.updateCategory(oldCategoryIds, newCategoryId);
    }

    public int getTotalRCount(){
        return postMapper.getTotalRCount();
    }


}
