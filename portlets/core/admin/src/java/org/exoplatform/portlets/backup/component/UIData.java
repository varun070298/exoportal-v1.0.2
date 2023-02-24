/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.backup.component;

import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.faces.core.event.*;
import org.exoplatform.portlets.backup.component.model.ImportData;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIData.java,v 1.2 2004/09/21 00:16:11 tuan08 Exp $
 */
public class UIData extends UIExoCommand {
  private ImportData data_  ;
  private Class importClass_ ;

  public UIData() {
  	setId("UIData") ;
		setRendererType("DataRenderer") ;
    addActionListener(BackActionListener.class, "cancel") ;
  }
  
  public void setImportClass(Class clazz ) { importClass_ = clazz ; }
  
  public String getFamily() {
  	return "org.exoplatform.portlets.backup.component.UIData" ;
  }
  
  public ImportData getImportDataModel() { return data_ ; }
  public void setImportDataModel(ImportData data) { data_ = data ;}
  
  static public class BackActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIData uiData = (UIData) event.getSource();
  		UINode parent = (UINode) uiData.getParent() ;
  		parent.setRenderedComponent(uiData.importClass_) ;
    }
  }
}	