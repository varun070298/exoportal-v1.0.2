/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider.prodat;

import java.util.Iterator;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.communication.sms.SmsMonitorService;
import org.exoplatform.services.communication.sms.common.CommunicationError;
import org.exoplatform.services.communication.sms.common.ResponseException;
import org.exoplatform.services.communication.sms.model.*;
import org.exoplatform.services.communication.sms.provider.*;
import org.exoplatform.services.communication.sms.util.ClientHttpSocket;
import org.exoplatform.services.communication.sms.util.ClientSocket;



/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 8:09:31 PM
 */
public class ProdatMessengerImpl implements Messenger {
    
    protected Provider _provider;
    protected SmsMonitorService _monitor;
    
    
    public ProdatMessengerImpl(Provider provider) {
        _provider = provider;
        PortalContainer manager = PortalContainer.getInstance();
        _monitor = (SmsMonitorService) manager.getComponentInstanceOfType(SmsMonitorService.class);
    }
    
    protected void statusSending(Messages messages) {
        for(Iterator i = messages.iterator(); i.hasNext(); ) {
            Message m = (Message) i.next();            
            for(Iterator j = m.getRecipients().iterator(); j.hasNext(); ) {
                Recipient r = (Recipient) j.next();
                r.setStatus(MessageStatus.SENDING);
                _monitor.incCountMessages();
            }
        }
    }
    
    public void service(SmsRequest request, SmsResponse response) throws CommunicationError, ResponseException  {
        Operator operator = _provider.getOperator();
        SmsMethod method = operator.getMethod();
        String host = operator.getHost();
        int port = Integer.parseInt(operator.getPort());
        String xml = null;
        
        if (method.equals(SmsMethod.SOCKET_CLIENT)) {
	        ClientSocket socket = new ClientSocket(host, port);
	        statusSending(request.getMessages());
	        xml = socket.sendMessage(request.getPayload());
        } else if (method.equals(SmsMethod.HTTP_CLIENT)) {
	        ClientHttpSocket http = new ClientHttpSocket(host, port, operator.getUsername(), operator.getPassword());
	        statusSending(request.getMessages());
	        xml = http.sendMessage(request.getPayload());
        }
        
        if (xml == null) throw new CommunicationError("Unknown method to service in prodat messenger");

        response.setResult(xml);
        response.translate();
    }
    
}
