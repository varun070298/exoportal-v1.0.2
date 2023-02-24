/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component;

import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.content.ContentUtil;
import org.exoplatform.portlets.content.display.component.model.ContentConfig;


/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentConfigForm.java,v 1.4 2004/08/17 13:07:02 tuan08 Exp $
 */
public class UIContentConfigForm extends UISimpleForm {
  
  private UIStringInput nameInput_ ;
  private UIStringInput titleInput_ ;
  private UIStringInput uriInput_ ;
  private UIStringInput encodingInput_ ;
  private ContentConfig config_ ;
  
  public UIContentConfigForm() {
    super("configForm", "post", null) ;
    setId("UIContentConfigForm") ;
    setClazz("UIContentConfigForm") ;
    nameInput_ = new UIStringInput("name", "") ;
    titleInput_ = new UIStringInput("title", "") ;
    uriInput_ = new UIStringInput("uri", "") ;
    encodingInput_ = new UIStringInput("encoding", "") ;
    
    add(new HeaderRow().
        add(new Cell("#{UIContentConfigForm.header.content-config-form}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIContentConfigForm.label.name}")).
        add(new ComponentCell(this, nameInput_)));
    add(new Row().
        add(new LabelCell("#{UIContentConfigForm.label.title}")).
        add(new ComponentCell(this, titleInput_)));
    add(new Row().
        add(new LabelCell("#{UIContentConfigForm.label.uri}")).
        add(new ComponentCell(this, uriInput_)));
    add(new Row().
        add(new LabelCell("#{UIContentConfigForm.label.encoding}")).
        add(new ComponentCell(this, encodingInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UIContentConfigForm.button.save}", SAVE_ACTION)).
            add(new FormButton("#{UIContentConfigForm.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  }
  
  public void setContentConfig(ContentConfig config) {
    config_ = config ;
    if (config == null) {
      nameInput_.setText("") ;
      nameInput_.setEditable(true) ;
      titleInput_.setText("") ;
      uriInput_.setText("") ;
      encodingInput_.setText("") ;
    } else {
      nameInput_.setText(config.getName()) ;
      nameInput_.setEditable(false) ;
      titleInput_.setText(config.getTitle()) ;
      uriInput_.setText(config.getUri()) ;
      encodingInput_.setText(config.getEncoding()) ;
    }
  }
  
  
  static public class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIContentConfigForm uiForm = (UIContentConfigForm) event.getComponent() ;
      uiForm.setRenderedSibling(UIContentConfig.class) ;
    }
  }
  
  static public class SaveActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIContentConfigForm uiForm = (UIContentConfigForm) event.getComponent() ;
      if(uiForm.config_ == null) {
        uiForm.config_ = new ContentConfig() ;
      }
      String name = uiForm.nameInput_.getValue() ;
      if(name == null || name.length() == 0) {
        InformationProvider iprovider = findInformationProvider(uiForm) ;
        iprovider.addMessage(new ExoFacesMessage("#{UIContentConfigForm.msg.name-is-null}")) ;
        return ;
      }
      uiForm.config_.setName(name);
      uiForm.config_.setTitle( uiForm.titleInput_.getValue());
      uiForm.config_.setUri( uiForm.uriInput_.getValue());
      String encoding = uiForm.encodingInput_.getValue();
      if(encoding == null || "".equals(encoding)){
        encoding = "UTF-8";
      }
      uiForm.config_.setEncoding(encoding);
      UIContentConfig uiConfig = (UIContentConfig)uiForm.getSibling(UIContentConfig.class) ;
      uiConfig.saveContentConfig(ContentUtil.EDIT_STATE, uiForm.config_) ;
      uiForm.setRenderedSibling(UIContentConfig.class) ;
    }
  }
}