/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.component;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.Cell;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.HeaderRow;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.services.communication.sms.SmsMonitorService;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 21, 2004 12:45:24 PM
 */
public class UISmsMonitor extends UIGrid {

    private SmsMonitorService _monitor;
    private ResourceBundle _res;

    private UIOutput _uiNumberOfSentMessages;
    private UIOutput _uiNumberOfSuccessfulMessages;
    private UIOutput _uiNumberOfFailedMessages;
    private UIOutput _uiNumberOfErroneousMessages;

    public UISmsMonitor(SmsMonitorService smsMonitorService, ResourceBundle res) {
        super();
        setId("UISmsMonitorForm");
        setRendererType("SmsMonitorRenderer");
        setClazz("UISmsForm");
        _monitor = smsMonitorService;
        _res = res;

        _uiNumberOfSentMessages = new UIOutput();
        _uiNumberOfSentMessages.setId("NumberOfSentMessages");
        _uiNumberOfSuccessfulMessages = new UIOutput();
        _uiNumberOfSuccessfulMessages.setId("NumberOfSuccessfulMessages");
        _uiNumberOfFailedMessages = new UIOutput();
        _uiNumberOfFailedMessages.setId("NumberOfFailedMessages");
        _uiNumberOfErroneousMessages = new UIOutput();
        _uiNumberOfErroneousMessages.setId("NumberOfErroneousMessages");
        
        //updateCounter();
        
        add(new HeaderRow().
            add(new Cell("SMS Monitor").
            addColspan("2")));
        add(new Row().
            add(new Cell("Total number sent Messages").addClazz("counter-label")).
            add(new ComponentCell(this, _uiNumberOfSentMessages)));
        add(new Row().
	        add(new Cell("Successfull Messages").addClazz("counter-label")).
	        add(new ComponentCell(this, _uiNumberOfSuccessfulMessages)));
        add(new Row().
            add(new Cell("Failed Messages").addClazz("counter-label")).
            add(new ComponentCell(this, _uiNumberOfFailedMessages)));
        add(new Row().
                add(new Cell("Erroneous Messages").addClazz("counter-label")).
                add(new ComponentCell(this, _uiNumberOfErroneousMessages)));
    }

    public String getFamily() {
        return "org.exoplatform.portlets.communication.sms.component.UISmsMonitor";
    }
    
    public void updateCounter() {
        _uiNumberOfSentMessages.setValue(_monitor.getCountMessages());
        _uiNumberOfSuccessfulMessages.setValue(_monitor.getSuccessfullMessages());
        _uiNumberOfFailedMessages.setValue(_monitor.getFailedMessages());
        _uiNumberOfErroneousMessages.setValue(_monitor.getErroneousMessage());
        
    }

}