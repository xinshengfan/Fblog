package com.fblog.biz;

import com.fblog.core.WebConstants;
import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.dao.entity.*;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.core.utils.CollectionUtils;
import com.fblog.core.utils.JsoupUtils;
import com.fblog.core.utils.Utility;
import com.fblog.service.*;
import com.fblog.service.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用于文章的业务功能
 */
@Component
public class PostManager {
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private UploadService uploadService;

    public PostVO loadReadById(String postId) {
        PostVO postVo = postService.loadById(postId);
        if (postVo == null) {
            return null;
        }
        if (PostConstants.TYPE_POST.equals(postVo.getType())) {
            //是贴子
            Category category = categoryService.loadById(postVo.getCategoryid());
            postVo.setCategory(category);
            postVo.setTags(tagService.listTagsByPost(postId));
        }
        User user = userService.loadById(postVo.getCreator());
        postVo.setUser(user);
        return postVo;
    }

    /**
     * 插入文章，同时更新上传文件（图片）的postid
     */
    @Transactional
    public void insertPost(Post post, List<Tag> tags) {
        postService.insert(post);
        //查找当前html中所有图片的链接
        List<String> imgs = JsoupUtils.getImgesOrLinks(post.getContent());
        if (!CollectionUtils.isEmpty(imgs)) {
            uploadService.uploadPostId(post.getId(), imgs);
        }
        if (PostConstants.TYPE_POST.equals(post.getType()) && !CollectionUtils.isEmpty(tags)) {
            tagService.insertBatch(tags);
        }
    }

    /**
     * 更新文章,先重置以前文件对应的附件的postid,再更新文章对应的postid
     */
    @Transactional
    public boolean updatePost(Post post, List<Tag> tags) {
        return updatePost(post, tags, false);
    }

    @Transactional
    public boolean updatePost(Post post, List<Tag> tags, boolean fast) {
        if (!fast) {
            //先重置以前文件对应的附件的postid,再更新文章对应的postid
            uploadService.setNullPostId(post.getId());
            List<String> imgs = Utility.extractImagePath(JsoupUtils.getImgesOrLinks(post.getContent()));
            if (!CollectionUtils.isEmpty(imgs)) {
                uploadService.uploadPostId(post.getId(), imgs);
            }
        }
        int affect = postService.update(post);
        if (PostConstants.TYPE_POST.equals(post.getType()) && !CollectionUtils.isEmpty(tags)) {
            tagService.deleteByPostId(post.getId());
            tagService.insertBatch(tags);
        }
        return affect != 0;
    }

    /**
     * 删除文章，同时删除文章对应的上传记录及文件
     */
    @Transactional
    public void deletePost(String postId, String postTyle) {
        List<Upload> list = uploadService.listByPostId(postId);
        uploadService.deleteByPostId(postId);
        postService.deleteById(postId);

        for (Upload upload : list) {
            File file = new File(WebConstants.APPLICATION_PATH, upload.getPath());
            file.deleteOnExit();
        }
    }

    /***************  分页查询业务 *********************/

    public PageModel<PostVO> listPost(int pageIndex, int pageSize) {
        return page2PageVO(postService.listPost(pageIndex, pageSize));
    }

    public PageModel<PostVO> listPage(int pageIndex, int pageSize) {
        return page2PageVO(postService.listPage(pageIndex, pageSize));
    }

    public PageModel<PostVO> listByTag(String tagName, int pageIndex, int pageSize) {
        return page2PageVO(postService.listByTag(tagName, pageIndex, pageSize));
    }

    public PageModel<PostVO> listByCategory(Category category, int pageIndex, int pageSize) {
        return page2PageVO(postService.listByCategory(category, pageIndex, pageSize));
    }

    public PageModel<PostVO> listByMonth(Date yearMonth, int pageIndex, int pageSize) {
        return page2PageVO(postService.listByMonth(yearMonth, pageIndex, pageSize));
    }

    public List<PostVO> listBySiteMap() {
        List<String> postIds = postService.listBySiteMap();
        List<PostVO> contents = new ArrayList<>(postIds.size());
        for (String id : postIds) {
            PostVO postVo = postService.loadById(id);
            contents.add(postVo);
        }
        return contents;
    }

    public List<PostVO> listRecent(int nums, String creator) {
        List<String> ids = postService.listRecent(nums, creator);
        List<PostVO> contents = new ArrayList<>(ids.size());
        for (String id : ids) {
            contents.add(loadReadById(id));
        }
        return contents;
    }

    /**
     * 由于分而查询的PageMode中的Content是查询中的所有文章的id,
     * 需要使用loadById将文章加载出来
     */
    private PageModel<PostVO> page2PageVO(PageModel<String> postPage) {
        //构建一个新的用于保存文章内容的PageModel
        PageModel<PostVO> model = new PageModel<PostVO>(postPage.getPageIndex(), postPage.getPageSize());
        model.setTotalCount(postPage.getTotalCount());
        List<PostVO> contents = new ArrayList<>(postPage.getContent().size());
        for (String id : postPage.getContent()) {
            contents.add(loadReadById(id));
        }
        model.setContent(contents);
        return model;
    }

}
