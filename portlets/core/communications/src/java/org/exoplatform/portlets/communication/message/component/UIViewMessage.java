/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.message.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIViewMessage.java,v 1.10 2004/10/23 01:58:56 tuan08 Exp $
 */
public class UIViewMessage extends UIExoCommand {
  public final static String TEXT_FORMAT = "TEXT_FORMAT" ;
  public final static String HTML_FORMAT = "HTML_FORMAT" ;
  
  public final static String FORMAT_TYPE = "format" ;
  public final static String REPLY_MESSAGE_ACTION = "replyMessage" ;
  public final static String ARCHIVED_MESSAGE_ACTION = "achieveMessage" ;
  public final static String CHANGE_FORMAT_ACTION = "changeFormat" ;
  public static Parameter[] replyMessageParams_ = { 
      new Parameter(ACTION, REPLY_MESSAGE_ACTION) 
  };
  public static Parameter[] deleteMessageParams_ = { 
      new Parameter(ACTION, DELETE_ACTION) 
  };
  public static Parameter[] archivedMessageParams_ = { 
      new Parameter(ACTION, ARCHIVED_MESSAGE_ACTION) 
  };
  public static Parameter[] htmlFormatParams_ = { 
      new Parameter(ACTION, CHANGE_FORMAT_ACTION),
      new Parameter(FORMAT_TYPE, HTML_FORMAT) 
  };
  public static Parameter[] textFormatParams_ = { 
      new Parameter(ACTION, CHANGE_FORMAT_ACTION),
      new Parameter(FORMAT_TYPE, TEXT_FORMAT) 
  };
  public static Parameter[] cancelParams_ = { 
      new Parameter(ACTION, CANCEL_ACTION) 
  };  

  private Message message_ ;
  private MessageService service_ ;
  private String format_ = HTML_FORMAT;
  
  public UIViewMessage(MessageService service) {
  	setId("UIViewMessage") ;
    setClazz("UIViewMessage");
    setRendererType("ViewMessageRenderer") ;
    service_ = service ;
    addActionListener(ChangeFormatActionListener.class, CHANGE_FORMAT_ACTION) ;
    addActionListener(DeleteMessageActionListener.class, DELETE_ACTION) ;
    addActionListener(ReplyMessageActionListener.class, REPLY_MESSAGE_ACTION) ;
    addActionListener(ArchivedMessageActionListener.class, ARCHIVED_MESSAGE_ACTION) ;
    
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  }
  
  public void setMessage(Message message) throws Exception {
    message_ = message ;
  }
  
  public Message getMessage() { return message_ ; }
  
  public String getFormat() { return format_ ; }
  
  public String getFamily() {
    return "org.exoplatform.portlets.communication.message.component.UIViewMessage" ;
  }
  
  static public class DeleteMessageActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIViewMessage uiView =  (UIViewMessage) event.getSource();
      UIAccount uiAccount = (UIAccount) uiView.getParent() ;
      Folder selectFolder = uiAccount.getSelectFolder() ;
      if(!(MessageService.TRASH_FOLDER.equals(selectFolder.getName()))) {
        Folder trash = uiAccount.getFolder(MessageService.TRASH_FOLDER) ;
        uiView.service_.moveMessage(uiAccount.getAccount(),trash, uiView.message_) ;
      } else {
        uiView.service_.removeMessage(uiView.message_) ;
      }
      UISummary uiSummary = (UISummary) uiView.getSibling(UISummary.class) ;
      uiSummary.update() ;
      uiView.setRenderedSibling(UISummary.class) ;
    }
  } 
  
  static public class ArchivedMessageActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIViewMessage uiView =  (UIViewMessage) event.getSource();
      UIAccount uiAccount = (UIAccount) uiView.getParent() ;
      Folder archived = uiAccount.getFolder(MessageService.ARCHIVED_FOLDER) ;
      uiView.service_.moveMessage(uiAccount.getAccount(), archived,uiView.message_) ;
      UISummary uiSummary = (UISummary) uiView.getSibling(UISummary.class) ;
      uiSummary.update() ;
      uiView.setRenderedSibling(UISummary.class) ;
    }
  } 
  
  static public class ReplyMessageActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIViewMessage uiView =  (UIViewMessage) event.getSource();
      UIMessageForm uiForm = (UIMessageForm) uiView.getSibling(UIMessageForm.class) ;
      UIAccount uiAccount = (UIAccount) uiView.getParent() ;
      uiForm.setFormData(uiAccount.getAccount() , uiView.message_) ;
      uiView.setRenderedSibling(UIMessageForm.class) ;
    }
  } 
  
  static public class ChangeFormatActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIViewMessage uiView =  (UIViewMessage) event.getSource();
      uiView.format_ = event.getParameter(FORMAT_TYPE) ;
    }
  } 

  static public class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIViewMessage uiView =  (UIViewMessage) event.getSource();
      UIAccount uiAccount = (UIAccount) uiView.getParent() ;
      uiAccount.setSelectFolder(uiAccount.getSelectFolder().getName());      
      uiView.setRenderedSibling(UISummary.class) ;      
    }
  } 
}