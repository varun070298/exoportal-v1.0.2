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
static public class WSRPAdminPortletSuite extends WebUnitSuite {
  public WSRPAdminPortletSuite(String path) {
    super("WSRPAdminPortletSuite", "Go to WSRP Admin portlet and test creation of the producers") ;
    goToPage(path) ;
 
    addWebUnit(
        new ClickLinkWithText("ClickCreateProducer", "Click create producer tab").
        setTextLink("Create Producer").
        addValidator(new ExpectFormNameValidator("producerForm"))
    ) ;
    
    addWebUnit(
        new SubmitFormUnit("ClickSaveProducer", "Click save to create a new producer with the default information").
        setFormName("producerForm").
        setField("op","save")
    ) ;
    
    addWebUnit(
        new ClickLink("ClickProducers", "Click the 'Producers' tab").
        setTextLink("Producers").
        addValidator(new ExpectLinkWithTextValidator("exo producer"))
    ) ;
  }
}