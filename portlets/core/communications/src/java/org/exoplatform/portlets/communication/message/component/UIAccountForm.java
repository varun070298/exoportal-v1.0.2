/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import java.util.*;
import javax.faces.event.PhaseId;
import org.exoplatform.faces.core.Util;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.message.Account;
import org.exoplatform.services.communication.message.MessageService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIAccountForm.java,v 1.3 2004/10/14 23:40:27 tuan08 Exp $
 */
public class UIAccountForm extends  UISimpleForm {
  final static public String  CHANGE_PROTOCOL_ACTION   = 
    Util.encodeActionPhase("changeProtocol", PhaseId.APPLY_REQUEST_VALUES) ;
  static private List PROTOCOL_OPTIONS ;
  static {
    PROTOCOL_OPTIONS = new ArrayList() ;
    PROTOCOL_OPTIONS .add(new SelectItem(MessageService.STANDALONE_PROTOCOL, MessageService.STANDALONE_PROTOCOL));
    PROTOCOL_OPTIONS .add(new SelectItem(MessageService.IMAP_PROTOCOL, MessageService.IMAP_PROTOCOL));
    PROTOCOL_OPTIONS .add(new SelectItem(MessageService.POP3_PROTOCOL, MessageService.POP3_PROTOCOL));
  }
  private MessageService service_ ;
  private Account account_ ;
  
  private UIStringInput accountNameInput_;
  private UIStringInput ownerNameInput_;
  private UIStringInput replyToAddressInput_;
  private UISelectBox   protocolInput_;
  private UIStringInput userNameInput_;
  private UIStringInput passwordInput_;
  private UIStringInput mailServerInput_ ;
  private UITextArea signatureInput_;
  
  private Row userNameRow_ ;
  private Row passwordRow_ ;
  private Row mailServerRow_ ;
  
