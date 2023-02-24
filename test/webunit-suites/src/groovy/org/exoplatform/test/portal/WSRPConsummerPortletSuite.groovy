/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.portal ;

import org.exoplatform.test.web.*;
import org.exoplatform.test.web.condition.*;
import org.exoplatform.test.web.unit.*;
import org.exoplatform.test.web.validator.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ForumPortletSuite.java,v 1.3 2004/10/21 15:21:49 tuan08 Exp $
 **/
static public class WSRPConsummerPortletSuite extends WebUnitSuite {
  public WSRPConsummerPortletSuite(String path) {
    super("WSRPConsummerPortletSuite", "Go to WSRP Consummer portlet and select the exomvc portlet") ;
    goToPage(path) ;
 
    addWebUnit(
        new ClickURLLinkUnit("ClickWSRPMode", "click on the wsrp mode of the consummer portlet").
        setURL("portal:componentId=wsrp-consumer&portal:portletMode=wsrp").
        addValidator(new ExpectLinkWithTextValidator("exo producer"))
    ) ;
    
    addWebUnit(
        new ClickURLLinkUnit("ClickWSRPMode", "click on the wsrp mode of the consummer portlet").
        setURL("portletHandle=exomvc/ExoMVC&action=selectPortlet").
        addValidator(new ExpectLinkWithTextValidator("exo producer"))
    ) ;
    
    addWebUnit(
        new ClickURLLinkUnit("ClickWSRPViewMode", "click on the view mode of the consummer portlet").
        setURL("portal:componentId=wsrp-consumer&portal:portletMode=view").
        addValidator(new ExpectLinkWithURLValidator("mvc.page.name=groovy.list.script.page&action=run&page=groovy.list.script.page"))
    ) ;
  }
}