/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.util.ArrayList ;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 29, 2004
 * @version $Id: DatabaseServiceConfig.java,v 1.1 2004/10/30 02:27:52 tuan08 Exp $
 */
public class DatabaseServiceConfig {
  private ArrayList datasource = new ArrayList() ;
  
  public ArrayList getDatasource() { return datasource; }
  public void setDatasource(ArrayList list) {  this.datasource = list; }
  
  static public class DataSourceConfig {
    private String name ;
    private String jndi ;
    
    public String getJndi() {    return jndi;   }
    public void setJndi(String jndi) {    this.jndi = jndi;   }
    
    public String getName() {   return name;   }
    public void setName(String name) {    this.name = name;   }
  }
}
