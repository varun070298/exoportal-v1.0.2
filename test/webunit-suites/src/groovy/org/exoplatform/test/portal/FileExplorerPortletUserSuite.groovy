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
public class FileExplorerPortletUserSuite  extends WebUnitSuite {
  public FileExplorerPortletUserSuite(String path) {
    super("FileExplorerUserSuite", "Go to the file explorer and test the user actions") ;
    goToPage(path) ;
    addWebUnit(
        new ClickLink("ClickHelpDirectory", "Go to help directory").
        setTextLink("help").
        setBlockId("file-explorer").
        addValidator(new ExpectLinkWithURLValidator("op=viewNodeContent&uri=/help/file-explorer-portlet-help.html"))
    );
  }
}
