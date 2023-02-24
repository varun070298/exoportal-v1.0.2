/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.rdb.container.data;

/**
 * Created by The eXo Platform SARL        .
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: ValueRecord.java,v 1.1 2004/11/02 18:34:38 geaz Exp $
 * 
 * @hibernate.class  table="JCR_CONTAINER_VALUE"
 */
public abstract class ValueRecord {
  
  private Long id;
  
  public ValueRecord() {
  }
  
  /** @hibernate.id  generator-class="increment" */
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
}
