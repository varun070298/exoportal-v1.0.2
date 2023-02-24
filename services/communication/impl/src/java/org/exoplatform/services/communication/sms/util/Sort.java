/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author: Ove Ranheim
 * @email:  oranheim@users.sourceforge.net
 */
public class Sort {

    public static final int ASC = 0;
    public static final int DESC = 1;

    public Sort() {
    }

    public static List sort(List list, String fieldName, int order) {

        Collections.sort(list, new SortComparator(fieldName));
        if (order == Sort.DESC) Collections.reverse(list);

        return list;
    }

    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        list.add(new TestBean("ove", "toyen"));
        list.add(new TestBean("tuva", "lokka"));
        list.add(new TestBean("knut", "st.haugen"));
        list.add(new TestBean("tor-egil", "Ljan"));
        List sortedList = Sort.sort(list, "name", Sort.DESC);
        for (Iterator iterator = sortedList.iterator(); iterator.hasNext();) {
            TestBean myBean = (TestBean) iterator.next();
            System.out.println(myBean.getName());
        }
    }

    static public void putInSorted(List a, Comparator c) {
        int cnt = 0;
        for (Iterator iterator = a.iterator(); iterator.hasNext();) {
            Object temp = (Object) iterator.next();

            int j = cnt++ - 1;
            for (; j >= 0 && c.compare(temp, a.get(j)) < 0; j--)
                a.set(j + 1, a.get(j));

            a.set(j + 1, temp);

        }
    } 

}

