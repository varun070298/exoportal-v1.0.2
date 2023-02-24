/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.portal;

import org.exoplatform.test.web.*;
import org.exoplatform.test.web.unit.*;
import org.exoplatform.test.web.validator.ExpectLinkWithTextValidator;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: RandomPageBrowseSuite.java,v 1.1 2004/10/11 23:27:29 tuan08 Exp $
 **/
public class RandomPageBrowseSuite extends WebUnitSuite {
  
  public RandomPageBrowseSuite() {
    super("RandomPageBrowse", "Go to the various pages in exo portal") ;
    addWebUnit(
        new ClickLinkWithText("GoToCommunityPage", "Tell the web client click on the Community page").
        setTextLink("Community").
        addValidator(new ExpectLinkWithTextValidator("Forum"))
    ) ;
    addWebUnit(
        new ClickLinkWithText("GoToDemo", "Tell the web client click on the Demo page").
        setTextLink("Demo").
        addValidator(new ExpectLinkWithTextValidator("eXo Platform MVC"))
    ) ;
    addWebUnit(
        new ClickLink("GoToSearchPage", "Tell the web client click on the search page").
        setTextLink("Search").
        addValidator(new ExpectLinkWithTextValidator("Advanced Search"))
    ) ;
    addWebUnit(
        new ClickLinkWithText("GoToSiteMapPage", "Tell the web client click on the site mape page").
        setTextLink("Site Map").
        addValidator(new ExpectLinkWithTextValidator("Forum"))
    ) ;
  }
}