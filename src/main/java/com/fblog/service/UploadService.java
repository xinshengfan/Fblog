package com.fblog.service;

import com.fblog.core.dao.entity.Upload;
import com.fblog.core.dao.mapper.BaseMapper;
import com.fblog.core.dao.mapper.UploadMapper;
import com.fblog.core.dao.entity.PageModel;
import com.fblog.service.vo.UploadVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fansion on 19/01/2017.
 * 图片及文件上传业务
 */
@Service
public class UploadService extends BaseService {
    @Autowired
    private UploadMapper uploadMapper;

    @Override
    protected BaseMapper getMapper() {
        return uploadMapper;
    }

    public void uploadPostId(String postId, List<String> imgPaths) {
        uploadMapper.updatePostid(postId, imgPaths);
    }

    public PageModel<UploadVO> list(int pageIndex, int pageSize) {
        PageModel<UploadVO> model = new PageModel<UploadVO>(pageIndex, pageSize);
        list(model);
        return model;
    }

    public List<Upload> listByPostId(String postId) {
        return uploadMapper.listByPostid(postId);
    }

    /**
     * 将所有postid的记录置空,非删除记录
     */
    public void setNullPostId(String postId) {
        uploadMapper.setNullPostid(postId);
    }

    public void deleteByPostId(String postId) {
        uploadMapper.deleteByPostid(postId);
    }

}
