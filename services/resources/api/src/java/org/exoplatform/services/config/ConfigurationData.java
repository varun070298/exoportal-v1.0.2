/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.config;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 5, 2004
 * @version $Id$
 */
public interface ConfigurationData {
  public String getServiceType() ;
  public void   setServiceType(String s) ;
  
  public String getData() ;
  public String setData(String s) ;
}
