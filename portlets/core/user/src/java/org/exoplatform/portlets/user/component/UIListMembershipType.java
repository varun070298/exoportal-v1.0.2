/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.user.component.UIOrganizationPortlet.UIMembershipNode;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIListMembershipType.java,v 1.9 2004/08/26 04:18:02 tuan08 Exp $
 */
public class UIListMembershipType extends UIGrid {
  private static SimpleDateFormat ft_ = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss") ;
  
  static Parameter[] EDIT_MEMBERSHIP = {new Parameter(ACTION , "editMembership") } ;
  static Parameter[] DELETE_MEMBERSHIP = {new Parameter(ACTION , "deleteMembership") } ;
  static Parameter[] ADD_MEMBERSHIP = {new Parameter(ACTION , "addMembership") };
  
  private List memberships_ ;
  private OrganizationService service_ ;
  private MembershipDataHandler dataHandler_ ;
  private boolean adminRole_ ;
   
  public UIListMembershipType(OrganizationService service) throws Exception {
    setId("UIListMembershipType") ;
    service_ = service ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
    dataHandler_ = new MembershipDataHandler() ;
    add(new Rows(dataHandler_, "even", "odd").
        add(new Column("#{UIListMembershipType.header.name}", "name")).        
        add(new Column("#{UIListMembershipType.header.owner}", "owner")).
        add(new Column("#{UIListMembershipType.header.created-date}", "createdDate")).
        add(new Column("#{UIListMembershipType.header.modified-date}", "modifiedDate")).
        add(new Column("#{UIListMembershipType.header.description}", "description")).
        add(new ActionColumn("#{UIListMembershipType.header.action}", "name").
            add(adminRole_ ,new Button("#{UIListMembershipType.button.edit}", EDIT_MEMBERSHIP)).
            add(adminRole_ ,new Button("#{UIListMembershipType.button.delete}", DELETE_MEMBERSHIP)).
            setCellClass("action-column"))) ;
    add(new Row().setClazz("footer").
        add(new ActionCell().
            add(adminRole_, new Button("#{UIListMembershipType.button.add}", ADD_MEMBERSHIP)).
            addColspan("6").addAlign("center")));
    
    addActionListener(AddMembershipActionListener.class, "addMembership") ;
    addActionListener(EditMembershipActionListener.class, "editMembership") ;
    addActionListener(DeleteMembershipActionListener.class, "deleteMembership") ;
    update() ;
  }
  
  public void update() throws Exception {
    memberships_ =(List)service_.findMembershipTypes();
    dataHandler_.setDatas(memberships_) ;
  }
  
  public Collection getMembershipTypes() { return memberships_ ; }
  
  static public class MembershipDataHandler extends ListDataHandler {
  	private MembershipType membership_  ;
  	
  	public String  getData(String fieldName)   {
  		if("name".equals(fieldName)) return membership_.getName() ;      
      if("owner".equals(fieldName)) return membership_.getOwner() ;
      if("createdDate".equals(fieldName)) return ft_.format(membership_.getCreatedDate()) ;
      if("modifiedDate".equals(fieldName)) return ft_.format(membership_.getModifiedDate());
      if("description".equals(fieldName)) return membership_.getDescription() ;
  		return null ;
  	}
  	
  	public void setCurrentObject(Object o) { membership_ = (MembershipType) o; }
  }
  
  static public class AddMembershipActionListener extends ExoActionListener  {
    public AddMembershipActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIListMembershipType uiList = (UIListMembershipType) event.getComponent() ;
  		UIMembershipNode uiController = (UIMembershipNode) uiList.getParent() ;
      UIMembershipTypeForm uiForm =  
      	(UIMembershipTypeForm)uiController.getChildComponentOfType(UIMembershipTypeForm.class) ;
      uiForm.addMembershipType() ;
      uiController.setRenderedComponent(uiForm.getId()) ;
    }
  }
  
  static public class EditMembershipActionListener extends ExoActionListener  {
    public EditMembershipActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIListMembershipType uiList = (UIListMembershipType) event.getComponent() ;
  		String name = event.getParameter(OBJECTID) ;
      UIMembershipNode uiController = (UIMembershipNode) uiList.getParent() ;
      UIMembershipTypeForm uiForm = 
      	(UIMembershipTypeForm)uiController.getChildComponentOfType(UIMembershipTypeForm.class) ;
      uiForm.setMembershipType(name) ;
      uiController.setRenderedComponent(uiForm.getId()) ;
    }
  }
  
  static public class DeleteMembershipActionListener extends ExoActionListener  {
    public DeleteMembershipActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIListMembershipType uiList = (UIListMembershipType) event.getComponent() ;
  		String name = event.getParameter(OBJECTID) ;
      uiList.service_.removeMembershipType(name) ;
      uiList.update() ;
    }
  }
}