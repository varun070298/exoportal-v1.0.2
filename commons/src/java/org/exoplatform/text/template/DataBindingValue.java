/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template;

import org.exoplatform.commons.utils.ExpressionUtil;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 2, 2005
 * @version $Id$
 */
public class DataBindingValue  extends Value {
  public String method_ ;
  
  public DataBindingValue(String expression) {
    super(expression) ;
    method_ = ExpressionUtil.removeBindingExpression(expression) ;
    char[] buf = method_.toCharArray() ;
    if(method_.endsWith("()")) { 
      method_ = method_.substring(0, method_.length() - 2) ;
    } else {
      if(Character.isLowerCase(buf[0])) {
        buf[0] = Character.toUpperCase(buf[0]) ;
      }
      method_ = "get" + new String(buf) ;
    }
  }
  
  public String getMethodName() { return method_ ; }
}