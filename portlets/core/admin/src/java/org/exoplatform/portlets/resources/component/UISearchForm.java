/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.resources.component;

import java.util.* ;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.resources.LocaleConfigService;
import org.exoplatform.services.resources.LocaleConfig ;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen 
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UISearchForm.java,v 1.4 2004/06/24 13:35:32 tuan08 Exp $
 */
public class UISearchForm extends UISimpleForm {
  
  private UIStringInput nameInput_ ;
  private UISelectBox   languageInput_ ;

  public UISearchForm(boolean adminRole) {
    super("searchForm", "post", null) ;
    setId("UISearchForm");
    LocaleConfigService service =
      (LocaleConfigService) PortalContainer.getComponent(LocaleConfigService.class);
    Iterator i = service.getLocalConfigs().iterator() ;
    List options = new ArrayList() ;
    options.add(new SelectItem("All", ""))  ;
    while (i.hasNext()) {
      LocaleConfig config = (LocaleConfig) i.next() ;
      options.add(new SelectItem(config.getLocaleName(), config.getLanguage()))  ;
    }
    nameInput_ = new UIStringInput("name", "") ;
    languageInput_ = new UISelectBox("language","" , options) ;    
    add(new Row().
        add(new LabelCell("#{UISearchForm.label.name}")).
        add(new ComponentCell(this, nameInput_))) ;
    add(new Row().
        add(new LabelCell("#{UISearchForm.label.language}")).
        add(new ComponentCell(this, languageInput_))) ;
    ListComponentCell buttonCell = new ListComponentCell() ;
    buttonCell.addColspan("2").addAlign("center") ;
  	buttonCell.add(new FormButton("#{UISearchForm.button.search}", "searchResource")) ;
    if(adminRole) {
    	buttonCell.add(new FormButton("#{UISearchForm.button.new-resource}", "newResource")) ;
    }
    add(new Row().add(buttonCell)) ;
    addActionListener(SearchActionListener.class, "searchResource") ;
    addActionListener(NewResourceActionListener.class, "newResource") ;
  }
 
  static public class SearchActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
  		UISearchForm uiForm = (UISearchForm) event.getComponent() ; 
  		UIListResources uiListResources = (UIListResources) uiForm.getParent() ;
    	String language = uiForm.languageInput_.getValue();
    	if ("all".equals(language)) language = null ;
    	uiListResources.update(uiForm.nameInput_.getValue(), language);
    }
  }
  
  static public class NewResourceActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
  		UISearchForm uiForm = (UISearchForm) event.getComponent() ; 
  		UIResourcesPortlet uiPortlet = (UIResourcesPortlet) uiForm.getParent().getParent() ;
    	UIResource uiResource = 
    		(UIResource)uiPortlet.getChildComponentOfType(UIResource.class) ;
    	uiResource.setResourceBundleData(null) ;
      uiResource.setMode(UIResource.EDIT_MODE) ;
    	uiPortlet.setRenderedComponent(uiResource.getId()) ;
    }
  }
}