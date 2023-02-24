/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.rdb.repository.data;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: PathReference.java,v 1.2 2004/11/02 18:34:16 geaz Exp $
 *
 *
 * @hibernate.class  table="JCR_REPOSITORY_REFERENCE"
 *
 */

public class PathReference {
    public PathReference() {
    }

  private Long id;
  private String path;
  private UUIDReference uuidRef;

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
   * @hibernate.property name="PATH"
   *                     not-null="true"
   */
   public String getPath() {
     return path;
   }
   public void setPath(String path) {
     this.path = path;
   }

  /**
   * @hibernate.many-to-one  column="UUID_REFERENCE_ID"
   *                         ????not-null="true"
   */
  public UUIDReference  getUuidRef() {
    return uuidRef;
  }

  public void  setUuidRef(UUIDReference UUID) {
    this.uuidRef = UUID;
  }

}