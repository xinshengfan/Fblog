package com.fblog.core.dao.mapper;

import com.fblog.core.dao.entity.Upload;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UploadMapper extends BaseMapper {
    List<Upload> listByPostid(String postid);

    /**
     * 更新上传文件记录对应的文章ID
     */
    int updatePostid(@Param("postid") String postid, @Param("imgpaths") List<String> imgPaths);

    /**
     * 将所有postid的记录置null，非删除
     */
    void setNullPostid(String postid);

    int deleteByPostid(String postid);

}
