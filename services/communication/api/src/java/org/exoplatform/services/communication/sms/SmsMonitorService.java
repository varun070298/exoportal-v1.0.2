/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 20, 2004 5:55:44 PM
 */
public interface SmsMonitorService {

    /**
     * @return
     */
    public Integer getCountMessages();

    /**
     * 
     */
    public void incCountMessages();

    /**
     * @return
     */
    public Integer getFailedMessages();

    /**
     * 
     */
    public void incFailedMessages();

    /**
     * @return
     */
    public Integer getSuccessfullMessages();

    /**
     * 
     */
    public void incSuccessfulMessages();
    
    /**
     * @return
     */
    public Integer getErroneousMessage();

}
