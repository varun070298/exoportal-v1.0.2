/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.backup;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Apr 12, 2005
 * @version $Id$
 */
public class Metadata {
  private String version ;
  private String description ;
  private String type;
   
  public Metadata() {
  }
  
  public Metadata(String v, String desc, String t) {
    version = v ;
    description = desc ;
    type = t;
  }
  
  public String getDataVersion() { return version ; }
  public void   setDataVersion(String v) { version = v ; }
  
  public String getDescription() { return description ; }
  public void   setDescription(String desc) { description = desc ; }
  
  public String getType() { return type; }
  public void   setType(String t) { type = t ; }
}
