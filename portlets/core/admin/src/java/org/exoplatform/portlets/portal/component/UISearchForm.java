/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.portal.component;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.faces.core.component.UISelectBox;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.FormButton;
import org.exoplatform.faces.core.component.model.LabelCell;
import org.exoplatform.faces.core.component.model.ListComponentCell;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.faces.core.component.model.SelectItem;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.portal.Query;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UISearchForm.java,v 1.4 2004/09/29 17:44:51 benjmestrallet Exp $
 */
public class UISearchForm extends UISimpleForm {
  private static List PERMISSIONS ;
  static {
    PERMISSIONS = new ArrayList(10) ;
    PERMISSIONS.add(new SelectItem("All", ""))  ;
    PERMISSIONS.add(new SelectItem("owner", "owner"))  ;
    PERMISSIONS.add(new SelectItem("any", "any"))  ;
  }
  
  private UIStringInput ownerInput_ ;
  private UISelectBox   viewPermissionInput_ ;
  private UISelectBox   editPermissionInput_ ;

  public UISearchForm() {
    super("searchForm", "post", null) ;
    setId("UISearchForm");    
    ownerInput_ = new UIStringInput("owner", "") ;
    viewPermissionInput_ = new UISelectBox("viewPermission","" , PERMISSIONS) ;
    editPermissionInput_ = new UISelectBox("editPermission","" , PERMISSIONS) ;
    add(new Row().
        add(new LabelCell("#{UISearchForm.label.owner}")).
        add(new ComponentCell(this, ownerInput_))) ;
    add(new Row().
        add(new LabelCell("#{UISearchForm.label.view-permission}")).
        add(new ComponentCell(this, viewPermissionInput_))) ;
    add(new Row().
        add(new LabelCell("#{UISearchForm.label.edit-permission}")).
        add(new ComponentCell(this, editPermissionInput_))) ;
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UISearchForm.button.search}", "search")).
            addColspan("2").addAlign("center"))) ;
    
    addActionListener(SearchPagesActionListener.class, "search") ;
  }
  
  static public class SearchPagesActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception { 
		  UISearchForm uiForm = (UISearchForm) event.getSource() ;
      UIAvailablePortal uiTarget = 
        (UIAvailablePortal) uiForm.getParent() ;
      Query q = new Query(uiForm.ownerInput_.getValue(),
                          uiForm.viewPermissionInput_.getValue(),
                          uiForm.editPermissionInput_.getValue());
      uiTarget.refresh(q) ;
		}	
  }
}