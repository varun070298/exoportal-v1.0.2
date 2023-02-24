/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.ExpressionUtil;
import org.exoplatform.text.template.DataHandler;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 3, 2005
 * @version $Id$
 */
public class Button extends  Text {
  private String parameters_ = "";
  
  public Button(String value)  {
    super(value) ;
  }
  
  public Button addParameter(String name, String value) {
    parameters_ += Constants.AMPERSAND + name + "=" + value ;
    return this ;
  }
  
  public void render(XhtmlDataHandlerManager manager, 
                     ResourceBundle res, Writer w) throws IOException {
    DataHandler dh = manager.getDataHandler(dataHandlerType_) ;
    String baseURL =  manager.getBaseURL() ; 
    w.write("<a");
    if (cssClass_ != null) {
      w.write(" class='"); w.write(cssClass_); w.write("'");
    }
    w.write(" href='"); w.write(baseURL); w.write(parameters_); w.write("'>");
    w.write(ExpressionUtil.getExpressionValue(res,resolveValueAsString(data_ ,dh, res)));
    w.write("</a>");
  }
}
