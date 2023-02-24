/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

/**
 * Jul 19, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ValueParam.java,v 1.1 2004/10/20 20:54:13 tuan08 Exp $
 */
public class ValueParam extends Parameter {
	private String  value  ;
  
  public String getValue() { return value ; }
  public void   setValue(String s) { value = s ; }
}