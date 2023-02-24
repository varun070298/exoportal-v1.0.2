/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.portal;

import org.exoplatform.test.web.*;
import org.exoplatform.test.web.unit.*;
import org.exoplatform.test.web.validator.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: FileExplorerPortletUserSuite.java,v 1.1 2004/10/11 23:27:28 tuan08 Exp $
 **/
public class FileExplorerPortletAdminSuite  extends WebUnitSuite {
  
  public FileExplorerPortletAdminSuite(String path) {
    super("FileExplorerAdminSuite", "Go to File Explorer and test the admin actions") ;
    goToPage(path) ;
    addWebUnit(
        new ClickURLLinkUnit("GotoAdminTab", "Go To Admin Tab").
        setURL("uicomponent=UINode&op=selectTab&tabId=UIAdmin").
        setBlockId("file-explorer").
        addValidator(new ExpectTextValidator("Remove This File Or Directory"))
    );
    addWebUnit(
        new ClickURLLinkUnit("GotoEditorTab", "Go to editor tab").
        setURL("uicomponent=UIAdmin&op=selectTab&tabId=UINodeEditor").
        setBlockId("file-explorer").
        addValidator(new ExpectFormNameValidator("uploadForm"))
    );
    addWebUnit(
        new ClickURLLinkUnit("GotoPropertiesTab", "Go to the properties tab").
        setURL("uicomponent=UIAdmin&op=selectTab&tabId=UIFileNodeInfo").
        setBlockId("file-explorer").
        addValidator(new ExpectTextValidator("Remove This File Or Directory"))
    );
    
    addWebUnit(
        new ClickURLLinkUnit("GotoViewTab", "Go Back To List children tab").
        setURL("uicomponent=UINode&op=selectTab&tabId=UINodeContent").
        setBlockId("file-explorer")
    );
    
    addWebUnit(
        new ClickURLLinkUnit("ClickIntroFile", "Click on the file intro-to-content-portlet_en.html").
        setURL("op=changeNode&objectId=/intro-to-content-portlet_en.html").
        setBlockId("file-explorer").
        addValidator(new ExpectTextValidator("Hello, This is the first time you use the file portlet."))
    );
    
    addWebUnit(
       new ClickURLLinkUnit("GotoAdminTab", "Go To Admin Tab").
       setURL("uicomponent=UINode&op=selectTab&tabId=UIAdmin").
       setBlockId("file-explorer").
       addValidator(new ExpectTextValidator("Remove This File Or Directory"))
    );
    
    addWebUnit(
       new ClickURLLinkUnit("GotoEditorTab", "Go to editor tab").
       setURL("uicomponent=UIAdmin&op=selectTab&tabId=UINodeEditor").
       setBlockId("file-explorer").
       addValidator(new ExpectFormNameValidator("contentForm"))
    );
    
    addWebUnit(
       new SubmitFormUnit("SubmitSaveContentForm", "Try To save the current content").
       setFormName("contentForm").
       setField("op","saveContent").
       addValidator(new ExpectTextValidator("info: Your file has been saved successfully."))
    ) ;
  }
}