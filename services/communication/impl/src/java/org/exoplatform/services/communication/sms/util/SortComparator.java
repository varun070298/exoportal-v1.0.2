/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * @author: Ove Ranheim
 * @email:  oranheim@users.sourceforge.net
 */
public class SortComparator implements Comparator {

    public String p_fieldName;

    public SortComparator(String fieldName) {
        p_fieldName = fieldName;
    }

    public int compare(Object o1, Object o2) {
        try {

            Object val1 = PropertyUtils.getProperty(o1, p_fieldName);
            Object val2 = PropertyUtils.getProperty(o2, p_fieldName);

            if (val1.equals(val2)) { return 0; }

            if (val1 instanceof String) { return ((String) val1).compareTo((String) val2); }
            if (val1 instanceof Integer) {
                Integer int1 = new Integer((String) val1);
                Integer int2 = new Integer((String) val2);

                return int1.compareTo(int2);
            }
            if (val1 instanceof java.util.Date) {
                java.util.Date date1 = (java.util.Date) val1;
                java.util.Date date2 = (java.util.Date) val2;
                return date1.compareTo(date2);
            }

        } catch (IllegalAccessException e) {
            throw new SortException(e);
        } catch (InvocationTargetException e) {
            throw new SortException(e);
        } catch (NoSuchMethodException e) {
            throw new SortException(e);
        }

        return 0;
    }
}

