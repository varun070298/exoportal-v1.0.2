/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.rdb.container.data;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: BinaryValueRecord.java,v 1.1 2004/11/02 18:34:15 geaz Exp $
 *
 * @hibernate.joined-subclass table="JCR_CONTAINER_BINARY"
 *                            dynamic-update="true"
 * 
 * @hibernate.joined-subclass-key column="ID"
 *
 */

public class BinaryValueRecord extends ValueRecord {

    private byte[] value;

    public BinaryValueRecord() {
        super();
    }

    public BinaryValueRecord(byte[] value) {
        super();
        this.value = value;
    }

  /**
   * @hibernate.property name="VALUE"
   *                     type="binary" 
   */
    public byte[] getValue(){
        return value;
    }

    public void setValue(byte[] value){
        this.value = value;
    }
}
