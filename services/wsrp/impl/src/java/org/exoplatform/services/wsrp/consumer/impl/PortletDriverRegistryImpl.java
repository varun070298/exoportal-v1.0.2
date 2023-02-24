/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.impl;


import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import org.exoplatform.services.wsrp.consumer.*;
import org.exoplatform.services.wsrp.exceptions.WSRPException;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 22:49:30
 */

public class PortletDriverRegistryImpl implements PortletDriverRegistry {

  private Map portletDrivers = new HashMap();  

  public PortletDriverRegistryImpl() {
  }

  public PortletDriver getPortletDriver(WSRPPortlet portlet) throws WSRPException {
    PortletDriver driver = null;
    if ((driver = (PortletDriver) portletDrivers.get(portlet.getPortletKey().toString())) == null) {      
      driver = new PortletDriverImpl(portlet);
      portletDrivers.put(portlet.getPortletKey().toString(), driver);
    }
    return driver;
  }

  public Iterator getAllPortletDrivers() {
    return portletDrivers.values().iterator();
  }

}