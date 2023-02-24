/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider.prodat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.communication.sms.SmsMonitorService;
import org.exoplatform.services.communication.sms.common.ResponseException;
import org.exoplatform.services.communication.sms.model.*;
import org.exoplatform.services.communication.sms.provider.Provider;
import org.exoplatform.services.communication.sms.provider.SmsResponse;
import org.exoplatform.services.xml.querying.InvalidSourceException;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.QueryRunTimeException;
import org.exoplatform.services.xml.querying.Statement;
import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLQueryingService;
import org.exoplatform.services.xml.querying.impl.xtas.Query;
import org.exoplatform.services.xml.querying.impl.xtas.WellFormedUniFormTree;
import org.exoplatform.services.xml.querying.impl.xtas.XMLQueryingServiceImpl;
import org.xml.sax.InputSource;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 9:40:10 PM
 */
public class ProdatResponseImpl implements SmsResponse {

    private SmsMonitorService _monitor;
    private Provider _provider;
    private Messages _messages;
    private String _payload;    

    public ProdatResponseImpl(Provider provider) {
        _provider = provider;
        PortalContainer manager = PortalContainer.getInstance();
        _monitor = (SmsMonitorService) manager.getComponentInstanceOfType(SmsMonitorService.class);
    }

    /*
     * @see org.exoplatform.services.communication.sms.provider.SmsResponse#getMessages()
     */
    public Messages getMessages() {
        return _messages;
    }

    /*
     * @see org.exoplatform.services.communication.sms.provider.SmsResponse#setMessages(org.exoplatform.services.communication.sms.model.Messages)
     */
    public void setMessages(Messages messages) {
        _messages = messages;
    }

    /*
     * @see org.exoplatform.services.communication.sms.provider.SmsResponse#getResult()
     */
    public String getResult() {
        return _payload;
    }

    /*
     * @see org.exoplatform.services.communication.sms.provider.SmsResponse#setResult(java.lang.String)
     */
    public void setResult(String payload) {
        _payload = payload;
    }
    
    private void beautifyResponse() {
        // This eliminates the exception "Content not allowed in prolog"
        _payload = _payload.replaceAll("\\?>", "?>\n");
        _payload = _payload.replaceAll("><", ">\n<");
        _payload = _payload.substring(0, _payload.length() - 1);
    }

    private String select(XMLQueryingService service, Query query, String xpath) throws InvalidStatementException, QueryRunTimeException, InvalidSourceException {
        Statement qc = service.createStatementHelper().select(xpath);
        query.prepare(qc);
        query.execute();
        return query.getResult().toString();        
    }
    
    /*
     * @see org.exoplatform.services.communication.sms.provider.SmsResponse#translate()
     */
    public void translate() throws ResponseException {
        try {
            PortalContainer manager = PortalContainer.getInstance();
            XMLQueryingService service = (XMLQueryingService) manager.getComponentInstanceOfType(XMLQueryingServiceImpl.class);
            Query query = (Query) service.createQuery();
            WellFormedUniFormTree wft = new WellFormedUniFormTree();
            beautifyResponse();
            InputStream is = new ByteArrayInputStream(_payload.getBytes());
            wft.init(new InputSource(is));
            query.setInput(wft);

            // Update messages head
            String logon = select(service, query, "/SESSION/LOGON/text()");
            if ("OK".equals(logon)) {
                _messages.setLogonStatus(LogonStatus.SUCCESS);
                _messages.setReason("");
            } else if ("FAIL".equals(logon)) {
                _messages.setLogonStatus(LogonStatus.FAILED);
                _messages.setReason(select(service, query, "/SESSION/REASON/text()"));
                _messages.errorOccured();                
            } else {
                throw new ResponseException("Unknown logon status received from the gateway");
            }
            if (_messages.hasErrorOccured()) return;
            
            Boolean hasErrorOccured = Boolean.FALSE;
            for(Iterator i = _messages.iterator(); i.hasNext(); ) {
                Message m = (Message) i.next();
                for(Iterator j = m.getRecipients().iterator(); j.hasNext(); ) {
                    Recipient r = (Recipient) j.next();
                    Integer id = r.getId();
                    
                    String xpath = "/SESSION/MSGLST/MSG/STATUS[../ID="+id+"]/text()";
                    String status = select(service, query, xpath);
                    if ("OK".equals(status)) {
                        r.setStatus(MessageStatus.COMPLETE);
                        _monitor.incSuccessfulMessages();
                    } else if ("FAIL".equals(status)) {
                        r.setStatus(MessageStatus.FAILURE);
                        hasErrorOccured = Boolean.TRUE;
                        _monitor.incFailedMessages();
                    }
                    
                    xpath = "/SESSION/MSGLST/MSG/INFO[../ID="+id+"]/text()";                    
                    String info = select(service, query, xpath);
                    r.setError(info);
                }
            }
            if (hasErrorOccured.booleanValue()) _messages.errorOccured();

        } catch (InvalidStatementException e) {
            throw new ResponseException(e);
        } catch (InvalidSourceException e) {
            throw new ResponseException(e);
        } catch (QueryRunTimeException e) {
            throw new ResponseException(e);
        } catch (UniFormTransformationException e) {
            throw new ResponseException(e);
        }

    }

}

