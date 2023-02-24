/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

import org.exoplatform.commons.utils.ExoProperties;
/**
 * Jul 19, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PropertiesParam.java,v 1.1 2004/10/20 20:54:13 tuan08 Exp $
 */
public class PropertiesParam extends Parameter {
	private ExoProperties  properties  = new ExoProperties();
  
  public ExoProperties getProperties() { return properties ; }
  
  public String getProperty(String name) { return properties.getProperty(name) ; }
  public void  setProperty(String name, String value) { properties.setProperty(name, value) ;}
}