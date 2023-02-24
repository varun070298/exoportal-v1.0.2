/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.backup.component;

import java.util.* ;
import org.exoplatform.faces.core.component.UIPortlet;

/**
 * May 31, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UIBackupPortlet extends UIPortlet {
	
	public UIBackupPortlet(UIExportData uiExportData,
			                   UIImportServiceDataNode uiImportServiceDataNode,
												 UIImportUserDataNode uiImportUserDataNode) {
		setId("UIBackupPortlet") ;
		setClazz("UIBackupPortlet") ;
		setRendererType("BackupPortletRenderer") ; 
		setName("Backup Menu");
		List children = getChildren() ;
		uiExportData.setRendered(true) ;
		children.add(uiExportData) ;
		uiImportServiceDataNode.setRendered(false) ;
		children.add(uiImportServiceDataNode) ;
		uiImportUserDataNode.setRendered(false) ;
		children.add(uiImportUserDataNode) ;
	}
	
	public String getFamily() {
		return "org.exoplatform.portlets.backup.component.UIBackupPortlet" ;
	}
}