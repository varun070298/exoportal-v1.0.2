/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 29, 2004
 * @version $Id: HibernateServiceContainer.java,v 1.1 2004/10/30 02:27:51 tuan08 Exp $
 */
public interface HibernateServiceContainer {
  public HibernateService getHibernateService() ;
  public HibernateService getHibernateService(String name) ;
  public void closeAllSessions();
}
