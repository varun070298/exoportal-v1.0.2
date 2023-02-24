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
import org.exoplatform.services.portal.model.Container;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Decorator;
import org.exoplatform.services.portal.skin.model.Style;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContainerForm.java,v 1.11 2004/11/03 01:19:45 tuan08 Exp $
 */
public class UIContainerForm extends UISimpleForm {
  
  private UIContainer uiEditingContainer_ ; 
  private UIBasicComponent uiParent_ ; 
  private SkinConfigService configService_ ;

  private UIStringInput idInput_ ;
  private UIStringInput titleInput_ ;
  private UISelectBox styleInput_ ;
  private UISelectBox rendererInput_ ;
  private UIStringInput widthInput_ ;
  private UIStringInput heightInput_ ;
  private List styleOptions_ ;
  private List rendererOptions_ ;

  public UIContainerForm(SkinConfigService configService)  {
    super("containerForm", "post", null) ;
    setId("UIContainerForm");
    configService_ = configService ;
    idInput_ = new UIStringInput("id", "") ;
    idInput_.setEditable(false) ;
    titleInput_ = new UIStringInput("title", "") ;
    styleInput_ = new UISelectBox("style", "", null) ;
    rendererInput_ = new UISelectBox("renderer","" , null) ;
    widthInput_ = new UIStringInput("width", "") ;
    heightInput_ = new UIStringInput("height", "") ;
    String saveButton = "#{UIContainerForm.link.save}";
    String cancelButton = "#{UIContainerForm.link.cancel}";
    rendererOptions_ = new ArrayList() ;
    styleOptions_ = new ArrayList() ;
    Iterator i = configService_.getContainerDecorators().iterator() ; 
    while(i.hasNext()) {
      Decorator decorator = (Decorator) i.next() ;
      String rendererType = decorator.getRendererType() ;
      rendererOptions_.add(new SelectItem(rendererType, rendererType)) ;
    }

    add(new HeaderRow().
        add(new Cell("#{UIContainerForm.header.add-edit-container}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIContainerForm.label.container-id}")).
        add(new ComponentCell(this, idInput_)));
    add((new Row()).
        add(new LabelCell("#{UIContainerForm.label.container-title}")).
            add(new ComponentCell(this, titleInput_)));
    add(new Row().
        add(new LabelCell("#{UIContainerForm.label.renderer}")).
        add(new ComponentCell(this, rendererInput_)));
    add(new Row().
        add(new LabelCell("#{UIContainerForm.label.style}")).
        add(new ComponentCell(this, styleInput_)));
    add(new Row().
        add(new LabelCell("#{UIContainerForm.label.width}")).
        add(new ComponentCell(this, widthInput_)));
    add(new Row().
        add(new LabelCell("#{UIContainerForm.label.height}")).
        add(new ComponentCell(this, heightInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton(saveButton, SAVE_ACTION)).
            add(new FormButton(cancelButton, CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    addActionListener(ShowLastBodyComponentActionListener.class, CANCEL_ACTION) ;
  }
  
  public void  setEditingContainer(UIContainer uiContainer) {
    uiEditingContainer_ = uiContainer ;
    styleOptions_.clear() ; 
    Container model = uiContainer.getContainerModel() ;
    String currentRenderer = uiContainer.getRendererType() ;

    String currentStyle = uiContainer.getDecorator() ;
    if(currentStyle == null || currentStyle.length() == 0) {
      currentStyle = "default" ;
    }

    Decorator decorator =  configService_.getContainerDecorator(currentRenderer) ; 
    List styles = decorator.getStyles() ;
    for (int i = 0; i < styles.size(); i++) {
      Style style = (Style) styles.get(i) ;
      String name = style.getName() ;
      styleOptions_.add(new SelectItem(name, name)) ;
    }
    
    idInput_.setValue(uiContainer.getId());
    rendererInput_.setOptions(rendererOptions_);
    rendererInput_.setValue(currentRenderer);
    styleInput_.setOptions(styleOptions_);
    styleInput_.setValue(currentStyle);
    widthInput_.setValue(model.getWidth()) ;
    heightInput_.setValue(model.getHeight()) ;
  }
  
  public void  addNewContainer(UIBasicComponent uiParent) {
    uiEditingContainer_ = null ;
    uiParent_ = uiParent ;
    styleOptions_.clear() ;
    Decorator decorator = configService_.getContainerDecorator("default") ; 
    List styles = decorator.getStyles() ;
    for (int i = 0; i < styles.size(); i++) {
      Style style = (Style) styles.get(i) ;
      String name = style.getName() ;
      styleOptions_.add(new SelectItem(name, name)) ;
    }
    
    idInput_.setValue("");
    rendererInput_.setOptions(rendererOptions_);
    rendererInput_.setValue("default");
    styleInput_.setOptions(styleOptions_);
    styleInput_.setValue("default");
    widthInput_.setValue("") ;
    heightInput_.setValue("") ;
  }
  
  static public class SaveActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
      UIContainerForm uiForm = (UIContainerForm) event.getSource() ;
			UIPortal uiPortal = (UIPortal) uiForm.getAncestorOfType(UIPortal.class) ;
		  if(uiForm.uiEditingContainer_ == null) {	
        List children = uiForm.uiParent_.getChildren() ;
        uiForm.uiEditingContainer_ = new UIContainer(new Container()) ;
        children.add(uiForm.uiEditingContainer_) ;
      }
		  Container model = uiForm.uiEditingContainer_.getEditableContainerModel() ;
      model.setDecorator(uiForm.styleInput_.getValue()) ;
      model.setRenderer(uiForm.rendererInput_.getValue()) ;
      model.setWidth(uiForm.widthInput_.getValue()) ;
      model.setHeight(uiForm.heightInput_.getValue()) ;
      model.setId(uiForm.idInput_.getValue()) ;
      model.setTitle(uiForm.titleInput_.getValue()) ;
      uiForm.uiEditingContainer_.updateChange() ;
      uiForm.uiEditingContainer_.setId(uiForm.idInput_.getValue()) ;
      uiForm.uiEditingContainer_.setComponentModified(true) ;
      uiPortal.setLastBodyComponent() ;
		}
	}
}