/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.HashMap; 
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 22, 2004
 * @version $Id: XResources.java,v 1.1 2004/10/22 23:49:17 tuan08 Exp $
 */
public class XResources extends HashMap {

  public Object getResource(Class cl)  { return get(cl) ; }
  public void   addResource(Class cl , Object resource) {
    put(cl, resource) ;
  }
  
  public Object removeResource(Class cl) {  return remove(cl) ; }
}  