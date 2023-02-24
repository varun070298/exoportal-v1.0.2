/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.backup.component;

import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.backup.ImportExportService ;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIImportUserData.java,v 1.8 2004/09/21 00:16:11 tuan08 Exp $
 */
public class UIImportUserData extends UIImportData {
   
  public UIImportUserData(ImportExportService service) {
  	super(service) ;
  	setId("UIImportUserData") ;
    setDirectory(service_.getUserDataDirectory()) ;
  	populateImportData() ;
  	
    addActionListener(RefreshActionListener.class, "refresh") ;
    addActionListener(ViewDataActionListener.class, "viewData") ;
    addActionListener(ImportUserActionListener.class, "importData") ;
    addActionListener(ImportAllActionListener.class,"importAll") ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
  }
  
  static public class ImportUserActionListener extends ExoActionListener  {
    public ImportUserActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
  	  UIImportUserData component = (UIImportUserData)event.getComponent() ;
  		String dataName = event.getParameter("dataName") ;
  		int idx = dataName.indexOf('.') ;
	    String userName = dataName.substring(0, idx) ;
  		component.getBackupService().importUserData(userName) ;
    }
  }
  
  static public class ImportAllActionListener extends ExoActionListener  {
    public ImportAllActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
  	  UIImportUserData component = (UIImportUserData)event.getComponent() ;
  		component.getBackupService().importUserData() ;
    }
  }
}