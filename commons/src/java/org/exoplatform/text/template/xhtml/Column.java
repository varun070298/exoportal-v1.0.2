/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;
import org.exoplatform.commons.utils.ExpressionUtil;
import org.exoplatform.text.template.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public class Column extends Element {
  protected Value header_ ;
  protected Value data_ ;
  
  public Column(String value)  { 
    if(ExpressionUtil.isResourceBindingExpression(value)) {
      data_ = new ResourceBindingValue(value) ;
    } else if(ExpressionUtil.isDataBindingExpression(value)) {
      data_ = new DataBindingValue(value) ;
    } else {
      data_ = new StringValue(value) ;
    }
  }
  
  public Column(String header, String value)  {
    this(value) ;
    if(ExpressionUtil.isResourceBindingExpression(header)) {
      header_ = new ResourceBindingValue(header) ;
    } else {
      header_ = new StringValue(header) ;
    }
  }
  
  public Value  getHeader() { return header_ ; }
  
  public Value  getData()  { return data_ ; }
  
  public void renderHeader(ResourceBundle res, Writer w) throws IOException {
    w.write("<th>") ;
    w.write(resolveValueAsString(header_, null, res)) ;
    w.write("</th>") ;
  }
  
  public void renderCell(XhtmlDataHandlerManager manager,
                         ResourceBundle res, Writer w) throws IOException {
    if(cssClass_ == null) {
      w.write("<td>") ;
    } else {
      w.write("<td class='") ; w.write(cssClass_); w.write("'>") ;
    }
    DataHandler dhandler = manager.getDataHandler(dataHandlerType_);
    if(formater_ != null) { 
      formater_.format(w, dhandler.getValue((DataBindingValue)data_)) ;
    } else {
      w.write(resolveValueAsString(data_ ,dhandler, res)) ;
    }
    w.write("</td>") ;
  }
  
  public void render(XhtmlDataHandlerManager manager, 
                     ResourceBundle res, Writer w) throws IOException {
    throw new RuntimeException("This method should not be called");
  }
}