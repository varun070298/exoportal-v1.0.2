/****************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.jcr.impl.storage.rdb.repository.data;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;

/**
 * Created by The eXo Platform SARL        .
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: UUIDReference.java,v 1.2 2004/11/02 18:34:39 geaz Exp $
 * 
 * @hibernate.class  table="JCR_REPOSITORY_UUID"
 */
public class UUIDReference {
    public UUIDReference() {
    }

    private Long id;
    private ContainerRecord container;
    private String uuid;
    private String realPath;
    private Set references;

    /** @hibernate.id  generator-class="increment" */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** @hibernate.property name="UUID"
     *                      not-null="true"
     *                     
     **/
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String UUID) {
        this.uuid = UUID;
    }

    /** @hibernate.property name="REAL_PATH"
     *                      not-null="true"
     *
     **/
    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    /** @hibernate.many-to-one  column="CONTAINER_ID"
     *                          not-null="true"
     **/
    public ContainerRecord getContainer() {
        return container;
    }

    public void setContainer(ContainerRecord container) {
        this.container = container;
    }

    /**
     * @hibernate.set  lazy="true"
     *                 cascade="all"
     * @hibernate.collection-one-to-many class="org.exoplatform.services.jcr.impl.storage.rdb.repository.data.PathReference"
     * @hibernate.collection-key column="UUID_REFERENCE_ID"
     */
	public Set getReferences() {
		return references;
	}

	public void setReferences(Set references) {
		this.references = references;
	}

	public Set getRefPaths() {
        Iterator refs = references.iterator();
        Set paths = new HashSet();
        while(refs.hasNext()) {
            paths.add( ((PathReference) refs.next()).getPath());
        }
		return paths;
	}
}
