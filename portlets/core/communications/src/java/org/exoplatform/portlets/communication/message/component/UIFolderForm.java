/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.message.Account;
import org.exoplatform.services.communication.message.Folder;
import org.exoplatform.services.communication.message.MessageService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIFolderForm.java,v 1.2 2004/09/10 03:56:17 tuan08 Exp $
 */
public class UIFolderForm extends  UISimpleForm {

  private MessageService service_ ;
  private Account account_ ;
  private Folder folder_ ;
  
  private UIStringInput nameInput_;
  private UIStringInput labelInput_;
  
  public UIFolderForm(MessageService service) throws Exception {
    super("folderForm", "post", null) ;
  	setId("UIFolderForm") ;
    setClazz("UIFolderForm");
    service_ = service ;
    
    nameInput_ = new UIStringInput("name", "") ;
    labelInput_ = new UIStringInput("label", "") ;
    
    add(new HeaderRow().
        add(new Cell("#{UIFolderForm.header.add-edit-folder}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIFolderForm.label.folder-name}")).
        add(new ComponentCell(this, nameInput_)));
    add(new Row().
        add(new LabelCell("#{UIFolderForm.label.folder-label}")).
        add(new ComponentCell(this, labelInput_)));
   
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UIFolderForm.button.delete}", DELETE_ACTION)).
            add(new FormButton("#{UIFolderForm.button.save}", SAVE_ACTION)).
            add(new FormButton("#{UIFolderForm.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
    addActionListener(DeleteActionListener.class, DELETE_ACTION) ;
  }
  
  public void setFormData(Account account , Folder folder) {
    account_ = account ;
    folder_ = folder ;
    if(folder == null) {
      nameInput_.setValue("") ;
      nameInput_.setEditable(true) ;
      labelInput_.setValue("") ;
    } else {
      nameInput_.setValue(folder.getName()) ;
      nameInput_.setEditable(false) ;
      labelInput_.setValue(folder.getLabel()) ;
    }
  }
  
  static public class SaveActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIFolderForm uiForm =  (UIFolderForm) event.getSource();
      UIAccountConfiguration uiAccConfig = 
        (UIAccountConfiguration) uiForm.getSibling(UIAccountConfiguration.class) ;
      Folder folder = uiForm.folder_ ;
      boolean newFolder = false ;
      if(folder == null) {
        folder = uiForm.service_.createFolderInstance() ;
        folder.setName(uiForm.nameInput_.getValue()) ;
        newFolder = true  ;
      }
      folder.setLabel(uiForm.labelInput_.getValue()) ;
      if(newFolder) {
        uiForm.service_.createFolder(uiForm.account_, folder) ;
      } else {
        uiForm.service_.updateFolder(folder) ;
      }
      uiAccConfig.updateSelectAccount(uiForm.account_.getAccountName()) ;
      uiForm.setRenderedSibling(UIAccountConfiguration.class) ;
    }
  }
  
  static public class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIFolderForm uiForm =  (UIFolderForm) event.getSource();
      uiForm.setRenderedSibling(UIAccountConfiguration.class) ;
    }
  } 
  
  static public class DeleteActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIFolderForm uiForm =  (UIFolderForm) event.getSource();
      if(uiForm.folder_ != null) {
        uiForm.service_.removeFolder(uiForm.folder_) ;
        UIAccountConfiguration uiAccConfig = 
          (UIAccountConfiguration) uiForm.getSibling(UIAccountConfiguration.class) ;
        uiAccConfig.updateSelectAccount(uiForm.account_.getAccountName()) ;
      }
      uiForm.setRenderedSibling(UIAccountConfiguration.class) ;
    }
  } 
}