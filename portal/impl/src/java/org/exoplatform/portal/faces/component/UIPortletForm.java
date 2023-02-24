/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.*;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.listener.share.ShowLastBodyComponentActionListener;
import org.exoplatform.services.portal.model.Portlet;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Decorator;
import org.exoplatform.services.portal.skin.model.Style;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPortletForm.java,v 1.10 2004/09/30 01:00:05 tuan08 Exp $
 */
public class UIPortletForm extends UISimpleForm {
  final static String UPDATE_RENDERER_ACTION = "updateRenderer";
  
  private UIPortlet uiEditingPortlet_ ; 
  private SkinConfigService skinService_ ;
  
  private UIStringInput idInput_ ;
  private UIStringInput titleInput_ ;
  private UISelectBox portletStyleInput_ ;
  private UISelectBox decoratorInput_ ;
  private UISelectBox rendererInput_ ;
  private UIStringInput widthInput_ ;
  private UIStringInput heightInput_ ;
  private UICheckBox showInfoBarInput_ ;
  private UICheckBox showWindowStateInput_ ;
  private UICheckBox showPortletModeInput_ ;
  private List decoratorOptions_ ;
  private List portletStyleOptions_ ;
  private List rendererOptions_ ;
  
  public UIPortletForm(SkinConfigService configService) {
    super("portletForm", "post", null) ;
    setId("UIPortletForm");
    skinService_ = configService ;
    idInput_ = new UIStringInput("id", "") ;
    idInput_.setEditable(false) ;
    titleInput_ = new UIStringInput("title", "") ;    
    portletStyleInput_ = new UISelectBox("style", "", null) ;
    decoratorInput_ = new UISelectBox("decorator", "", null) ;
    rendererInput_ = new UISelectBox("renderer","" , null) ;
    rendererInput_.setUpdateOnChangeAction(UPDATE_RENDERER_ACTION) ;
    heightInput_ = new UIStringInput("height", "") ;
    widthInput_ = new UIStringInput("width", "") ;
    showInfoBarInput_ = new UICheckBox("showInfoBar", "true") ;
    showWindowStateInput_ = new UICheckBox("showWindowState", "true") ;
    showPortletModeInput_ = new UICheckBox("showPortletMode", "true") ;
    String saveButton =  "#{UIPortletForm.link.save}" ;
    String cancelButton =  "#{UIPortletForm.link.cancel}" ;
    rendererOptions_ = new ArrayList(5) ;
    portletStyleOptions_ = new ArrayList(5) ;
    decoratorOptions_ = new ArrayList(5) ;

    add(new HeaderRow().
        add(new Cell("#{UIPortletForm.header.edit-portlet}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.portlet-id}")).
        add(new ComponentCell(this, idInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.portlet-title}")).
        add(new ComponentCell(this, titleInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.renderer}")).
        add(new ComponentCell(this, rendererInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.style}")).
        add(new ComponentCell(this, portletStyleInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.decorator}")).
        add(new ComponentCell(this, decoratorInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.width}")).
        add(new ComponentCell(this, widthInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.height}")).
        add(new ComponentCell(this, heightInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.show-info-bar}")).
        add(new ComponentCell(this, showInfoBarInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.show-portlet-mode}")).
        add(new ComponentCell(this, showPortletModeInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.show-window-state}")).
        add(new ComponentCell(this, showWindowStateInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton(saveButton, SAVE_ACTION)).
            add(new FormButton(cancelButton, CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    
    addActionListener(ShowLastBodyComponentActionListener.class, CANCEL_ACTION) ;
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    addActionListener(UpdateRendererActionListener.class, UPDATE_RENDERER_ACTION) ;
  }
  
  public void setEditingPortlet(UIPortlet uiPortlet) {
    uiEditingPortlet_ = uiPortlet ; 
    rendererOptions_.clear() ;
    portletStyleOptions_.clear() ;
    decoratorOptions_.clear() ;
    ExoWindowID windowID = uiEditingPortlet_.getWindowId() ;
    String portletId = windowID.getPortletApplicationName() + "/" + windowID.getPortletName()  ;
    Portlet model = uiPortlet.getPortletModel() ;
    Iterator decoratorIterator = skinService_.getPortletDecorators().iterator() ;
    String currentRenderer = uiPortlet.getRendererType() ;
    Decorator selectDecorator = null ;
    while(decoratorIterator.hasNext()) {
      Decorator decorator = (Decorator) decoratorIterator.next() ;
      String rendererType = decorator.getRendererType() ;
      rendererOptions_.add(new SelectItem(rendererType, rendererType)) ;
      if (selectDecorator == null)  {
        selectDecorator = decorator ;
      }else if(currentRenderer.equals(rendererType)) {
        selectDecorator = decorator ;
      }
    }

    List styles = selectDecorator.getStyles() ;
    for (int i = 0; i < styles.size(); i++) {
      Style style = (Style) styles.get(i) ;
      String name = style.getName() ;
      decoratorOptions_.add(new SelectItem(name, name)) ;
    }
    
    List portletStyles = skinService_.getPortletStyles(portletId) ;
    if(portletStyles == null)  {
      portletStyles = skinService_.getPortletStyles("default") ;
    }
    for (int i = 0; i < portletStyles.size(); i++) {
      Style style = (Style) portletStyles.get(i) ;
      String name = style.getName() ;
      portletStyleOptions_.add(new SelectItem(name, name)) ;
    }

    idInput_.setValue(uiPortlet.getId()) ;
    titleInput_.setValue(model.getTitle()) ;
    rendererInput_.setOptions(rendererOptions_);
    rendererInput_.setValue(currentRenderer);
    decoratorInput_.setOptions(decoratorOptions_);
    decoratorInput_.setValue(model.getDecorator());
    portletStyleInput_.setOptions(portletStyleOptions_);
    portletStyleInput_.setValue(model.getPortletStyle());
    widthInput_.setValue(model.getWidth()) ;
    heightInput_.setValue(model.getHeight()) ;
    showInfoBarInput_.setSelect(model.getShowInfoBar()) ;
    showWindowStateInput_.setSelect(model.getShowWindowState()) ;
    showPortletModeInput_.setSelect(model.getShowPortletMode()) ;
  }
  
  static public class UpdateRendererActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPortletForm uiForm = (UIPortletForm) event.getSource() ;
      String selectRenderer = uiForm.rendererInput_.getValue() ;
      Decorator selectDecorator =  uiForm.skinService_.getPortletDecorator(selectRenderer) ; 
      uiForm.decoratorOptions_.clear() ;
      List styles = selectDecorator.getStyles() ;
      String decoratorDefaultStyle = "default" ;
      for (int i = 0; i < styles.size(); i++) {
        Style style = (Style) styles.get(i) ;
        String name = style.getName() ;
        uiForm.decoratorOptions_.add(new SelectItem(name, name)) ;
        if(i == 0) decoratorDefaultStyle = name ;
      }
      uiForm.rendererInput_.setValue(selectRenderer);
      uiForm.decoratorInput_.setOptions(uiForm.decoratorOptions_);
      uiForm.decoratorInput_.setValue(decoratorDefaultStyle);
    }
  }
  
  static public class SaveActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPortletForm uiForm = (UIPortletForm) event.getSource() ;
      UIPortal uiPortal = (UIPortal) uiForm.getAncestorOfType(UIPortal.class);
      Portlet model = uiForm.uiEditingPortlet_.getEditablePortletModel() ;
      String portletTitle = uiForm.titleInput_.getValue() ;
      if(portletTitle == null || portletTitle.length() == 0) portletTitle  = null ;
      model.setTitle(portletTitle) ;
      model.setDecorator(uiForm.decoratorInput_.getValue()) ;
      model.setPortletStyle(uiForm.portletStyleInput_.getValue()) ;
      model.setRenderer(uiForm.rendererInput_.getValue()) ;
      model.setWidth(uiForm.widthInput_.getValue()) ;
      model.setHeight(uiForm.heightInput_.getValue()) ;
      model.setShowInfoBar(uiForm.showInfoBarInput_.getSelect()) ;
      model.setShowWindowState(uiForm.showWindowStateInput_.getSelect()) ;
      model.setShowPortletMode(uiForm.showPortletModeInput_.getSelect()) ;
      String id = uiForm.idInput_.getValue()  ;
      ExoWindowID windowId = uiForm.uiEditingPortlet_.getWindowId() ;
      if(id == null || id.length() == 0) id =  windowId.getPortletName() ;
      windowId.setUniqueID(id) ;
      model.setWindowId(windowId.generatePersistenceId()) ;
      uiForm.uiEditingPortlet_.setId(id);  
      uiForm.uiEditingPortlet_.setDisplayTitle(portletTitle);  
      uiForm.uiEditingPortlet_.updateChange() ;
      uiForm.uiEditingPortlet_.setComponentModified(true) ;
      uiPortal.setLastBodyComponent() ;
    }
  }
}