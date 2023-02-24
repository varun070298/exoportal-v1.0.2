package org.exoplatform.test.portal ;

import org.exoplatform.test.web.*;
import org.exoplatform.test.web.unit.*;
import org.exoplatform.test.web.validator.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UserLoginSuite.java,v 1.1 2004/10/11 23:27:29 tuan08 Exp $
 **/
public class ContentPortletSuite extends WebUnitSuite {
  
  public ContentPortletSuite() {
 	super("ContentPortletSuite",  "Create a new page, , add content portlet and test it") ;
 	  addWebUnit(CommonUnit.GO_HOME_UNIT) ;
 	  CommonUnit.addCreateNewPage(this, "", "contentpage" ) ;
    addWebUnit(CommonUnit.CLICK_EDIT_PAGE_MODE) ;
    addWebUnit(
        new ClickURLLinkUnit("ClickAddPortlet", "Click add portlet of the current page").
        setURL("portal:action=addPortlet").
        addValidator(new ExpectLinkWithTextValidator("cancel"))
    ) ; 
    
    addWebUnit(
       new ClickURLLinkUnit("ClickContentApplication", "Click Content application").
       setURL("uicomponent=UIPortletCategories&op=showPortletCategory&portletCategoryName=content")
    ) ;
    
    addWebUnit(
       new ClickLinkWithText("ClickDisplayContent", "Click DisplayStaticContent Portlet").
       setTextLink("DisplayStaticContent")
    ) ;
    
    addWebUnit(
       new ClickURLLinkUnit("ClickContentApplication", "Click Select Static Display Content Portlet").
       setURL("uicomponent=UIPortletInfo&op=selectPortlet")
    ) ;
    
    addWebUnit(
        new ClickURLLinkUnit("SaveThePage", "save change of the current page").
        setURL("uicomponent=UIToolbarPage&op=savePage").
        addValidator(new ExpectTextValidator("No configuration for the content"))
    ) ; 
    addWebUnit(CommonUnit.CLICK_VIEW_MODE) ;
    /*
    addWebUnit(
        new ClickURLLinkUnit("ClickPortletEditMode", "Click edit mode of the display content portlet").
        setURL("portal:componentId=DisplayContent&portal:portletMode=edit").
        addValidator(new ExpectLinkWithTextValidator("new entry"))
    ) ; 
   */
  }
}
