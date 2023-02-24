/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.impl;

import org.exoplatform.services.communication.sms.SmsMonitorService;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 20, 2004 5:54:56 PM
 */
public class SmsMonitorServiceImpl implements SmsMonitorService {
    
    static private Integer _countMessages;
    static private Integer _successfullMessages;
    static private Integer _failedMessages;

    public  SmsMonitorServiceImpl() {        
        _countMessages = new Integer(0);
        _successfullMessages = new Integer(0);
        _failedMessages = new Integer(0);
    }
    
    public Integer getCountMessages() {
        return _countMessages;
    }

    public void incCountMessages() {
        synchronized(_countMessages) {
            int counter = _countMessages.intValue();
            counter++;
            _countMessages = new Integer(counter);
        }
    }

    public Integer getFailedMessages() {
        return _failedMessages;
    }

    public void incFailedMessages() {
        synchronized(_failedMessages) {
            int counter = _failedMessages.intValue();
            counter++;
            _failedMessages = new Integer(counter);
        }
    }

    public Integer getSuccessfullMessages() {
        return _successfullMessages;
    }

    public void incSuccessfulMessages() {
        synchronized(_successfullMessages) {
            int counter = _successfullMessages.intValue();
            counter++;
            _successfullMessages = new Integer(counter);
        }
    }
    
    public Integer getErroneousMessage() {
        synchronized(_countMessages) {
            int counter = _countMessages.intValue();
            int success = _successfullMessages.intValue();
            int failed = _failedMessages.intValue();
            return new Integer(counter-success-failed);            
        }
    }

}
