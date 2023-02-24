/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.rdb.container.data;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: SoftLinkValueRecord.java,v 1.1 2004/11/02 18:34:38 geaz Exp $
 *
 * @hibernate.joined-subclass
 *            table="JCR_CONTAINER_SOFTLINK"
 *            dynamic-update="true"
 * @hibernate.joined-subclass-key
 *            column="ID"
 *
 */

public class SoftLinkValueRecord extends ValueRecord {

    private String value;

    public SoftLinkValueRecord() {
        super();
    }

    public SoftLinkValueRecord(String value) {
        super();
        this.value = value;
    }

  /**
   * @hibernate.property name="VALUE"
   */
    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}
