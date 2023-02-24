/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import java.util.List;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.message.Account;
import org.exoplatform.services.communication.message.Folder;
import org.exoplatform.services.communication.message.MessageService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIAccountConfiguration.java,v 1.7 2004/11/03 01:24:56 tuan08 Exp $
 */
public class UIAccountConfiguration extends UIExoCommand {
  public final static String ADD_ACCOUNT_ACTION = "addAccount" ;
  public final static String EDIT_ACCOUNT_ACTION = "editAccount" ;
  public final static String DELETE_ACCOUNT_ACTION = "deleteAccount" ;
  public final static String SELECT_ACCOUNT_ACTION = "selectAccount" ;
  public final static String SELECT_FOLDER_ACTION = "selectFolder" ;
  public final static String ADD_FOLDER_ACTION = "addFolder" ;
  
  public final static String ACCOUNT_NAME = "accountName" ;
  public final static String FOLDER_NAME = "folderName" ;
  
  public static Parameter[] addAccountParams_ =  {new Parameter(ACTION, ADD_ACCOUNT_ACTION)} ;
  public static Parameter[] editAccountParams_ =  {new Parameter(ACTION, EDIT_ACCOUNT_ACTION)} ;
  public static Parameter[] deleteAccountParams_ =  {new Parameter(ACTION, DELETE_ACCOUNT_ACTION)} ;
  public static Parameter selectAccountParam =  new Parameter(ACTION, SELECT_ACCOUNT_ACTION) ;
  public static Parameter selectFolderParam =  new Parameter(ACTION, SELECT_FOLDER_ACTION) ;
  public static Parameter[] addFolderParams_ =  {new Parameter(ACTION, ADD_FOLDER_ACTION)} ;
  
  private MessageService service_ ;
  private String accountOwner_ ;
  private List accounts_ ;
  private Account selectAccount_ ;
  private List selectAccountFolders_ ;
  
  public UIAccountConfiguration(MessageService service) throws Exception {
  	setId("UIAccountConfiguration") ;
    setClazz("UIAccountConfiguration");
    setRendererType("AccountConfigurationRenderer");
    service_ = service ;
    accountOwner_ = SessionContainer.getInstance().getOwner() ;
    updateAccountList() ;
    addActionListener(AddAccountActionListener.class, ADD_ACCOUNT_ACTION) ;
    addActionListener(EditAccountActionListener.class, EDIT_ACCOUNT_ACTION) ;
    addActionListener(DeleteAccountActionListener.class, DELETE_ACCOUNT_ACTION) ;
    addActionListener(SelectAccountActionListener.class, SELECT_ACCOUNT_ACTION) ;
    addActionListener(SelectFolderActionListener.class, SELECT_FOLDER_ACTION) ;
    addActionListener(AddFolderActionListener.class, ADD_FOLDER_ACTION) ;
  }
  
  public String getAccountOwner() { return accountOwner_ ; }
  
  public List getAccounts() { return accounts_ ; }
  
  public Account getSelectAccount() { return selectAccount_ ; }
  
  public List getSelectAccountFolders() { return selectAccountFolders_ ; }
  
  public void updateAccountList() throws Exception {
    accounts_ = service_.getAccounts(accountOwner_) ;
    if(accounts_.size() > 0) {
      selectAccount_ = (Account) accounts_.get(0) ;
      selectAccountFolders_ = service_.getFolders(selectAccount_.getId()) ;
    } else {
      selectAccount_ = null;
      selectAccountFolders_ = null ;
    }
  }
  
  public void updateSelectAccount(String accountName) throws Exception {
    for(int i = 0 ; i < accounts_.size() ;i++) {
      Account account = (Account) accounts_.get(i) ;
      if(accountName.equals(account.getAccountName()))  {
        selectAccount_ = account ;
        selectAccountFolders_ = service_.getFolders(selectAccount_.getId()) ;
        return ;
      }
    }
  }
  
  public String getFamily() {
  	return "org.exoplatform.portlets.communication.message.component.UIAccountConfiguration" ;
  }
  
  static public class AddAccountActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccountConfiguration uiAccConfig =  (UIAccountConfiguration) event.getSource();
      UIAccountForm uiForm = (UIAccountForm) uiAccConfig.getSibling(UIAccountForm.class) ;
      uiForm.setAccount(null) ;
      uiAccConfig.setRenderedSibling(UIAccountForm.class) ;
    }
  }
  
  static public class EditAccountActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccountConfiguration uiAccConfig =  (UIAccountConfiguration) event.getSource();
      UIAccountForm uiForm = (UIAccountForm) uiAccConfig.getSibling(UIAccountForm.class) ;
      uiForm.setAccount(uiAccConfig.selectAccount_) ;
      uiAccConfig.setRenderedSibling(UIAccountForm.class) ;
    }
  }
  

  static public class DeleteAccountActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccountConfiguration uiAccConfig = (UIAccountConfiguration) event.getSource() ;
      uiAccConfig.service_.removeAccount(uiAccConfig.selectAccount_) ;
      uiAccConfig.updateAccountList();
    }
  }
  
  
  static public class SelectAccountActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccountConfiguration uiAccConfig = (UIAccountConfiguration) event.getSource() ;
      String accountName = event.getParameter(ACCOUNT_NAME) ;
      uiAccConfig.updateSelectAccount(accountName) ;
    }
  }
  
  static public class AddFolderActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccountConfiguration uiAccConfig = (UIAccountConfiguration) event.getSource() ;
      UIFolderForm uiFolderForm = 
        (UIFolderForm) uiAccConfig.getSibling(UIFolderForm.class) ;
      uiFolderForm.setFormData(uiAccConfig.selectAccount_, null) ;
      uiAccConfig.setRenderedSibling(UIFolderForm.class) ;
    }
  }
  
  static public class SelectFolderActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccountConfiguration uiAccConfig = (UIAccountConfiguration) event.getSource() ;
      String folderName = event.getParameter(FOLDER_NAME) ;
      UIFolderForm uiFolderForm = 
        (UIFolderForm) uiAccConfig.getSibling(UIFolderForm.class) ;
      List folders = uiAccConfig.selectAccountFolders_ ;
      for (int i = 0; i < folders.size(); i++) {
        Folder folder = (Folder) folders.get(i) ;
        if(folderName.equals(folder.getName())) {
          uiFolderForm.setFormData(uiAccConfig.selectAccount_, folder) ;
          uiAccConfig.setRenderedSibling(UIFolderForm.class) ;
          return ;
        }
      }
    }
  }
}