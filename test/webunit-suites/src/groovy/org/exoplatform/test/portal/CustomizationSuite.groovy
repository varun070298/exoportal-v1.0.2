/**************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved. *
 * Please look at license.txt in info directory for more license detail.  *
 **************************************************************************/
package org.exoplatform.test.portal;
import org.exoplatform.test.web.*;
import org.exoplatform.test.web.unit.*;
import org.exoplatform.test.web.validator.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: CustomizationSuite.java,v 1.2 2004/10/21 15:21:49 tuan08 Exp $
 **/
public class CustomizationSuite extends WebUnitSuite {
  
  public CustomizationSuite() {
    super("CutomizePortalSuite", "Add and edit in the the portal") ;
    // add a new page node
    CommonUnit.addCreateNewPage(this, "", "newNode") ;
    //edit page node
    addWebUnit( CommonUnit.CLICK_EDIT_NAVIGATION_MODE) ;
    addWebUnit(
        new ClickURLLinkUnit("ClickEditPageNode", "Click on the edit node button").
        setURL("uicomponent=UIMenu&op=editNode&nodeURI=/newNode").
        addValidator(new ExpectFormNameValidator("pageNodeForm"))
    ) ;
    addWebUnit(
        new SubmitFormUnit("ClickSearchLink", "Click search page link").
        setFormName("pageNodeForm").
        setField("name","newNode").
        setField("displayName","newNode").
        setField("xhtmlPageReference","exo:/home").
		setField("op","PhaseId[1].searchXhtmlRefPage").
        addValidator(new ExpectFormNameValidator("searchForm"))
    ) ;
    addWebUnit(
        new ClickURLLinkUnit("ClickSelectPage", "Select first page").
        setURL("op=select&objectId=").
        addValidator(new ExpectFormNameValidator("pageNodeForm"))
    ) ;
    addWebUnit(
        new SubmitFormUnit("SaveChange", "Save the changes").
        setFormName("pageNodeForm").
        setField("name","newNode").
        setField("displayName","newNode").
        setField("xhtmlPageReference","exo:/home").
        setField("op","save").
        addValidator(new ExpectLinkWithTextValidator("newNode"))
    ) ;
    // select and an existing page 
    addWebUnit(
        new ClickURLLinkUnit("ClickBrowsePage", "Click on the browse page button").
        setURL("uicomponent=UIToolbarNav&op=browsePage").
        addValidator(new ExpectFormNameValidator("searchForm"))
    ) ;
    addWebUnit(
        new ClickURLLinkUnit("ClickSelectPage", "Select first page").
        setURL("uicomponent=UIPageList&op=")
    ) ;
    addWebUnit(CommonUnit.CLICK_VIEW_MODE) ;
    // add container into the home page
    addWebUnit(CommonUnit.GO_HOME_UNIT) ;
    addWebUnit(CommonUnit.CLICK_EDIT_PAGE_MODE) ;
    addWebUnit(
        new ClickURLLinkUnit("ClickAddContainer", "Tell the web client click on add portlet").
        setURL("portal:action=addContainer").
        setBlockId("page-customizer")
    ) ;
    addWebUnit(
        new SubmitFormUnit("CreateNewContainer", "submit the form with values to create new container").
        setFormName("containerForm").
        setField(WebUnit.ACTION ,"save").
        setField("id", "NewContainer").
        setField("renderer", "ContainerColumnRenderer").
        setField("style","default").
        addValidator(new ExpectBlockValidator("NewContainer-customizer"))
    );
    //edit container properties 
    addWebUnit(
        new ClickURLLinkUnit("ClickEditContainer", "Edit the container properties").
        setURL("portal:action=editProperty").
        setBlockId("NewContainer-customizer")
    ) ;
    addWebUnit(
        new SubmitFormUnit("UpdateContainer", "submit the form with the new property values").
        setFormName("containerForm").
        setField(WebUnit.ACTION ,"save").
        setField("id", "OuterContainer").
        setField("renderer", "ContainerRowRenderer").
        setField("style","exo").
        addValidator(new ExpectBlockValidator("OuterContainer-customizer"))
    );
    //add container into the new container
    addWebUnit(
        new ClickURLLinkUnit("ClickAddContainer", "Tell the web client click on add portlet").
        setURL("portal:action=addContainer").
        setBlockId("OuterContainer-customizer")
    ) ; 
    addWebUnit(
        new SubmitFormUnit("CreateNewContainer", "submit the form with values to create new container").
        setFormName("containerForm").
        setField(WebUnit.ACTION ,"save").
        setField("id", "NewContainer").
        setField("renderer", "ContainerColumnRenderer").
        setField("style","default").
        addValidator(new ExpectBlockValidator("NewContainer-customizer"))
    );
    //add the first portlet  into the new created container
    addWebUnit(
        new ClickURLLinkUnit("ClickAddPortlet", "Tell the web client click on add portlet").
        setURL("portal:action=addPortlet").
        setBlockId("NewContainer-customizer")
    ) ;
    addWebUnit(
        new ClickLink("ClickContentCategory", "Click the content category").
        setTextLink("content")
    ) ;
    addWebUnit(
        new ClickLink("ClickContentPortlet", "Click on the content portlet").
        setTextLink("DisplayContent")
    ) ;
    addWebUnit(
        new ClickLinkWithText("ClickSelectPortlet", "Select the content portlet").
        setTextLink("select").
        addValidator(new ExpectBlockValidator("DisplayContent"))
    ) ; 
    // edit portlet properties 
    addWebUnit(
        new ClickURLLinkUnit("ClickEditPortlet", "Edit the portlet properties").
        setURL("portal:action=editProperty").
        setBlockId("DisplayContent-customizer")
    ) ;
    addWebUnit(
        new SubmitFormUnit("UpdateContainer", "submit the form with the new property values").
        setFormName("portletForm").
        setField(WebUnit.ACTION ,"save").
        setField("id", "portlet1").
        setField("style", "exo-content").
        addValidator(new ExpectBlockValidator("portlet1-customizer"))
    );
    // add the second portlet  into the new created container
    addWebUnit(
        new ClickURLLinkUnit("ClickAddPortlet", "Tell the web client click on add portlet").
        setURL("portal:action=addPortlet").
        setBlockId("NewContainer-customizer")
    ) ;
    addWebUnit(
        new ClickLink("ClickContenCategory", "Click the content category").
        setTextLink("content")
    ) ;
    addWebUnit(
        new ClickLink("ClickFilePortlet", "Click on the file browser portlet").
        setTextLink("FileExplorer")
    ) ;
    addWebUnit(
        new ClickLinkWithText("ClickSelectPortlet", "Select the file explorer portlet").
        setTextLink("select").
        addValidator(new ExpectBlockValidator("FileExplorer"))
    ) ;
    addWebUnit(
        new ClickURLLinkUnit("SaveChange", "Commit changes").
        setURL("uicomponent=UIToolbarPage&op=savePage").
        addValidator(new ExpectLinkWithURLValidator("portal:componentId=FileExplorer&portal:portletMode=edit"))
    ) ;
    // delete portlet  
    addWebUnit(
        new ClickURLLinkUnit("ClickDeletePortlet", "delete the first added portlet").
        setURL("portal:action=delete").
        setBlockId("portlet1-customizer").
        addValidator(new ExpectBlockValidator("portlet1", 0))
    ) ;
    //  delete container
    addWebUnit(
        new ClickURLLinkUnit("ClickDeleteContainer", "Delete the container").
        setURL("portal:action=delete").
        setBlockId("OuterContainer-customizer").
        addValidator(new ExpectBlockValidator("OuterContainer", 0)).
        addValidator(new ExpectBlockValidator("NewContainer", 0)).
        addValidator(new ExpectBlockValidator("FileExplorer", 0))
    ) ;
    //add portlet to page
    addWebUnit(
        new ClickURLLinkUnit("ClickAddPortlet", "click on add portlet").
        setURL("portal:action=addPortlet").
        setBlockId("page-customizer")
    ) ;
    addWebUnit(
        new ClickLink("ClickContentCategory", "Click the content category").
        setTextLink("content")
    ) ;
    addWebUnit(
        new ClickLink("ClickContentPortlet", "Click on the content portlet").
        setTextLink("DisplayContent")
    ) ;
    addWebUnit(
        new ClickLinkWithText("ClickSelectPortlet", "Select the content portlet").
        setTextLink("select").
        addValidator(new ExpectBlockValidator("DisplayContent"))
    ) ;  
    //check portlet role
    addWebUnit(
        new ClickURLLinkUnit("ClickAddPortlet", "click on add portlet").
        setURL("portal:action=addPortlet").
        setBlockId("page-customizer")
    ) ;
    addWebUnit(
        new ClickURLLinkUnit("SelectWSRPCategory", "click on add portlet").
        setURL("op=showPortletCategory&portletCategoryName=wsrp")
    ) ;
    addWebUnit(
        new ClickLinkWithText("ClickWSRPAdminPortlet", "Click the wsrp admin portlet").
        setTextLink("WSRPAdminPortlet")
    ) ;
    addWebUnit(
        new ClickLinkWithText("ClickSelectPortlet", "Click select portlet").
        setTextLink("select").
        addValidator(new ExpectTextValidator("onload=\"javascript"))
    ) ;
    addWebUnit(CommonUnit.CLICK_VIEW_MODE) ;
  }
}
