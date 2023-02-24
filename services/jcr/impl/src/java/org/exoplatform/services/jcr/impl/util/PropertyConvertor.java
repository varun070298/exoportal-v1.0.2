/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.util;

import javax.jcr.*;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: PropertyConvertor.java,v 1.6 2004/09/03 09:59:42 geaz Exp $
 */

public class PropertyConvertor {

  public static Value convert(Value oldValue, int type) throws IllegalStateException, RepositoryException {
    Value[] values = new Value[1];
    values[0] = oldValue;
    return convert(values, type)[0];
  }

  public static Value[] convert(Value[] oldValues, int type) throws RepositoryException {
    Value[] newValues = new Value[oldValues.length];
    if(oldValues.length == 0)
        return oldValues;
    if (oldValues[0] != null && oldValues[0].getType() == type)
        return oldValues;

    for (int i = 0; i < oldValues.length; i++) {
      Value value = oldValues[i];
        switch (type) {
          case PropertyType.STRING:
            newValues[i] = (oldValues[i] == null)?new StringValue(null):new StringValue(value.getString());
            break;
          case PropertyType.BINARY:
            newValues[i] = (oldValues[i] == null)?new BinaryValue((String)null):new BinaryValue(value.getStream());
            break;
          case PropertyType.BOOLEAN:
            newValues[i] = (oldValues[i] == null)?new BooleanValue(null):new BooleanValue(value.getBoolean());
            break;
          case PropertyType.LONG:
            newValues[i] = (oldValues[i] == null)?new LongValue(null):new LongValue(value.getLong());
            break;
          case PropertyType.DOUBLE:
            newValues[i] = (oldValues[i] == null)?new DoubleValue(null):new DoubleValue(value.getDouble());
            break;
          case PropertyType.DATE:
            newValues[i] = (oldValues[i] == null)?new DateValue(null):new DateValue(value.getDate());
            break;
          case PropertyType.SOFTLINK:
            newValues[i] = (oldValues[i] == null)?new SoftLinkValue(null):new SoftLinkValue(value.getString());
            break;
          case PropertyType.REFERENCE:
            newValues[i] = (oldValues[i] == null)?new ReferenceValue((String)null):new ReferenceValue(value.getString());
            break;
          default:
            newValues[i] = null;
//            throw new IllegalArgumentException("unknown type "+type);
        }
      }
      return newValues;
//    }
  }

}
