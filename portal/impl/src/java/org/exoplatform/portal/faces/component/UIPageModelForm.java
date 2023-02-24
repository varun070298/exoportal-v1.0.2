/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.ArrayList;
import java.util.List;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIRadioBox;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.core.validator.EmptyFieldValidator;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.model.Page;


/**
 * Sat, Jan 03, 2004 @ 11:16
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: UIPageModelForm.java,v 1.5 2004/09/29 17:44:51 benjmestrallet Exp $
 */
public class UIPageModelForm extends UISimpleForm {
  private UIStringInput nameInput_ ;
  private UIRadioBox templateInput_ ;
  private PortalConfigService service_  ;
  
  public UIPageModelForm(PortalConfigService service) {
    super("pageModelForm", "post", null) ;
    service_ = service ;
    nameInput_ = new UIStringInput("name", "").
    addValidator(EmptyFieldValidator.class) ;
    List pageTemplates = getTemplates("template", service) ;
    templateInput_  = new UIRadioBox("template", null, pageTemplates) ;
    add(new HeaderRow().
        add(new Cell("#{UIPageModelForm.header.new-page}").     
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIPageModelForm.label.name}")).      
        add(new ComponentCell(this, nameInput_)));
    add(new Row().
        add(new LabelCell("#{UIPageModelForm.label.select-template}")).
        add(new ComponentCell(this, templateInput_)));
    add(new Row().add(new ListComponentCell().
        add(new FormButton("#{UIPageModelForm.link.save}", SAVE_ACTION)).
        add(new FormButton("#{UIPageModelForm.link.cancel}", CANCEL_ACTION)).
        addColspan("2").addAlign("center"))) ;
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  }
  
  
  
  static public class SaveActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPageModelForm uiForm = (UIPageModelForm) event.getSource();
      UIPortal uiPortal = (UIPortal) uiForm.getAncestorOfType(UIPortal.class);
      String pageName = uiForm.nameInput_.getValue();
      String pageId = uiPortal.getOwner() + ":" + uiForm.nameInput_.getValue();
      if (uiForm.service_.getPage(pageId) != null) {
        InformationProvider iprovider = findInformationProvider(uiForm);
        iprovider.addMessage(new ExoFacesMessage("#{UIPageModelForm.msg.reference-page-exist}"));
      }
      Page page = 
        uiForm.service_.getPredefinedTemplate("template", uiForm.templateInput_.getValue());
      page.setOwner(uiPortal.getOwner());
      page.setName(pageName);
      uiForm.service_.savePage(page);
      UIPageList uiPageList = (UIPageList) uiForm.getSibling(UIPageList.class);
      uiPageList.update();
      uiForm.setRenderedSibling(UIPageList.class);
    }
  }

  static public class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPageModelForm uiForm = (UIPageModelForm) event.getSource();
      uiForm.setRenderedSibling(UIPageList.class);
    }
  }

  static private List getTemplates(String user, PortalConfigService service) {
    List pageTemplates = service.getPredefinedTemplates(user);
    List templates = new ArrayList(pageTemplates.size());
    String label = null;
    for (int i = 0; i < pageTemplates.size(); i++) {
      Page page = (Page) pageTemplates.get(i);
      String icon = page.getIcon();
      if (icon != null) {
        StringBuffer b = new StringBuffer();
        b.append("<img src='" + icon + "' title='" + page.getTitle() + "'/>");
        label = b.toString();
      } else {
        label = page.getTitle();
      }
      templates.add(new SelectItem(label, page.getName()));
    }
    return templates;
  }
  
}
