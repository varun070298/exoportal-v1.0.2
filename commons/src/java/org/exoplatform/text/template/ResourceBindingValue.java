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
public class ResourceBindingValue  extends Value {
  public String key_ ;
  
  public ResourceBindingValue(String expression) {
    super(expression) ;
    key_ = ExpressionUtil.removeBindingExpression(expression) ;
  }
  
  public String getKey() { return key_ ; }
}