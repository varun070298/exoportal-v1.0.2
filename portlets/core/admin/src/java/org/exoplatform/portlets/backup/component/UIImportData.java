/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.backup.component;

import java.util.* ;
import java.io.File ;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.event.*;
import org.exoplatform.portlets.backup.component.model.ImportData;
import org.exoplatform.services.backup.ImportExportService ;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIImportData.java,v 1.5 2004/09/21 00:16:11 tuan08 Exp $
 */
public class UIImportData extends UIExoCommand {
  protected String tabTitle_ ;
  protected ImportExportService service_ ;
  protected String directory_ ;
  protected List importDatas_ ;
  protected boolean adminRole_ = false ;

  public UIImportData(ImportExportService service) {
  	setRendererType("ImportDataRenderer") ;
  	service_ = service ;
  }
  
  public String getFamily() {
  	return "org.exoplatform.portlets.backup.component.UIImportData" ;
  }
  
  public boolean hasAdminRole() { return adminRole_ ; }
  public void    setAdminRole(boolean b) { adminRole_ = b ; }
  
  ImportExportService getBackupService() { return service_ ; } 
  
  public String getDirectory() { return directory_ ; }
  public void   setDirectory(String s) { directory_ = s ; }
  
  public List getImportDatas() { return importDatas_ ; }
  
  protected void populateImportData() {
  	File dir = new File(directory_) ;
  	File[] jarFiles = dir.listFiles(new ZipFileFilter()) ;
  	importDatas_ = new ArrayList(jarFiles.length) ;
  	for(int i = 0; i < jarFiles.length; i++) {
  		importDatas_.add(new ImportData(jarFiles[i])) ;
  	}
  }
  
  static public class RefreshActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIImportData ui = (UIImportData) event.getComponent() ;
  		ui.populateImportData() ;
    }
  }
  
  static public class ViewDataActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIImportData ui = (UIImportData) event.getComponent() ;
  		UINode parent = (UINode) ui.getParent() ;
  		UIData uiData = (UIData)  parent.getChildComponentOfType(UIData.class) ;
  		String dataName = event.getParameter("dataName") ;
  		for(int i = 0 ; i < ui.importDatas_.size() ; i++) {
  			ImportData data = (ImportData) ui.importDatas_.get(i) ;
  			if(data.getName().equals(dataName)) {
  				uiData.setImportDataModel(data) ;
  				parent.setRenderedComponent(uiData.getId()) ;
  			}
  		}
    }
  }
}