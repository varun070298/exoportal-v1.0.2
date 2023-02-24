/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.test;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.communication.sms.SmsMonitorService;
import org.exoplatform.services.communication.sms.SmsService;
import org.exoplatform.services.communication.sms.common.*;
import org.exoplatform.services.communication.sms.encoder.MessageFormat;
import org.exoplatform.services.communication.sms.mock.MockSmsService;
import org.exoplatform.services.communication.sms.model.*;
import org.exoplatform.services.communication.sms.model.RecipientImpl;
import org.exoplatform.services.communication.sms.provider.Provider;
import org.exoplatform.services.communication.sms.provider.Sender;
import org.exoplatform.services.communication.sms.util.SmsUtil;
import org.exoplatform.services.log.LogService;
import org.exoplatform.test.BasicTestCase;


/**
 * @author: Ove Ranheim
 * @email: oranheim@users.sourceforge.net
 */
public class TestSmsServiceCase extends BasicTestCase {

    static private String USERNAME = "uid";
    static private String PASSWORD = "pwd";
    
    static protected Log log_;
    static protected SmsService service_;
    static protected SmsMonitorService monitor_;
    static protected Messages messages_;

    public TestSmsServiceCase(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        setTestNumber(1);
        if (service_ == null) {
            PortalContainer manager = PortalContainer.getInstance();
            service_ = (SmsService) manager.getComponentInstanceOfType(MockSmsService.class);
            monitor_ = (SmsMonitorService) manager.getComponentInstanceOfType(SmsMonitorService.class);
            LogService logservice = (LogService) manager.getComponentInstanceOfType(LogService.class);
            log_ = logservice.getLog("org.exoplatform.services.communication.sms.test");
            messages_ = createTestMessage();
        }
    }
    
    protected void tearDown() throws Exception {
        log_ = null;
        service_ = null;
        monitor_ = null;
        messages_ = null;
    }
    
    private Messages createTestMessage() {
        RecipientImpl._identity = new Integer(0); 
        messages_ = service_.createMessages();

        Message message = messages_.addMessage();
        message.setFrom("123456789");
        message.addRecipient(service_.createRecipient("123456789"));
        message.addRecipient("987654321");
        message.setFormat(MessageFormat.PLAIN_TEXT);
        message.setContent("Plain text message");

        message = messages_.addMessage();
        message.setFrom("123456789");
        message.addRecipient("2002");
        message.setFormat(MessageFormat.UNICODE_TEXT);
        message.setContent("Soci�t� � Responsabilit� Limit�e");
        return messages_;
    }

    public void testNumberFormat() {
        String token1 = "+33-11-44-55-66";
        String token2 = "0047-227.79.933";
        String token3 = "0047-227.79a933";
        
        token1 = SmsUtil.prepareCellularNumber(token1);
        assertEquals(token1, "3311445566");
        
        token2 = SmsUtil.prepareCellularNumber(token2);
        assertEquals(token2, "4722779933");
        
        boolean isAlpha = SmsUtil.isAlpha(token3);
        assertEquals(isAlpha, true);
    }
    
    public void testInitialDraft() {
        Provider provider = service_.createProdatProvider(USERNAME, PASSWORD);
        Sender sender = service_.createSender(provider);
        assertEquals( messages_.getLogonStatus(), LogonStatus.NONE );
        
        Message m1 = messages_.getMessage(0);
        Message m2 = messages_.getMessage(1);
        
        assertEquals( m1.getFormat(), MessageFormat.PLAIN_TEXT );
        assertEquals( m2.getFormat(), MessageFormat.UNICODE_TEXT );
        
        Recipient r1 = (Recipient) m1.getRecipients().get(0);
        Recipient r2 = (Recipient) m1.getRecipients().get(1);
        Recipient r3 = (Recipient) m2.getRecipients().get(0);
        
        log_.info(r1.getStatus());
        assertEquals( r1.getStatus(), MessageStatus.DRAFT );
        assertEquals( r2.getStatus(), MessageStatus.DRAFT );
        assertEquals( r3.getStatus(), MessageStatus.DRAFT );
    }


    public void testPreparePending() {
        try {
            Provider provider = service_.createProdatProvider(USERNAME, PASSWORD);
            Sender sender = service_.createSender(provider);
            sender.prepare(messages_);
            assertEquals( messages_.getLogonStatus(), LogonStatus.NONE );
            
            Message m1 = messages_.getMessage(0);
            Message m2 = messages_.getMessage(1);
            
            assertEquals( m1.getFormat(), MessageFormat.PLAIN_TEXT );
            assertEquals( m2.getFormat(), MessageFormat.UNICODE_TEXT );
            
            Recipient r1 = (Recipient) m1.getRecipients().get(0);
            Recipient r2 = (Recipient) m1.getRecipients().get(1);
            Recipient r3 = (Recipient) m2.getRecipients().get(0);
            
            log_.info(r1.getStatus());
            assertEquals( r1.getStatus(), MessageStatus.PENDING );
            assertEquals( r2.getStatus(), MessageStatus.PENDING );
            assertEquals( r3.getStatus(), MessageStatus.PENDING );
            
        } catch (RequestException re) {
            re.printStackTrace();
        } catch (ConvertException e) {
            e.printStackTrace();
        }
    }


    public void testPerformSending() {
        try {
            Provider provider = service_.createProdatProvider(USERNAME, PASSWORD);
            Sender sender = service_.createSender(provider);
            sender.prepare(messages_);
            sender.send();
            assertEquals( messages_.getLogonStatus(), LogonStatus.SUCCESS );
            
            Message m1 = messages_.getMessage(0);
            Message m2 = messages_.getMessage(1);
            
            assertEquals( m1.getFormat(), MessageFormat.PLAIN_TEXT );
            assertEquals( m2.getFormat(), MessageFormat.UNICODE_TEXT );
            
            Recipient r1 = (Recipient) m1.getRecipients().get(0);
            Recipient r2 = (Recipient) m1.getRecipients().get(1);
            Recipient r3 = (Recipient) m2.getRecipients().get(0);
            
            log_.info(r1.getStatus());
            assertEquals( r1.getStatus(), MessageStatus.COMPLETE );
            assertEquals( r2.getStatus(), MessageStatus.COMPLETE );
            assertEquals( r3.getStatus(), MessageStatus.FAILURE );
            
            if (messages_.hasErrorOccured())
                log_.info("Recipient 1 in Message 2 failed due to: " + r3.getError());
            
        } catch (RequestException re) {
            re.printStackTrace();
        } catch (ConvertException e) {
            e.printStackTrace();
        } catch (CommunicationError e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
    }
    
    public void testMonitorService() {
        if (this.getTestNumber() == 1) {
	        assertEquals( monitor_.getCountMessages(), new Integer(3) );
	        assertEquals( monitor_.getSuccessfullMessages(), new Integer(2) );
	        assertEquals( monitor_.getFailedMessages(), new Integer(1) );
        }
    }

}