/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.portal;

import org.exoplatform.test.web.WebUnitSuite;
import org.exoplatform.test.web.unit.ClickLinkWithText;
import org.exoplatform.test.web.unit.ClickURLLinkUnit;
import org.exoplatform.test.web.unit.SubmitFormUnit;
import org.exoplatform.test.web.unit.WebUnit;
import org.exoplatform.test.web.validator.ExpectFormNameValidator;
import org.exoplatform.test.web.validator.ExpectLinkWithTextValidator;
import org.exoplatform.test.web.validator.ExpectTextValidator;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MessagePortletUserSuite.java,v 1.2 2004/11/03 16:00:48 tuan08 Exp $
 **/
public class MessagePortletUserSuite extends WebUnitSuite {
  
  WebUnit CLICK_EDIT_MODE = 
    new ClickURLLinkUnit("ClickMessagePortletEditMode", "Click on edit mode of the message portlet").
    setURL("portal:componentId=message-center&portal:portletMode=edit").
    addValidator(new ExpectTextValidator("Welcome To The Account Configuration Panel")) ;
  
  MessagePortletUserSuite(String path) {
    super("MessagePortletUserSuite", "DO to message portlet and execute the basic actions") ;
    goToPage(path) ;
    
    addWebUnit(CLICK_EDIT_MODE) ;
    // create standalone account
    addWebUnit(
        new ClickLinkWithText("ClickAddNewAccount", "Click 'add new account' link and go to new account form").
        setTextLink("add new account").
        addValidator(new ExpectFormNameValidator("accountForm"))
    ) ;
    addWebUnit(
        new SubmitFormUnit("SubmitAccountForm", "Submit new account data").
        setFormName("accountForm").
        setField(WebUnit.ACTION,"save").
        setField("accountName","standalone").
        setField("ownerName","IC3 Patient").
        setField("protocol","standalone").
        setField("signature","My Signature").
        addValidator(new ExpectLinkWithTextValidator("standalone"))
    ) ;
    //create imap account
    addWebUnit(
        new ClickLinkWithText("ClickAddNewAccount", "Click 'add new account' link and go to new account form").
        setTextLink("add new account").
        addValidator(new ExpectFormNameValidator("accountForm"))
    ) ;
    addWebUnit(
        new SubmitFormUnit("ChangeToImapAccountForm", "Switch to imap account form").
        setFormName("accountForm").
        setField(WebUnit.ACTION,"PhaseId[1].changeProtocol").
        setField("protocol","imap")
    ) ;
    addWebUnit(
        new SubmitFormUnit("CreateImapAccount", "Submit data and create a new imap account").
        setFormName("accountForm").
        setField(WebUnit.ACTION,"save").
        setField("accountName","imap").
        setField("ownerName","IC3 Patient").
        setField("protocol","imap").
        setField("userName","exo.platform").
        setField("password","exo2004").
        setField("mailServer","imap.laposte.net").
        setField("signature","My Signature").
        addValidator(new ExpectLinkWithTextValidator("standalone"))
    ) ;
    //Go back to view mode
    addWebUnit(
        new ClickURLLinkUnit("ClickMessagePortletViewMode", "Click on the view mode of the message portlet").
        setURL("portal:componentId=message-center&portal:portletMode=view").
        addValidator(new ExpectFormNameValidator("quickSearchForm"))
    ) ;
    
    //Go back to edit mode and delete all the accounts
    addWebUnit(CLICK_EDIT_MODE) ;
    
    //Select and delete standalone account
    addWebUnit(
        new ClickLinkWithText("SelectStandaloneAccount", "Click 'standalone'  link to select standalone account").
        setTextLink("standalone")
    ) ;
    addWebUnit(
        new ClickLinkWithText("DeleteStandaloneAccount", "Click 'delete account'  link to delete standalone account").
        setTextLink("delete account").
        addValidator(new ExpectLinkWithTextValidator("standalone", 0))
    ) ;
    
    //    Select and delete imap account
    addWebUnit(
        new ClickLinkWithText("SelectImapAccount", "Click 'imap'  link to select imap account").
        setTextLink("imap")
    ) ;
    
    addWebUnit(
        new ClickLinkWithText("DeleteImapAccount", "Click 'delete account'  link to delete the imap account").
        setTextLink("delete account").
        addValidator(new ExpectLinkWithTextValidator("imap", 0))
    ) ;
  }
}