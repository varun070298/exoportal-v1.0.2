/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import java.util.*;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.message.*;
import org.exoplatform.services.communication.message.Message;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIMessageForm.java,v 1.6 2004/10/23 01:58:56 tuan08 Exp $
 */
public class UIMessageForm extends UISimpleForm {
  public static String SEND_MESSAGE_ACTION = "sendMessage" ;
  private MessageService service_ ;
  private Account account_ ;
  private Message messageToReply_ ;
  private Message message_ ;
  
  private UIStringInput toInput_;
  private UISelectBox predefinedSubjectInput_;
  private UIStringInput subjectInput_;
  private UITextArea bodyInput_ ;
  
  public UIMessageForm(MessageService service) {
    super("messageForm", "post", null) ;
  	setId("UIMessagseForm") ;
    setClazz("UIMessageForm");
    setRendererType("SimpleFormButtonRenderer") ;
    service_ = service ;
    
    toInput_ = new UIStringInput("to", "") ;
    subjectInput_ = new UIStringInput("subject", "") ;
    bodyInput_ = new UITextArea("body", "") ;
    //String[] predifinedSubject = res.getStringArray("label.predefined-subject");
    String[] predifinedSubject = {
        "select a subject or enter your subject in the box below" ,
        "subject 1" ,
        "subject 2" ,
        "subject 3" 
    } ;
    List options = new ArrayList() ;
    for(int i = 0; i < predifinedSubject.length ; i++) {
      options.add(new SelectItem(predifinedSubject[i], predifinedSubject[i]));
    }
    predefinedSubjectInput_= new UISelectBox("predefinedSubject", "", options);
    add(new Row().
        add(new Cell("#{UIMessageForm.info.send-message-instruction}").
            addColspan("2")));
    add(new Row().setClazz("bottom-line").
        add(new LabelCell("#{UIMessageForm.label.to}")).
        add(new ComponentCell(this, toInput_)));
  
    add(new Row().setClazz("bottom-line").
        add(new LabelCell("#{UIMessageForm.label.subject}")).
        add(new ListComponentCell().
            add(this, predefinedSubjectInput_).
            add("<br>").
            add(this, subjectInput_)));
    Cell actions = 
      new ListComponentCell().
      add("<div style='float: right'>").
      add(new FormButton("#{UIMessageForm.button.send-message}", SEND_MESSAGE_ACTION)).
      add(new FormButton("#{UIMessageForm.button.cancel}", CANCEL_ACTION)).
      add("</div>").
      addColspan("2") ;
    add(new Row().add(actions)) ;
    add(new Row().
        add(new ComponentCell(this, bodyInput_).addColspan("2")));
    add(new Row(). add(actions)) ;
    
    addActionListener(SendMessageActionListener.class, SEND_MESSAGE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  } 
  public void setFormData(Account account, Message messageToReply) {
    account_ = account ;
    messageToReply_ = messageToReply ;
    message_ = service_.createMessageInstance() ;
    if(messageToReply_ != null) {
      toInput_.setValue(messageToReply.getFrom()) ;
      predefinedSubjectInput_.setValue("") ;
      subjectInput_.setValue("Re: " + messageToReply.getSubject()) ;
      bodyInput_.setValue(messageToReply.getBody()) ;
    } else  {
      toInput_.setValue("") ;
      predefinedSubjectInput_.setValue("") ;
      subjectInput_.setValue("") ;
      bodyInput_.setValue("") ;
    }
  }
  static public class SendMessageActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIMessageForm uiForm =  (UIMessageForm) event.getSource();
      Message message = uiForm.message_ ;
      String subject = uiForm.subjectInput_.getValue() ;
      if(subject == null || subject.length() == 0) {
        subject = uiForm.predefinedSubjectInput_.getValue() ;
      }
      message.setTo(uiForm.toInput_.getValue()) ;
      message.setSubject(subject) ;
      message.setBody(uiForm.bodyInput_.getValue()) ;
      uiForm.service_.sendMessage(uiForm.account_, message) ;     
      UISummary uiFolder = 
        (UISummary)uiForm.getSibling(UISummary.class) ;
      uiFolder.update() ;
      uiForm.setRenderedSibling(UISummary.class) ;
    }
  } 

  static public class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIMessageForm uiForm =  (UIMessageForm) event.getSource();
      uiForm.setRenderedSibling(UISummary.class) ;
    }    
  }
}