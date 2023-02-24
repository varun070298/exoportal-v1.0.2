/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.portal;

import org.exoplatform.test.web.*;
import org.exoplatform.test.web.unit.ClickLinkWithText;
import org.exoplatform.test.web.unit.NewSessionUnit;
import org.exoplatform.test.web.validator.ExpectTextValidator;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NewAccountSuite.java,v 1.1 2004/10/11 23:27:29 tuan08 Exp $
 **/
public class NewAccountSuite extends WebUnitSuite {
  
  public NewAccountSuite() {
    super("NewAccountSuite", "Create a new user account") ;
    
    addWebUnit(
        new NewSessionUnit("NewSession", "Create new session and go to the home page for the first time")
    );
    addWebUnit(
        new ClickLinkWithText("GoToMyPortalPage", "Tell the web client click on the My Portal page").
        setTextLink("My Portal").
        addValidator(new ExpectTextValidator("New Account"))
    ) ;           
    addWebUnit(CommonUnit.createNewAccountUnit("#{client.name}", "#{client.name}")) ;      
  }
}