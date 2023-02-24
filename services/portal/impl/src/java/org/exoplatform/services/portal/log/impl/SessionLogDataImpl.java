/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.log.impl;

import java.util.List ;
import org.exoplatform.services.portal.log.SessionLogData;
import com.thoughtworks.xstream.XStream;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 2, 2004
 * @version $Id$
 * @hibernate.class  table="EXO_SESSION_LOG"
 *                   polymorphism="explicit"
 * @hibernate.cache  usage="read-write"
 */
public class SessionLogDataImpl extends SessionLogDataDescriptionImpl implements SessionLogData {
  transient private List history ;
 
  /**
   * @hibernate.property length="65535" type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getData() throws Exception {
    XStream xstream = PortalLogServiceImpl.getXStreamInstance() ;
    String xml = xstream.toXML(history) ;
    return xml ;
    
  }
  
  public void   setData(String s) { 
    XStream xstream = PortalLogServiceImpl.getXStreamInstance() ;
    history = (List) xstream.fromXML(s) ; 
  }
  
  public List getActionHistory() { return history ; }
  public void setActionHistory(List list) { history = list ; }
}