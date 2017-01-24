package com.fblog.core.tag;

import com.fblog.core.dao.entity.PageModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class PrevTag extends AbstartTagSupport{
  private static final long serialVersionUID = 1L;

  @Override
  public int doStartTag() throws JspException{
    PageModel<?> model = getPagination().getModel();

    if(model.getPageIndex() > 1){
      setPageAttribute(model.getPageIndex() - 1);
      return TagSupport.EVAL_BODY_INCLUDE;
    }else{
      return TagSupport.SKIP_BODY;
    }
  }

}
