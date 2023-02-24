/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 2, 2005
 * @version $Id$
 */
public class Value  {
  protected String expression_ ;
  
  public Value(String expression) {
    expression_ = expression ;
  }
  
  public String getExpression() { return expression_ ; } 
}