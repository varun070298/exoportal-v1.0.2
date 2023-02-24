/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.util.Map ;
import java.util.List ;
import java.util.ResourceBundle;
import java.io.IOException  ;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.commons.utils.ExpressionUtil;
import org.exoplatform.faces.core.Util;
import org.exoplatform.faces.core.component.model.*;

/**

 * Wed, Dec 22, 2003 @ 23:14 

 * @author: Tuan Nguyen

 * @email:   tuan08@users.sourceforge.net

 * @version: $Id: UISelectBox.java,v 1.6 2004/06/26 03:47:31 tuan08 Exp $

 */
public class UISelectBox extends UIInput {
  private String updateOnChangeAction_ ;
  protected String value_ ;
  private List options_ ;

  public UISelectBox(String name, String value, List options) {
    name_ = name ;
    value_ = value;
    options_ = options ;
  }

  final public String getValue() { return value_ ; }
  final public UISelectBox setValue(String s) { 
    value_ = s ; 
    return this ;
  }

  final public String getUpdateOnChangeAction() { return updateOnChangeAction_ ; }
  final public UISelectBox setUpdateOnChangeAction(String s) { 
    updateOnChangeAction_ = s ; 
    return this ;
  }

  final public List getOptions() { return options_ ; }

  final public UISelectBox setOptions(List options) { 
    options_ = options ; 
    return this ;
  }

  public void decode(FacesContext context) {
    if (!editable_ || readonly_) return ;
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String value = (String) paramMap.get(name_) ;
    if (value != null) {
      value_ = value ;
    }
  }

  public void encodeBegin(FacesContext context) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    ResourceBundle res = Util.getApplicationResourceBundle()  ;
    w.write("<select "); w.write("name='"); w.write(name_); w.write("'") ;
    if (!editable_ || readonly_) {
      w.write(" disabled='true'");
    }
    if(getClazz() != null) {
      w.write(" class='"); w.write(getClazz());  w.write("'") ;
    }
    if(updateOnChangeAction_ != null) {
      UISimpleForm uiForm = (UISimpleForm) getParent();
      String formName = uiForm.getFormName() ;
      w.write(" onchange=\"javascript:submit_"); w.write(formName); w.write("('") ;
      w.write(updateOnChangeAction_);  w.write("')\"") ;
    }
    w.write(">\n") ;
    for(int i=0; i < options_.size(); i++) {
      SelectItem si = (SelectItem) options_.get(i) ;
      if (si.value_.equals(value_)) {
        w.write("<option selected=\"selected\" value=\""); w.write(si.value_); w.write("\">"); 
        w.write(ExpressionUtil.getExpressionValue(res,si.display_)); w.write("</option>\n");
      }  else {
        w.write("<option value=\""); w.write(si.value_); w.write("\">"); 
        w.write(ExpressionUtil.getExpressionValue(res,si.display_)); w.write("</option>\n");
      }
    }
    w.write("</select>\n") ;
  }
}
