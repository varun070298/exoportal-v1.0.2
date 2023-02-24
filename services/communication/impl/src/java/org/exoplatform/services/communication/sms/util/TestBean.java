/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.util;

/**
 * 
 * @author: Ove Ranheim
 * @email:  oranheim@users.sourceforge.net
 */
public class TestBean {
    private String p_name;
    private String p_address;

    public TestBean()
    {
    }

    public TestBean(String name, String address)
    {
        p_name = name;
        p_address = address;
    }

    public String getName()
    {
        return p_name;
    }

    public void setName(String name)
    {
        p_name = name;
    }

    public String getAddress()
    {
        return p_address;
    }

    public void setAddress(String address)
    {
        p_address = address;
    }
}
