/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

/**
 * Jun 30, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: DataHandler.java,v 1.2 2004/07/01 14:20:50 tuan08 Exp $
 */
public interface DataHandler {
  public void  begin() ;
  public void end() ;
  public boolean nextRow() ;
  
  public void setCurrentObject(Object o) ;
  public String  getData(String fieldName)  ; 
}