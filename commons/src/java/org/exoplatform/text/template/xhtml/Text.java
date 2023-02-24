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
public class Text extends Element {
  protected Value data_ ;
  
  public Text(String value)  { 
    if(value == null ) return ;
    if(ExpressionUtil.isResourceBindingExpression(value)) {
      data_ = new ResourceBindingValue(value) ;
    } else if(ExpressionUtil.isDataBindingExpression(value)) {
      data_ = new DataBindingValue(value) ;
    } else {
      data_ = new StringValue(value) ;
    }
  }
  
  public Value getData()  { return data_ ; }
  
  
  public void render(XhtmlDataHandlerManager manager, 
                     ResourceBundle res, Writer w) throws IOException {
    DataHandler dh = manager.getDataHandler(dataHandlerType_) ;
    w.write(resolveValueAsString(data_ ,dh, res)) ;
  }
}