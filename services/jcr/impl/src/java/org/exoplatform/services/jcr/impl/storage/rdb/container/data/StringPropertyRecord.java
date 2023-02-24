/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.rdb.container.data;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: StringPropertyRecord.java,v 1.1 2004/09/19 19:18:06 geaz Exp $
 *
 * @hibernate.joined-subclass
 *            table="CONTAINER_STRING"
 *            dynamic-update="true"
 * @hibernate.joined-subclass-key
 *            column="ID"
 *
 */

public class StringPropertyRecord extends PropertyRecord {

    private String value;

    public StringPropertyRecord() {
        super();
    }

  /**
   * @hibernate.property name="VALUE" length="65535" type="org.exoplatform.services.database.impl.TextClobType" 
   */
    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}
