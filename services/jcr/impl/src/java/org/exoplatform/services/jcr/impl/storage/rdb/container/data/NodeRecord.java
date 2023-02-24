/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.rdb.container.data;
import java.util.Set;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeRecord.java,v 1.2 2004/11/02 18:34:15 geaz Exp $
 *
 * @hibernate.class  table="JCR_CONTAINER_NODE"
 *
 */

public class NodeRecord {

  private Long id;
  private String path;
  private Set properties;
  private Long parentId;

  public NodeRecord() {
  }

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
   * @hibernate.property name="PARENT_ID"
   *                     not-null="true"
   */
   public Long getParentId() {
     return parentId;
   }
   public void setParentId(Long parentId) {
     this.parentId = parentId;
   }

	/**
	 * @hibernate.set  lazy="true"
     *                 cascade="all"
     * @hibernate.collection-one-to-many class="org.exoplatform.services.jcr.impl.storage.rdb.container.data.PropertyRecord"
     * @hibernate.collection-key column="NODE_ID"
	 */
	public Set getProperties() {
		return properties;
	}

	public void setProperties(Set properties) {
		this.properties = properties;
	}


}
