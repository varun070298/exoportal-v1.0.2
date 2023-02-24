/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.log.impl;

import java.util.LinkedList;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.monitor.ActionData;
import org.exoplatform.services.database.DBObjectPageList;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.idgenerator.IDGeneratorService;
import org.exoplatform.services.portal.log.PortalLogService;
import org.exoplatform.services.portal.log.Query;
import org.exoplatform.services.portal.log.SessionLogData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 2, 2004
 * @version $Id$
 */
public class PortalLogServiceImpl implements PortalLogService {
  static private XStream xstream_ ;
  static private String[] MAPPING =
  { "org/exoplatform/services/portal/log/impl/SessionLogDataDescriptionImpl.hbm.xml",
    "org/exoplatform/services/portal/log/impl/SessionLogDataImpl.hbm.xml" } ;
  
  private HibernateService hservice_ ;
  private IDGeneratorService idService_ ;
  
  public PortalLogServiceImpl(HibernateService hservice, IDGeneratorService idService) {
    hservice.addMappingFiles(MAPPING) ;
    hservice_ = hservice ;
    idService_ = idService ;
  }
  
  public SessionLogData getSessionLogData(String id) throws Exception {
    return (SessionLogData)hservice_.findOne(SessionLogDataImpl.class, id) ;
  }

  public void saveSessionLogData(SessionLogData data) throws Exception {
    SessionLogDataImpl impl = (SessionLogDataImpl) data ;
    if(impl.getId() == null) impl.setId(idService_.generateStringID(impl)) ;
    hservice_.create(impl) ;
  }

  public PageList getSessionLogDatas(Query query) throws Exception {
    ObjectQuery oq = new ObjectQuery(SessionLogDataDescriptionImpl.class);
    oq.addLIKE("sessionOwner", query.getSessionOwner()) ;
    oq.addLIKE("remoteUser", query.getRemoteUser()) ;
    oq.addLIKE("IPAddress", query.getIPAddress()) ;
    oq.addLIKE("clientName", query.getClientType()) ;
    oq.addGT("accessTime", query.getFromDate()) ;
    oq.addLT("accessTime", query.getToDate()) ;
    if(query.getError() > 0) {
      oq.addGT("errorCount", Integer.toString(0)) ;
    }
    return new DBObjectPageList(hservice_, oq) ;
  }
  
  static public XStream getXStreamInstance() {
    if (xstream_ == null ) {
      xstream_ = new XStream(new XppDriver());
      xstream_.alias("action-data", ActionData.class);
      xstream_.alias("session-data", LinkedList.class);
    }
    return xstream_ ;
  }
}
