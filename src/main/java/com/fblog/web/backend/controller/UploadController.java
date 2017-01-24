package com.fblog.web.backend.controller;

import com.fblog.biz.UploadManager;
import com.fblog.biz.editor.Ueditor;
import com.fblog.core.dao.constants.PostConstants;
import com.fblog.core.dao.entity.Upload;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.web.support.ServletRequestReader;
import com.fblog.web.support.WebContextFactory;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

@Controller
@RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
@RequestMapping("/backend/uploads")
public class UploadController {
    @Autowired
    private Ueditor ueditor;
    @Autowired
    private UploadManager uploadManager;

    @ResponseBody
    @RequestMapping(value = "/ueditor")
    public Object udeditor(ServletRequestReader reader) {
        return ueditor.server(reader);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam(value = "page",defaultValue = "1") int page, Model model){
        model.addAttribute("page",uploadManager.list(page, PostConstants.MAX_POST_SHOW));
        return "backend/upload/list";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Object insert(MultipartFile file){
        Upload upload = null;
        try(InputStream in = file.getInputStream()){
            upload = uploadManager.insertUpload(new InputStreamResource(in), new Date(), file.getOriginalFilename(),
                    WebContextFactory.get().getUser().getId());
        }catch(Exception e){
            e.printStackTrace();
        }
        return new MapContainer("success", upload != null);
    }

}
