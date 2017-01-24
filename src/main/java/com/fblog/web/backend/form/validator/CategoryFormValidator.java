package com.fblog.web.backend.form.validator;


import com.fblog.core.dao.entity.Category;
import com.fblog.core.dao.entity.MapContainer;
import com.fblog.core.utils.StringUtils;

public class CategoryFormValidator {

  public static MapContainer validateInsert(Category category){
    MapContainer form = new MapContainer();
    if(StringUtils.isEmpty(category.getName())){
      form.put("msg", "分类名称不能为空");
    }

    return form;
  }

}
