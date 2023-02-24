/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.message.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIAccount.java,v 1.9 2004/10/23 01:58:56 tuan08 Exp $
 */
public class UIAccount extends UIExoCommand  {
  public final static String COMPOSE_ACTION = "compose" ;
  public final static String SYNCHRONIZE_ACTION = "synchronize" ;
  public final static String CHANGE_FOLDER_ACTION = "changeFolder" ;

  public final static String FOLDER_NAME = "folderName" ;
  public static Parameter[] composeParams_ =  {new Parameter(ACTION, COMPOSE_ACTION)} ;
  public static Parameter[] synchronizeParams_ =  {new Parameter(ACTION, SYNCHRONIZE_ACTION)} ;
  public static Parameter   changeFolderParam_ =  new Parameter(ACTION, CHANGE_FOLDER_ACTION) ;
  
  private MessageService service_ ;
  private Account account_ ;
  private List folders_ ;
  private Folder selectFolder_ ;
  
  public UIAccount(MessageService service , 
                   UIViewMessage uiViewMessage,
                   UIMessageForm uiMessageForm,
                   UISummary uiSummary) {
  	setId("UIAccount") ;
    setClazz("UIAccount");
    setRendererType("AccountRenderer");
    service_ = service ;
    List children = getChildren() ;
    
    uiMessageForm.setRendered(false) ;
    children.add(uiMessageForm) ;
    uiViewMessage.setRendered(false) ;
    children.add(uiViewMessage) ;
    uiSummary.setRendered(true) ;
    children.add(uiSummary) ;
    
    addActionListener(SynchronizeActionListener.class, SYNCHRONIZE_ACTION) ;
    addActionListener(ComposeActionListener.class, COMPOSE_ACTION) ;
    addActionListener(ChangeFolderActionListener.class, CHANGE_FOLDER_ACTION) ;
  }
  
  public Account getAccount()  { return account_ ;  }
  
  public List getFolders() { return folders_ ; }
  
  public Folder getSelectFolder() { return selectFolder_ ; }
  
  public void setAccount(Account account) throws Exception { 
    account_ = account ;
    if(account != null) {
      folders_ = service_.getFolders(account.getId()) ;
      setSelectFolder(MessageService.INBOX_FOLDER) ;
      UISummary uiSummary = 
        (UISummary)getChildComponentOfType(UISummary.class) ;
      uiSummary.changeAccount(this) ;
      uiSummary.changeFolder(selectFolder_) ;
      setRenderedComponent(UISummary.class) ;
    }
  }
  
  public void setSelectFolder(String name) throws Exception {
    selectFolder_ = getFolder(name) ;
  }
  
  public Folder getFolder(String name) {
    for(int i = 0 ; i < folders_.size() ; i++) {
      Folder folder = (Folder) folders_.get(i) ;
      if(name.equals(folder.getName()))  {
        return folder ;
      }
    }
    return null ;
  }
  
  static public class ComposeActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccount uiAccount = (UIAccount) event.getSource() ;
      UIMessageForm uiMessageForm = 
        (UIMessageForm)uiAccount.getChildComponentOfType(UIMessageForm.class) ;
      uiMessageForm.setFormData(uiAccount.account_, null) ;
      uiAccount.setRenderedComponent(UIMessageForm.class) ;
    }
  }
  
  static public class SynchronizeActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccount uiAccount = (UIAccount) event.getSource() ;
      uiAccount.service_.synchronizeAccount(uiAccount.account_) ;
      UISummary uiSummary = 
        (UISummary)uiAccount.getChildComponentOfType(UISummary.class) ;
      uiAccount.setSelectFolder(MessageService.INBOX_FOLDER) ;
      uiSummary.changeFolder(uiAccount.selectFolder_) ;
      uiAccount.setRenderedComponent(UISummary.class) ;
    }
  }
  
  static public class ChangeFolderActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccount uiAccount = (UIAccount) event.getSource() ;
      String folderName = event.getParameter(FOLDER_NAME) ;
      uiAccount.setSelectFolder(folderName) ;
      UISummary uiSummary = 
        (UISummary)uiAccount.getChildComponentOfType(UISummary.class) ;
      uiSummary.changeFolder(uiAccount.selectFolder_) ;
      uiAccount.setRenderedComponent(UISummary.class) ;
    }
  }
  
  public String getFamily() {
    return "org.exoplatform.portlets.communication.message.component.UIAccount" ;
  }
}