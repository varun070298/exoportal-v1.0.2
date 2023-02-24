/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.util;

import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import org.exoplatform.services.communication.sms.SmsService;
import org.exoplatform.services.communication.sms.provider.Provider;



/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 22, 2004 4:14:49 PM
 */
public class SmsPortletUtil {
    public static final String PREF_USERNAME = "gatewayUsername";
    public static final String PREF_PASSWORD = "gatewayPassword";
    public static String SMS_PROVIDER = "SMS_PROVIDER";

    public static Provider getProvider(SmsService service) {
        FacesContext context = FacesContext.getCurrentInstance();
        PortletRequest request = (PortletRequest) context.getExternalContext().getRequest();
        PortletSession session = request.getPortletSession();
        Provider provider = (Provider) session.getAttribute(SMS_PROVIDER);
        if (provider == null) {
            PortletPreferences prefs = request.getPreferences();
            String uid = prefs.getValue(PREF_USERNAME, "");
            String pwd = prefs.getValue(PREF_PASSWORD, "");
            provider = service.createProdatProvider(uid, pwd);
            session.setAttribute(SMS_PROVIDER, provider);
        }
        return provider;
    }

    
}
