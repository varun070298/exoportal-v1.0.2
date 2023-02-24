/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.component;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import org.apache.commons.logging.Log;
import org.exoplatform.faces.FacesUtil;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIFormWithInformationProvider;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.communication.sms.util.SmsPortletUtil;
import org.exoplatform.services.communication.sms.SmsService;
import org.exoplatform.services.communication.sms.provider.Provider;
import org.exoplatform.services.log.LogService;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 21, 2004 12:45:24 PM
 */
public class UISmsAdminForm extends UIFormWithInformationProvider {

    public static final String SAVE_SMS_CONFIG_ACTION = "SaveSmsConfig";
    private static final String PSWINCOM_IMAGE = "<p><a target='_new' href='http://www.pswin.com'><img border='0' src='/content/images/PSWin3.jpg' align='left' valign'top'></a>";

    private Log _log;
    private SmsService _sms;
    private ResourceBundle _res;

    private UIStringInput _uiUsername;
    private UIStringInput _uiPassword;

    public UISmsAdminForm(LogService logService, SmsService smsService, ResourceBundle res) {
        super("smsAdminForm", "post", null);
        setId("UISmsAdminForm");
        setClazz("UISmsForm");
        _log = logService.getLog("org.exoplatform.portlets.communication.sms.component");
        _sms = smsService;
        _res = res;

        PortletPreferences prefs = FacesUtil.getPortletPreferences();
        String uid = prefs.getValue(SmsPortletUtil.PREF_USERNAME, "");
        String pwd = prefs.getValue(SmsPortletUtil.PREF_PASSWORD, "");

        _uiUsername = new UIStringInput(SmsPortletUtil.PREF_USERNAME, uid);
        _uiPassword = new UIStringInput(SmsPortletUtil.PREF_PASSWORD, "******");
        _uiPassword.setType(UIStringInput.PASSWORD);

        addActionListener(SaveSmsConfigActionListener.class, SAVE_SMS_CONFIG_ACTION);

        add(new HeaderRow().
            add(new Cell(res.getString("header.sms-config")).
            addColspan("2")));
        add(new Row().
            add(new Cell(PSWINCOM_IMAGE+res.getString("header.sms-info")+res.getString("header.sms-info-site")+"<br><br><br>").
            addColspan("2"))).
        add(new Row().
            add(new LabelCell(res.getString("label.sms-config-username"))).
            add(new ComponentCell(this, _uiUsername))).
        add(new Row().
            add(new LabelCell(res.getString("label.sms-config-password"))).
            add(new ComponentCell(this, _uiPassword)));
        
        FormButton saveButton = new FormButton(res.getString("button.sms-config-save-config"), SAVE_SMS_CONFIG_ACTION);
        saveButton.setClass("save");

        add(new Row().add(new ListComponentCell().
            add(saveButton).
            addColspan("2")));
    }

    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);
    }

    static public class SaveSmsConfigActionListener extends ExoActionListener {
      public void execute(ExoActionEvent event) throws Exception {
        UISmsAdminForm uiForm = (UISmsAdminForm) event.getComponent();
        Provider provider = SmsPortletUtil.getProvider(uiForm._sms);
        Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
        String uid = ((String[]) parameters.get(SmsPortletUtil.PREF_USERNAME))[0];
        String pwd = ((String[]) parameters.get(SmsPortletUtil.PREF_PASSWORD))[0];
        
        InformationProvider iprovider = findInformationProvider(uiForm);
        boolean hasError = false;
        if (uid == null || uid.length() == 0) {
          iprovider.addMessage(new ExoFacesMessage("msg.error-no-username"));
          hasError = true;
        }
        
        if (pwd == null || pwd.length() == 0 || ("******".equals(pwd))) {
          iprovider.addMessage(new ExoFacesMessage("msg.error-no-password"));
          hasError = true;
        }
        
        if (hasError) return;
        
        provider.getOperator().setUsername(uid);
        provider.getOperator().setPassword(pwd);
        
        PortletPreferences prefs = FacesUtil.getPortletPreferences();
        
        prefs.setValue(SmsPortletUtil.PREF_USERNAME, provider.getOperator().getUsername());
        prefs.setValue(SmsPortletUtil.PREF_PASSWORD, provider.getOperator().getPassword());
        try {
          prefs.store();
        } catch(Throwable t) {
          iprovider.addMessage(new FacesMessage("We got problems with storing the portlet preferences"));
          t.printStackTrace(); 
        };
      }
    }
    
}