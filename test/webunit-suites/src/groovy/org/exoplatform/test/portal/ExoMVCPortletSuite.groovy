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
static public class ExoMVCPortletSuite extends WebUnitSuite {
  public ExoMVCPortletSuite(String path) {
    super("ExoMVCPortletSuite", "Go to Exo MVC Portlet and test the basic actions") ;
    goToPage(path) ;
    
    GO_BACK_UNIT =
      new ClickURLLinkUnit("ClickGoBack", "Go back to  the list  script page ").
      setURL("mvc.page.name=groovy.list.script.page").
      addValidator(new ExpectLinkWithURLValidator("mvc.page.name=groovy.list.script.page&action=run&page=groovy.list.script.page")) ;
    //==============================units ======================================
    addWebUnit(
        new ClickURLLinkUnit("ClickGroovyListUserRun", "Run the list user groovy script").
        setURL("action=run&page=groovy.list.user.page").
        addValidator(new ExpectTextValidator("admin@localhost"))
    ) ;
    
    addWebUnit(GO_BACK_UNIT);
    
    addWebUnit(
        new ClickURLLinkUnit("ClickJSPListUserRun", "Run the list user jsp script").
        setURL("action=run&page=jsp.list.user.page").
        addValidator(new ExpectTextValidator("admin@localhost"))
    ) ;
    
    addWebUnit(GO_BACK_UNIT);
    
    addWebUnit(
        new ClickURLLinkUnit("ClickPOJOListUserRun", "Run the list user pojo page").
        setURL("action=run&page=pojo.list.user.page").
        addValidator(new ExpectTextValidator("admin@localhost"))
    ) ;
    
    addWebUnit(GO_BACK_UNIT);
    
    addWebUnit(
        new ClickURLLinkUnit("ClickVelocityListUserRun", "Run the list user velocity page").
        setURL("action=run&page=velocity.list.user.page").
        addValidator(new ExpectTextValidator("admin@localhost"))
    ) ;
    
    addWebUnit(GO_BACK_UNIT);
  }
}