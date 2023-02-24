/**************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved. *
 * Please look at license.txt in info directory for more license detail.  *
 **************************************************************************/
package org.exoplatform.test.portal ;

import org.exoplatform.test.web.*;
import org.exoplatform.test.web.unit.* ;
import org.exoplatform.test.web.validator.*;
/** 
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: CommonUnit.java,v 1.2 2004/10/21 15:21:49 tuan08 Exp $
 **/
public class CommonUnit extends WebUnitSuite {
 
  final static public WebUnit GO_HOME_UNIT = 
    new ClickLinkWithText("GoToHome", "Tell the web client click on the Home page").
    setTextLink("Home") ; 
    
  final static public WebUnit CLICK_VIEW_MODE = 
  	new ClickURLLinkUnit("ClickViewMode", "Tell the web client click on the view Mode link").
    setURL("op=changeMode&mode=normal").
    addValidator(new ExpectLinkWithURLValidator("uicomponent=UIToolbarView&op=changeMode&mode=edit-portal")) ; 
    
  final static public WebUnit CLICK_EDIT_NAVIGATION_MODE = 
  	new ClickURLLinkUnit("ClickEditNavigationMode", "Tell the web client click on the Edit Navigation Mode link").
    setURL("UIToolbarView&op=changeMode&mode=edit-navigation").
	addValidator(new ExpectLinkWithURLValidator("uicomponent=UIToolbarNav&op=saveNode")) ; 
    
  final static public WebUnit CLICK_EDIT_PAGE_MODE = 
  	new ClickURLLinkUnit("ClickEditPageMode", "Tell the web client click on the Edit Page Mode link").
    setURL("UIToolbarView&op=changeMode&mode=edit-page").
	  addValidator(new ExpectLinkWithURLValidator("uicomponent=UIToolbarPage&op=savePage")) ; 
    
  static WebUnit createNewAccountUnit(String userName , String password) {
    WebUnit unit = 
      new SubmitFormUnit("CreateNewAccount", "create exoplatform account").
      setFormName("accountForm").
      setField("username", userName).
      setField("password1x", password).
      setField("password2x", userName).
      setField("firstname", userName).
      setField("lastname", userName).
      setField("email", "user@localhost.net").
      setField("op","save").
      addValidator(new ExpectTextValidator("New Account")) ;
    return unit ;
  }
  
  static WebUnit addCreateNewPage(WebUnitSuite suite, String parentNode, String pageName) {
    suite.addWebUnit(CLICK_EDIT_NAVIGATION_MODE) ;
    suite.addWebUnit(
        new ClickURLLinkUnit("ClickAddNode", "Add a child node to the root node").
        setURL("uicomponent=UIMenu&op=addNode&nodeURI=${parentNode}")
    ) ;
    
    suite.addWebUnit(
        new SubmitFormUnit("GoToSearchPage", "Go to search page").
        setFormName("pageNodeForm").
        setField("op","PhaseId[1].searchXhtmlRefPage").
        addValidator(new ExpectFormNameValidator("searchForm"))
    ) ;
    
    suite.addWebUnit(
        new ClickURLLinkUnit("ClickAddNewPage", "Go To add new page form").
        setURL("uicomponent=UIToolbar&op=newPage").
        addValidator(new ExpectFormNameValidator("pageModelForm"))
    ) ;
    
    suite.addWebUnit(
        new SubmitFormUnit("AddNewBlankPage", "create a new blank page").
        setFormName("pageModelForm").
        setField("name", parentNode + "/" + pageName).
        setField("template", "blank-page").
        setField("op","save").
        addValidator(new ExpectFormNameValidator("searchForm"))
    ) ;
    
    suite.addWebUnit(
        new ClickURLLinkUnit("SelectContentPage", "Select the new created page").
        setURL("op=select&objectId=#{client.name}:" + parentNode + "/" + pageName).
        addValidator(new ExpectFormNameValidator("pageNodeForm"))
    ) ;
    
    suite.addWebUnit(
        new SubmitFormUnit("SubmitAddNodeForm", "Create new node with an existed reference page").
        setFormName("pageNodeForm").
        setField("name", pageName).
        setField("displayName",pageName).
        setField("op","save").
        addValidator(new ExpectLinkWithTextValidator(pageName)).
        addValidator(new ExpectLinkWithURLValidator("uicomponent=UIMenu&op=editNode&nodeURI=" + parentNode + "/" + pageName))
    ) ;
    
    suite.addWebUnit(
        new ClickURLLinkUnit("SaveNodeChange", "Commit changes").
        setURL("uicomponent=UIToolbarNav&op=saveNode")
    ) ;
    
    suite.addWebUnit(CLICK_VIEW_MODE) ;
  }
  
  static public void addClickUnits(WebUnitSuite suite , String path) {
    String[] links = path.split("/") ;
    for(text  in  links) {
      suite.addWebUnit(new ClickLinkWithText("Click:" + text, "Go to " + text + "page").
                      setTextLink(text)) ;
    }
  }
}
