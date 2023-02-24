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
import org.exoplatform.services.portal.model.Page;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Decorator;
import org.exoplatform.services.portal.skin.model.Style;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPageForm.java,v 1.8 2004/09/29 17:44:51 benjmestrallet Exp $
 */
public class UIPageForm extends UISimpleForm {
  static private List PERMISSIONS = new ArrayList() ;
  static {
    PERMISSIONS.add(new SelectItem("owner", "owner"));
    PERMISSIONS.add(new SelectItem("any", "any"));
  }
  
  private UIPage uiEditingPage_;
  private SkinConfigService skinService_;
  private UIStringInput titleInput_;
  private UIStringInput iconInput_;
  private UISelectBox viewPermissionInput_;
  private UISelectBox editPermissionInput_;
  private UISelectBox decoratorInput_;
  private UISelectBox rendererInput_;
  private UIStringInput widthInput_;
  private UIStringInput heightInput_;
  private UIStringInput stateInput_;
  private List styleOptions_;
  private List rendererOptions_;

  public UIPageForm(SkinConfigService skinService) {
    super("pageForm", "post", null);
    setId("UIPageForm");
    skinService_ = skinService ;
    titleInput_ = new UIStringInput("title", "");
    iconInput_ = new UIStringInput("icon", "");
    viewPermissionInput_ = new UISelectBox("viewPermission", "", PERMISSIONS);
    editPermissionInput_ = new UISelectBox("editPermission", "", PERMISSIONS);
    decoratorInput_ = new UISelectBox("decorator", "", null);
    rendererInput_ = new UISelectBox("renderer", "", null);
    rendererInput_.setUpdateOnChangeAction("updateStyles");
    widthInput_ = new UIStringInput("width", "");
    heightInput_ = new UIStringInput("height", "");
    stateInput_ = new UIStringInput("state", "");
    
    String saveButton = "#{UIPageForm.link.save}";
    String cancelButton = "#{UIPageForm.link.cancel}";
    rendererOptions_ = new ArrayList();
    styleOptions_ = new ArrayList();
    Iterator i = skinService_.getPageDecorators().iterator() ;
    while(i.hasNext()) {
      Decorator decorator = (Decorator) i.next() ;
      String rendererType = decorator.getRendererType();
      rendererOptions_.add(new SelectItem(rendererType, rendererType));
    }

    add((new HeaderRow()).
         add((new Cell("#{UIPageForm.header.add-edit-page}")).
              addColspan("2")));
    add((new Row()).
         add(new LabelCell("#{UIPageForm.label.page-title}")).
             add(new ComponentCell(this, titleInput_)));
    add((new Row()).
         add(new LabelCell("#{UIPageForm.label.icon}")).
             add(new ComponentCell(this, iconInput_)));
    add((new Row()).
         add(new LabelCell("#{UIPageForm.label.view-permission}")).
             add(new ComponentCell(this, viewPermissionInput_)));
    add((new Row()).
         add(new LabelCell("#{UIPageForm.label.edit-permission}")).
             add(new ComponentCell(this, editPermissionInput_)));
    add((new Row()).
        add(new LabelCell("#{UIPageForm.label.renderer}")).
            add(new ComponentCell(this, rendererInput_)));
    add((new Row()).
         add(new LabelCell("#{UIPageForm.label.decorator}")).
             add(new ComponentCell(this, decoratorInput_)));
    add((new Row()).
         add(new LabelCell("#{UIPageForm.label.width}")).
             add(new ComponentCell(this, widthInput_)));
    add((new Row()).
         add(new LabelCell("#{UIPageForm.label.height}")).
             add(new ComponentCell(this, heightInput_)));
    add((new Row()).
         add(new LabelCell("#{UIPageForm.label.page-state}")).
             add(new ComponentCell(this, stateInput_)));
    add((new Row()).
         add((new ListComponentCell()).
              add(new FormButton(saveButton, SAVE_ACTION)).
              add(new FormButton(cancelButton, CANCEL_ACTION)).
              addColspan("2").addAlign("center")));
    
    addActionListener(ShowLastBodyComponentActionListener.class, CANCEL_ACTION) ;
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
  }

  public void setEditingPage(UIPage uiPage) {
    uiEditingPage_ = uiPage;
    styleOptions_.clear();
    Page model  = uiPage.getPageModel() ;
    String currentRenderer = uiPage.getRendererType();
    String currentDecorator = uiPage.getDecorator();
    if(currentDecorator == null || currentDecorator.length() == 0)
      currentDecorator = "default";
    Decorator decorator = skinService_.getPageDecorator(currentRenderer);
    List styles = decorator.getStyles();
    for(int i = 0; i < styles.size(); i++) {
      Style style = (Style)styles.get(i);
      String name = style.getName();
      styleOptions_.add(new SelectItem(name, name));
    }

    titleInput_.setValue(model.getTitle());
    iconInput_.setValue(model.getIcon());
    editPermissionInput_.setValue(uiPage.getEditPermission());
    viewPermissionInput_.setValue(uiPage.getViewPermission());
    rendererInput_.setOptions(rendererOptions_);
    rendererInput_.setValue(currentRenderer);
    decoratorInput_.setOptions(styleOptions_);
    decoratorInput_.setValue(currentDecorator);
    widthInput_.setValue(model.getWidth());
    heightInput_.setValue(model.getHeight());
    stateInput_.setValue(model.getState());
  }

  public void addNewPage() {
    titleInput_.setValue("");
    iconInput_.setValue("");
    rendererInput_.setOptions(rendererOptions_);
    rendererInput_.setValue("default");
    decoratorInput_.setOptions(styleOptions_);
    decoratorInput_.setValue("default");
    editPermissionInput_.setValue("owner");
    viewPermissionInput_.setValue("any");
    widthInput_.setValue("");
    heightInput_.setValue("");
    stateInput_.setValue("");
  }
  
  static public class SaveActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPageForm uiForm = (UIPageForm) event.getComponent();
      UIPortal uiPortal = (UIPortal) uiForm.getAncestorOfType(UIPortal.class) ;
      Page model = uiForm.uiEditingPage_.getEditablePageModel() ;
      model.setTitle(uiForm.titleInput_.getValue());
      model.setIcon(uiForm.iconInput_.getValue());
      model.setViewPermission(uiForm.viewPermissionInput_.getValue());
      model.setEditPermission(uiForm.editPermissionInput_.getValue());
      model.setDecorator(uiForm.decoratorInput_.getValue());
      model.setRenderer(uiForm.rendererInput_.getValue());
      model.setWidth(uiForm.widthInput_.getValue());
      model.setHeight(uiForm.heightInput_.getValue());
      model.setState(uiForm.stateInput_.getValue());
      uiForm.uiEditingPage_.updateChange() ;
      uiForm.uiEditingPage_.setComponentModified(true) ;
      uiPortal.setLastBodyComponent() ;
    }
  }
}