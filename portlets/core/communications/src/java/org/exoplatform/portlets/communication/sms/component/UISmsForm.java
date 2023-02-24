/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.portlet.ActionRequest;

import org.apache.commons.logging.Log;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIFormWithInformationProvider;
import org.exoplatform.faces.core.component.UISelectBox;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.UITextArea;
import org.exoplatform.faces.core.component.model.Cell;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.FormButton;
import org.exoplatform.faces.core.component.model.HeaderRow;
import org.exoplatform.faces.core.component.model.LabelCell;
import org.exoplatform.faces.core.component.model.ListComponentCell;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.faces.core.component.model.SelectItem;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.communication.sms.util.SmsPortletUtil;
import org.exoplatform.services.communication.sms.SmsService;
import org.exoplatform.services.communication.sms.encoder.MessageFormat;
import org.exoplatform.services.communication.sms.model.*;
import org.exoplatform.services.communication.sms.provider.Provider;
import org.exoplatform.services.communication.sms.provider.Sender;
import org.exoplatform.services.log.LogService;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 21, 2004 12:45:24 PM
 */
public class UISmsForm extends UIFormWithInformationProvider {
  
  public static final String SEND_SMS_ACTION = "SendSms";
  public static final String CLEAR_SMS_ACTION = "ClearSms";
  
  public static final String MSG_FROM_FIELD = "smsFromNumber";
  public static final String MSG_TO_FIELD = "smsToNumber";
  public static final String MSG_FORMAT_FIELD = "smsFormat";
  public static final String MSG_MESSAGE_FIELD = "smsMessage";
  public static final String MSG_MESSAGE_COUNTER_FIELD = "smsMessageCounter";
  
  private static final String JS_TEXT_COUNTER = 
    "<SCRIPT type='text/javascript'>\n"+
    "<!--\n"+
    "function textCounter(messagefield,cntfield,maxlimit) {\n"+
    "	cntfield.value = maxlimit - messagefield.value.length;\n"+
    "}\n"+
    "//-->\n"+
    "</SCRIPT>\n";
  
  private static List MSG_FORMATS;
  
  static {
    MSG_FORMATS = new ArrayList(5) ;
    MSG_FORMATS.add(new SelectItem("Plain Text", MessageFormat.PLAIN_TEXT.toString())) ;
    MSG_FORMATS.add(new SelectItem("Unicode Text", MessageFormat.UNICODE_TEXT.toString())) ;
    //MSG_FORMATS.add(new SelectItem("Raw UDH Binary", MessageFormat.RAW_BINARY_UDH.toString())) ;
  }
  
  private Log _log;
  private SmsService _sms;
  private ResourceBundle _res;
  
  private UIStringInput _uiSmsFrom;
  private UIStringInput _uiSmsTo;
  private UISmsRecipients _uiSmsRecipients;
  private UISelectBox _uiSmsFormats;
  private UITextArea _uiSmsMessage;
  private UIStringInput _uiSmsMessageCounter;
  
  public UISmsForm(LogService logService, SmsService smsService, ResourceBundle res) {
    super("smsForm", "post", null);
    setId("UISmsForm");
    setClazz("UISmsForm");
    
    _log = logService.getLog("org.exoplatform.portlets.communication.sms.component");
    _sms = smsService;
    _res = res;
    
    _uiSmsFrom = new UIStringInput(MSG_FROM_FIELD, "");
    _uiSmsFrom.setTitle(res.getString("input.sms-from-number-format"));
    _uiSmsRecipients = new UISmsRecipients(logService, smsService);
    _uiSmsTo = new UIStringInput(MSG_TO_FIELD, "");
    _uiSmsTo.setTitle(res.getString("input.sms-number-format"));
    _uiSmsFormats = new UISelectBox(MSG_FORMAT_FIELD, MessageFormat.PLAIN_TEXT.toString(), MSG_FORMATS);
    _uiSmsMessage = new UITextArea(MSG_MESSAGE_FIELD, "").setCols("80").setRows("24");
    _uiSmsMessage.setKeyDown("textCounter("+MSG_MESSAGE_FIELD+","+MSG_MESSAGE_COUNTER_FIELD+",804)");
    _uiSmsMessage.setKeyUp("textCounter("+MSG_MESSAGE_FIELD+","+MSG_MESSAGE_COUNTER_FIELD+",804)");
    _uiSmsMessageCounter = new UIStringInput(MSG_MESSAGE_COUNTER_FIELD, "804");
    _uiSmsMessageCounter.setClazz("counter");
    _uiSmsMessageCounter.setEditable(false);
    
    addActionListener(SendSmsActionListener.class, SEND_SMS_ACTION);
    addActionListener(ClearSmsActionListener.class, CLEAR_SMS_ACTION);
    
    add(new HeaderRow().
        add(new Cell(res.getString("header.send-sms")).
            addColspan("2")));
    add(new Row().
        add(new LabelCell(res.getString("label.sms-from"))).
        add(new ComponentCell(this, _uiSmsFrom)));
    /*add(new Row().
     add(new LabelCell("Recipients")).
     add(new ComponentCell(this, _uiSmsRecipients)));*/
    add(new Row().
        add(new LabelCell(res.getString("label.sms-recipients"))).
        add(new ComponentCell(this, _uiSmsTo)));
    add(new Row().
        add(new LabelCell(res.getString("label.sms-message"))).
        add(new ComponentCell(this, _uiSmsFormats)));
    add(new Row().
        add(new ComponentCell(this, _uiSmsMessage).addColspan("2")));
    add(new Row().
        add(new ComponentCell(this, _uiSmsMessageCounter).addColspan("2").addAlign("right")));
    
    FormButton sendButton = new FormButton(res.getString("button.sms-send"), SEND_SMS_ACTION);
    sendButton.setClass("send");
    FormButton clearButton = new FormButton(res.getString("button.sms-clear"), CLEAR_SMS_ACTION);
    clearButton.setClass("clear");
    
    add(new Row().add(new ListComponentCell().add(sendButton).add(clearButton).addColspan("2")));
  }
  
