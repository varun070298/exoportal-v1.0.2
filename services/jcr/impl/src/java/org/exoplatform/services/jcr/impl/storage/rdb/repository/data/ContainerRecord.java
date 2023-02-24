/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.rdb.repository.data;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: ContainerRecord.java,v 1.2 2004/11/02 18:34:16 geaz Exp $
 *
 * @hibernate.class  table="JCR_REPOSITORY_CONTAINER"
 *
 */

public class ContainerRecord {
    public ContainerRecord() {
    }

  private Long id;
  private String name;

  /**
   * @hibernate.id  generator-class="increment"
   **/
  public Long  getId() {
    return id;
  }
  public void  setId(Long id) {
    this.id = id;
  }


  /**
   * @hibernate.property name="NAME"
   *                     not-null="true"
   *                     unique="true"
   */
   public String getName() {
     return name;
   }
   public void setName(String name) {
     this.name = name;
   }

}
