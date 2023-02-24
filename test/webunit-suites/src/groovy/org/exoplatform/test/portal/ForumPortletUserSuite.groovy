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
static public class ForumPortletUserSuite extends WebUnitSuite {
  public ForumPortletUserSuite(String path) {
    super("ForumUserSuite", "Go to forum page and do some basic actions") ;
    goToPage(path) ;
    //==============================user units ======================================
    addWebUnit(
        new ClickLinkWithText("ClickForum", "Click on 'Exo Release News' Forum").
        setTextLink("Exo Release News").
        addValidator(new ExpectImageWithAltTextValidator("new topic"))
    ) ;
    addWebUnit(
        new ClickImageLinkWithAltTextUnit("ClickNewTopic", "Tell the web client click on New Topic").
        setAltText("new topic")
    ) ; 
    addWebUnit(
        new SubmitFormUnit("SubmitPostForm", "Tell the web client submit the post").
        setFormName("postForm").
        setField("op","save").
        setField("subject","#{client.name}: eXo Platform").
        setField("message","#{client.name} :  The eXo platform™  software is a powerful Open Source corporate portal and content").
        addValidator(new ExpectImageWithAltTextValidator("reply"))
    ) ;
    addWebUnit(
        new ClickImageLinkWithAltTextUnit("ClickReplyTopic", "Tell the web client click on Reply").
        setAltText("reply")
    ) ; 
    addWebUnit(
        new SubmitFormUnit("ReplyPostForm", "Tell the web client submit the post").
        setFormName("postForm").
        setField("op","save").
        setField("subject","#{client.name}").
        setField("message","#{client.name}").
        addValidator(new ExpectImageWithAltTextValidator("reply"))
    ) ;
    addWebUnit(
        new ClickLinkWithText("ClickForum1Path", "Tell the web client click Forum 1 topic list").
        setTextLink("Exo Release News")
    ) ; 
  }
}