  public UIAccountForm(MessageService service) throws Exception {
    super("accountForm", "post", null) ;
  	setId("UIAccountForm") ;
    setClazz("UIAccountForm");
    service_ = service ;
    
    accountNameInput_ = new UIStringInput("accountName", "") ;
    ownerNameInput_ = new UIStringInput("ownerName", "") ;
    replyToAddressInput_ = new UIStringInput("replyTo", "") ;
    protocolInput_ = new UISelectBox("protocol", "", PROTOCOL_OPTIONS);
    protocolInput_.setUpdateOnChangeAction(CHANGE_PROTOCOL_ACTION) ;
    userNameInput_ = new UIStringInput("userName", "") ;
    passwordInput_ = new UIStringInput("password", "") ;
    mailServerInput_ = new UIStringInput("mailServer", "") ;
    signatureInput_ = new UITextArea("signature", "") ;
    
    add(new HeaderRow().
        add(new Cell("#{UIAccountForm.header.add-edit-account}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIAccountForm.label.account-name}")).
        add(new ComponentCell(this, accountNameInput_)));
    add(new Row().
        add(new LabelCell("#{UIAccountForm.label.owner-name}")).
        add(new ComponentCell(this, ownerNameInput_)));
    add(new Row().
        add(new LabelCell("#{UIAccountForm.label.reply-to}")).
        add(new ComponentCell(this, replyToAddressInput_)));
    add(new Row().
        add(new LabelCell("#{UIAccountForm.label.message-protocol}")).
        add(new ComponentCell(this, protocolInput_)));
    userNameRow_ = new Row().
                   add(new LabelCell("#{UIAccountForm.label.user-name}")).
                   add(new ComponentCell(this, userNameInput_)) ;
    add(userNameRow_);
    passwordRow_ = new Row().
                  add(new LabelCell("#{UIAccountForm.label.password}")).
                  add(new ComponentCell(this, passwordInput_)) ;
    add(passwordRow_);
    mailServerRow_ = new Row().
                     add(new LabelCell("#{UIAccountForm.label.mail-server}")).
                     add(new ComponentCell(this, mailServerInput_));
    add(mailServerRow_);
    add(new Row().
        add(new LabelCell("#{UIAccountForm.label.signature}")).
        add(new ComponentCell(this, signatureInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UIAccountForm.button.save}", SAVE_ACTION)).
            add(new FormButton("#{UIAccountForm.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
    addActionListener(ChangeProtocolActionListener.class, CHANGE_PROTOCOL_ACTION) ;
  }
  
  public void changeProtocol(String protocol) {
    if(protocol == null || MessageService.STANDALONE_PROTOCOL.equals(protocol)) {
      userNameRow_.setVisible(false) ;
      passwordRow_.setVisible(false) ;
      mailServerRow_.setVisible(false) ;
    } else {
      userNameRow_.setVisible(true) ;
      passwordRow_.setVisible(true) ;
      mailServerRow_.setVisible(true) ;
    }
  }
  
  public void setAccount(Account account) {
    account_  = account ;
    if(account == null) {
      accountNameInput_.setValue("") ;
      accountNameInput_.setEditable(true) ;
      ownerNameInput_.setValue("") ;
      replyToAddressInput_.setValue("") ;
      userNameInput_.setValue("") ;
      passwordInput_.setValue("") ;
      mailServerInput_.setValue("") ;
      protocolInput_.setValue(MessageService.STANDALONE_PROTOCOL) ;
      changeProtocol(MessageService.STANDALONE_PROTOCOL) ;
    } else  {
      accountNameInput_.setValue(account.getAccountName()) ;
      accountNameInput_.setEditable(false) ;
      ownerNameInput_.setValue(account.getOwnerName()) ;
      replyToAddressInput_.setValue(account.getReplyToAddress()) ;
      userNameInput_.setValue(account.getProperty(Account.SERVER_SETTING_USERNAME)) ;
      passwordInput_.setValue(account.getProperty(Account.SERVER_SETTING_PASSWORD)) ;
      mailServerInput_.setValue(account.getProperty(Account.SERVER_SETTING_HOSTNAME)) ;
      protocolInput_.setValue(account.getProtocol()) ;
      changeProtocol(account.getProtocol()) ;
    }
  }
   
  static public class SaveActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccountForm uiForm =  (UIAccountForm) event.getSource();
      UIAccountConfiguration uiAccConfig = 
        (UIAccountConfiguration) uiForm.getSibling(UIAccountConfiguration.class) ;
      Account account = uiForm.account_ ;
      boolean newAccount = false ;
      if (account == null) {
        account = uiForm.service_.createAccountInstance() ;
        account.setAccountName(uiForm.accountNameInput_.getValue()) ;
        account.setOwner(uiAccConfig.getAccountOwner()) ;
        newAccount = true ;
      }
      account.setOwnerName(uiForm.ownerNameInput_.getValue()) ;
      account.setReplyToAddress(uiForm.replyToAddressInput_.getValue()) ;
      account.setProtocol(uiForm.protocolInput_.getValue()) ;
      account.setSignature(uiForm.signatureInput_.getValue()) ;
      
      account.setProperty(Account.SERVER_SETTING_USERNAME, 
                          uiForm.userNameInput_.getValue()) ;
      account.setProperty(Account.SERVER_SETTING_PASSWORD, 
                          uiForm.passwordInput_.getValue()) ;
      account.setProperty(Account.SERVER_SETTING_HOSTNAME, 
                          uiForm.mailServerInput_.getValue()) ;
      if(newAccount) {
        uiForm.service_.createAccount(account) ;
      } else {
        uiForm.service_.updateAccount(account) ;
      }
      uiAccConfig.updateAccountList() ;
      uiForm.setRenderedSibling(UIAccountConfiguration.class) ;
    }
  }
  
  static public class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccountForm uiForm =  (UIAccountForm) event.getSource();
      uiForm.setRenderedSibling(UIAccountConfiguration.class) ;
    }
  }
  
  static public class ChangeProtocolActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAccountForm uiForm =  (UIAccountForm) event.getSource();
      uiForm.changeProtocol(uiForm.protocolInput_.getValue()) ;
    }
  }
}