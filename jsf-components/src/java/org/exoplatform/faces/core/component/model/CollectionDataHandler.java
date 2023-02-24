/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.util.Collection ;
import java.util.Iterator ;
/**
 * Jun 30, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: CollectionDataHandler.java,v 1.1 2004/07/31 14:46:58 tuan08 Exp $
 */
abstract public class CollectionDataHandler implements DataHandler {
  protected Collection datas_ ;
  protected Iterator iterator_ ;
  
  public CollectionDataHandler() {
  }
  
  public CollectionDataHandler setDatas(Collection datas) {
    datas_ = datas ;
    return this ;
  }
  
  public void  begin() { iterator_ = datas_.iterator(); }
  public void end()  { iterator_ = null ; }
  
  public boolean nextRow() {
    boolean test = iterator_.hasNext();
    if(test) {
    	setCurrentObject(iterator_.next()) ;
    }
    return test ;
  }
  
  public Object getCurrentObject() { return iterator_.next()  ; }
  public String    getCurrentObjectId() { return null ; }
}