  public void encodeBegin(FacesContext context) throws IOException {
    super.encodeBegin(context);
    ExternalContext econtext = context.getExternalContext(); 
    boolean inSmsRole = (econtext.getUserPrincipal() != null);
    if (!inSmsRole) {
      _uiSmsFrom.setText("eXo platform");
      _uiSmsFrom.setEditable(false);
    } else {
      _uiSmsFrom.setEditable(true);
    }
    ResponseWriter w = context.getResponseWriter();        
    Provider provider = SmsPortletUtil.getProvider(_sms);
    w.write(JS_TEXT_COUNTER);
    //w.write("<label>Username: "+provider.getOperator().getUsername()+"</label>&nbsp;&nbsp;");
    //w.write("<label>Password: "+provider.getOperator().getPassword()+"</label>");
  }
  
  public void encodeEnd(FacesContext context) throws IOException {
    super.encodeEnd(context);
  }
  
  
  static public class SendSmsActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UISmsForm uiForm = (UISmsForm) event.getComponent();
      Provider provider = SmsPortletUtil.getProvider(uiForm._sms);
      ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext();
      Map parameters = econtext.getRequestParameterValuesMap();
      String from = ((String[]) parameters.get(MSG_FROM_FIELD))[0];
      String to = ((String[]) parameters.get(MSG_TO_FIELD))[0];
      MessageFormat messageFormat = MessageFormat.parse( ((String[]) parameters.get(MSG_FORMAT_FIELD))[0] );
      String message = ((String[]) parameters.get(MSG_MESSAGE_FIELD))[0];
      
      InformationProvider iprovider = findInformationProvider(uiForm);
      ResourceBundle res = event.getApplicationResourceBundle() ;
      boolean hasError = false;
      if (from == null || from.length() == 0) {
        iprovider.addMessage(new FacesMessage(res.getString("msg.error-sms-from-field")));
        hasError = true;
      }
      if (to == null || to.length() == 0) {
        iprovider.addMessage(new FacesMessage(res.getString("msg.error-sms-to-field")));
        hasError = true;
      }
      if (message == null || message.length() == 0) {
        iprovider.addMessage(new FacesMessage(res.getString("msg.error-sms-message-field")));
        hasError = true;
      }
      
      if (hasError) return;
      
      SmsService sms = uiForm._sms;
      ActionRequest prequest = (ActionRequest) econtext.getRequest();
      String encoding = prequest.getCharacterEncoding();
      Messages messages = sms.createMessages();
      messages.setEncoding(encoding);
      org.exoplatform.services.communication.sms.model.Message msg = messages.addMessage();
      msg.setFrom(from);
      msg.addRecipient(to);
      msg.setContent(message);
      msg.setFormat(messageFormat);
      
      Sender sender = sms.createSender(provider);
      sender.prepare(messages);
      uiForm._log.debug("Message: "+message);
      String payload = sender.getProvider().getAdapter().getRequest().getPayload();
      uiForm._log.debug("PSWinCom Payload: "+payload);
      hasError = sender.send().hasErrorOccured();
      
      if (!hasError) {
        iprovider.addMessage(new FacesMessage(res.getString("msg.sms-success")));
      } else {
        iprovider.addMessage(new FacesMessage(res.getString("msg.sms-failure")+"<br>"));
        String errorMsg = "";
        if (LogonStatus.FAILED.equals(messages.getLogonStatus())) {
          errorMsg = messages.getReason();
        } else {
          Recipient r = (Recipient) msg.getRecipients().get(0);
          errorMsg = r.getError();
        }                        
        iprovider.addMessage(new FacesMessage(res.getString("msg.sms-failure-reason")+errorMsg));
      }
      
    }
  }
  
  static public class ClearSmsActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UISmsForm uiForm = (UISmsForm) event.getComponent();
      uiForm._uiSmsFrom.setValue("");
      uiForm._uiSmsTo.setValue("");
      uiForm._uiSmsMessage.setValue("");
      uiForm._uiSmsFormats.setValue(MessageFormat.PLAIN_TEXT.toString());
      uiForm._uiSmsMessageCounter.setValue("804");
    }
  }
}