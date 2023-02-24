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
static public class  ForumPortletAdminSuite extends WebUnitSuite {
  public ForumPortletAdminSuite(String path) {
    super("ForumAdminSuite", "Go to forum page and do some admin actions") ;
    goToPage(path) ;
    addWebUnit(
        new ClickURLLinkUnit("ClickForumConfigMode", "Click on the config mode of the forum portlet").
        setURL("portal:componentId=forum&portal:portletMode=config").
        setBlockId("forum")
    ) ;
    addWebUnit(
        new ClickURLLinkUnit("DeleteForumCategory", "Delete old category if exists").
        setURL("uicomponent=UIAdminViewCategories&op=deleteCategory").
        addCondition(new HaveLinkWithURLCondition("uicomponent=UIAdminViewCategories&op=deleteCategory"))
    ) ;
    addWebUnit(
        new ClickURLLinkUnit("DeleteForumCategory", "Delete old category if exists").
        setURL("uicomponent=UIAdminViewCategories&op=deleteCategory").
        addCondition(new HaveLinkWithURLCondition("uicomponent=UIAdminViewCategories&op=deleteCategory"))
    ) ;
    addWebUnit(
        new ClickLinkWithText("ClickNewCategory", "Click New Category Link").
        setTextLink("New Category").
        addValidator(new ExpectFormNameValidator("category"))
    ) ;
    addWebUnit(
        new SubmitFormUnit("SubmitCategroryForm", "Fill the  category information and submit").
        setFormName("category").
        setField("categoryName", "Exo Announcement").
        setField("categoryDesc", "for exo news").
        setField("order", "0").
        setField("op","save").
        addValidator(new ExpectTextValidator("Exo Announcement"))
    ) ;
    addWebUnit(
        new ClickLink("ClickAddForum", "Add new forum").
        setTextLink("add").
        addValidator(new ExpectFormNameValidator("forumForm"))
    ) ;
    addWebUnit(
        new SubmitFormUnit("SubmitForumForm", "Fill the  forum information and submit").
        setFormName("forumForm").
        setField("forumName", "Exo Release News").
        setField("forumDesc", "for exo news, and release information").
        setField("viewRole","guest").
        setField("createTopic","guest").
        setField("replyTopic","guest").
        setField("moderators","admin").
        setField("order","0").
        setField("op","save").
        addValidator(new ExpectLinkWithURLValidator("uicomponent=UIAdminViewCategories&op=addForum"))
    ) ;
    addWebUnit(
        new ClickURLLinkUnit("ClickForumViewMode", "Click on the view mode of the forum portlet").
        setURL("portal:componentId=forum&portal:portletMode=view").
        setBlockId("forum")
    ) ;
    addWebUnit(
        new ClickLinkWithText("ClickForum", "Click on 'Exo Release News' Forum").
        setTextLink("Exo Release News").
        addValidator(new ExpectImageWithAltTextValidator("new topic"))
    ) ;
    addWebUnit(
        new ClickImageLinkWithAltTextUnit("ClickNewTopic", "Click on New Topic link").
        setAltText("new topic")
    ) ; 
    addWebUnit(
        new SubmitFormUnit("SubmitPostForm", "Tell the web client submit the post").
        setFormName("postForm").
        setField("op","save").
        setField("subject","new subject").
        setField("message","test message")
    ) ;
    addWebUnit(new ClickLinkWithText("ClickForumTopics", "Click on forum 'Exo Release News' topics").
        setTextLink("Exo Release News")
    ) ;
    addWebUnit(new ClickImageLinkWithAltTextUnit("ClickDeleteTopic", "Click on 'de' to delete the new topic").
        setAltText("delete").
        setBlockId("forum").
        addValidator(new ExpectLinkWithTextValidator("new subject", 0))) ; 
  }
}