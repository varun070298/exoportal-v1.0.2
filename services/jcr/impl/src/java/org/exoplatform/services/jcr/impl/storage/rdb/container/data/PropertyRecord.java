/****************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.jcr.impl.storage.rdb.container.data;
import javax.jcr.Value;
import javax.jcr.StringValue;
import javax.jcr.ValueFormatException;
import java.util.Set;

/**
 * Created by The eXo Platform SARL        .
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: PropertyRecord.java,v 1.2 2004/11/02 18:34:37 geaz Exp $
 * 
 * @hibernate.class  table="JCR_CONTAINER_PROPERTY"
 */
public class PropertyRecord {

    private Long id;
    private String name;
    private Set values;

    public PropertyRecord() {
    }

    /** @hibernate.id  generator-class="increment" */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

  /**
   * @hibernate.property name="NAME"
   *                     not-null="true"
   */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  	/**
	 * @hibernate.set  lazy="true"
     *                 cascade="all"
     * @hibernate.collection-one-to-many class="org.exoplatform.services.jcr.impl.storage.rdb.container.data.ValueRecord"
     * @hibernate.collection-key column="PROPERTY_ID"
	 */
	public Set getValues() {
		return values;
	}

	public void setValues(Set values) {
		this.values = values;
	}

}
