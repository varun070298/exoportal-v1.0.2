/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;
import org.exoplatform.Constants;
import org.exoplatform.text.template.DataBindingValue;
import org.exoplatform.text.template.DataHandler;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 3, 2005
 * @version $Id$
 */
public class LinkColumn extends  Column {
  private DataBindingValue beanId_ ;
  private String parameters_ = "";
  
  public LinkColumn(String header, String value, String beanid)  {
    super(header, value) ;
    beanId_ = new DataBindingValue(beanid) ;
  }
  
  public LinkColumn addParameter(String name, String value) {
    parameters_ += Constants.AMPERSAND + name + "=" + value ;
    return this ;
  }
  
  public void renderCell(XhtmlDataHandlerManager manager,
                         ResourceBundle res, Writer w) throws IOException {
    if(cssClass_ == null) {
      w.write("<td>") ;
    } else {
      w.write("<td class='") ; w.write(cssClass_); w.write("'>") ;
    }
    DataHandler dhandler = manager.getDataHandler(dataHandlerType_);
    String linkLabel = resolveValueAsString(data_, dhandler, res) ;
    String id = dhandler.getValueAsString(beanId_) ;
    String baseURL =  manager.getBaseURL() ; 
    w.write("<a href='"); 
    w.write(baseURL); w.write(parameters_) ;
    w.write(Constants.AMPERSAND + "objectId="); w.write(id) ;
    w.write("'>");
    w.write(linkLabel);
    w.write("</a>");
    w.write("</td>") ;
  }
}
