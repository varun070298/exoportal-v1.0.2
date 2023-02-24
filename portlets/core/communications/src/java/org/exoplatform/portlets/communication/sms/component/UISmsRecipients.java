/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.component;

import javax.faces.component.UIOutput;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.LabelCell;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.services.communication.sms.SmsService;
import org.exoplatform.services.log.LogService;



/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 21, 2004 5:12:09 PM
 */
public class UISmsRecipients extends UIGrid {
    
    public UISmsRecipients(LogService logService, SmsService smsService) {
        super();
        setId("UISmsRecipients");
        setRendererType("SmsRecipientsRenderer");
        
        UIOutput uiOut = new UIOutput();
        uiOut.setId("testing");
        uiOut.setValue("Ove Ranheim");
        
        add(new Row().
                add(new LabelCell("Recipients")).
                add(new ComponentCell(this, uiOut)));
        
    }
    
    public String getFamily() {
        return "org.exoplatform.portlets.communication.sms.component.UISmsRecipients";
    }

}
