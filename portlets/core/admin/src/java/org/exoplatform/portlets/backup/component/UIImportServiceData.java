/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.backup.component;

import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.backup.ImportExportService;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIImportServiceData.java,v 1.6 2004/09/21 00:16:11 tuan08 Exp $
 */
public class UIImportServiceData extends UIImportData {
 
  public UIImportServiceData(ImportExportService service) {
  	super(service) ;
  	setId("UIImportServiceData") ;
  	setDirectory(service_.getServiceDataDirectory()) ;
  	populateImportData() ;
    addActionListener(RefreshActionListener.class, "refresh") ;
    addActionListener(ViewDataActionListener.class, "viewData") ;
    addActionListener(ImportServiceActionListener.class, "import") ;
    addActionListener(ImportAllActionListener.class, "importAll") ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
  }
  
  static public  class ImportServiceActionListener extends ExoActionListener  {
    public ImportServiceActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIImportServiceData uiComp = (UIImportServiceData) event.getSource() ;
  		String dataName = event.getParameter("dataName") ;
  		String serviceName = dataName.substring(0, dataName.length() - 4) ;
  		uiComp.service_.importServiceData(serviceName) ;
    }
  }
  
  static public class ImportAllActionListener extends ExoActionListener  {
    public ImportAllActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIImportServiceData uiComp = (UIImportServiceData) event.getSource() ;
  		uiComp.service_.importServiceData() ;
    }
  }
}