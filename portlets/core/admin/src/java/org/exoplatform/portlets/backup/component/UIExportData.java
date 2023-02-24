/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.backup.component;

import java.util.* ;
import org.exoplatform.faces.core.component.UICommandNode;
import org.exoplatform.faces.core.event.*;
import org.exoplatform.services.backup.ImportExportService;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIExportData.java,v 1.4 2004/09/21 00:16:11 tuan08 Exp $
 */
public class UIExportData extends UICommandNode {
   
  private ImportExportService service_ ;
  private Collection importersExporters_ ;
  private boolean adminRole_ ;

  public UIExportData(ImportExportService service) {
  	setId("UIExportData") ;
  	setRendererType("ExportDataRenderer") ;
  	setName("Export Data");
  	service_ = service ;
  	importersExporters_ = service_.getPlugins();
    addActionListener(ExportAllActionListener.class , "exportAll") ;
    addActionListener(ExportDataActionListener.class, "exportData") ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
  }
  
  public boolean hasAdminRole() { return adminRole_ ; }
  
  public Collection getImportersExporters() { return importersExporters_ ; }
  
  public String getFamily() {
  	return "org.exoplatform.portlets.backup.component.UIExportData" ;
  }
  
  static public class ExportAllActionListener extends ExoActionListener  {
    public ExportAllActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIExportData uiComp = (UIExportData) event.getSource() ;
  		uiComp.service_.exportUserData() ;
  		uiComp.service_.exportServiceData() ;
    }
  }
  
  static public class ExportDataActionListener extends ExoActionListener  {
    public ExportDataActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIExportData uiComp = (UIExportData) event.getSource() ;
  		String name = event.getParameter("name") ;
  		uiComp.service_.exportServiceData(name) ;
    }
  }
}