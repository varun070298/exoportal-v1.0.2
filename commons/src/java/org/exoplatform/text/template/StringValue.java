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
public class StringValue  extends Value {
  
  public StringValue(String expression) {
    super(expression) ;
  }
  
  public String getValue() { return expression_ ; }
}