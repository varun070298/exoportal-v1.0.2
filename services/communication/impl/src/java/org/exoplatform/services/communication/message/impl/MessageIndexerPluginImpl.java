/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import org.apache.lucene.document.*;
import org.apache.lucene.index.Term;
import org.exoplatform.services.communication.message.*;
import org.exoplatform.services.indexing.BaseIndexerPlugin;
import org.exoplatform.services.indexing.IndexingService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 13, 2004
 * @version $Id: MessageIndexerPluginImpl.java,v 1.5 2004/11/01 15:07:50 tuan08 Exp $
 */
public class MessageIndexerPluginImpl 
  extends BaseIndexerPlugin implements MessageIndexerPlugin {
  
  private MessageService mservice_  ;
  
  public MessageIndexerPluginImpl(IndexingService iservice, 
                                  MessageServiceImpl mservice) {
    super(iservice) ;
    mservice_ = mservice ;
    mservice.setIndexer(this) ;
  }
  
  public Document createDocument(Account account, MessageImpl message)  throws Exception {
    String identifier = message.getId() ;
    String title =  message.getSubject() ;
    if(title == null ) title = "-----------No Subject----------" ;
    String textToIndex = message.getBody();
    if(textToIndex == null) textToIndex = "" ;
    String desc =  getContentDescription(textToIndex, 200);
    Document doc = createBaseDocument(identifier, message.getFrom(), title, desc , 
                                      textToIndex, account.getAccessRole())  ;
    doc.add(Field.Text(FOLDER_ID_FIELD, message.getFolderId())) ;
    doc.add(Field.Text(ACCOUNT_ID_FIELD, account.getId())) ;
    doc.add(Field.Text(TO_FIELD, message.getTo())) ;
    doc.add(Field.Text(FROM_FIELD, message.getFrom())) ;
    doc.add(Field.Text(FLAG_FIELD, message.getFlags())) ;
    doc.add(Field.Text(RECEIVED_DATE_FIELD, DateField.dateToString(message.getReceivedDate()))) ;
    return doc ;
  }
  
  public String getPluginIdentifier() { return IDENTIFIER  ; }
  
  public Object getObject(String user,String objectId) throws Exception {
    return mservice_.getMessage(objectId) ;
  }
  
  public String getObjectAsText(String user,String objectId) throws Exception {
    Message  message = mservice_.getMessage(objectId) ;
    if(message == null)  return UNSYNCHRONIZED_DATABASE ;  
    return message.getBody()  ;
  }
  
  public String getObjectAsXHTML(String user,String objectId) throws Exception  {
    Message  message = mservice_.getMessage(objectId) ;
    if(message == null)  return UNSYNCHRONIZED_DATABASE ;  
    return message.getBody()  ;
  }
  
  public String getObjectAsXML(String user, String objectId) throws Exception  {
    Message  message = mservice_.getMessage(objectId) ;
    if(message == null)  return UNSYNCHRONIZED_DATABASE ;  
    return message.getBody()  ;
  }
  
  void removeAccount(Account account) throws Exception {
    Term term = new Term(ACCOUNT_ID_FIELD, account.getId()) ;
    iservice_.queueDeleteDocuments(term) ;
  }
  
  void removeFolder(Folder folder) throws Exception {
    Term term = new Term(FOLDER_ID_FIELD, folder.getId()) ;
    iservice_.queueDeleteDocuments(term) ;
  }
  
  void removeMessage(Message message) throws Exception {
    Term term = new Term(IndexingService.IDENTIFIER_FIELD,  message.getId()) ;
    iservice_.queueDeleteDocuments(term) ;
  }
  
  void moveMesasge(Account account, String oldFolderId, MessageImpl message) throws Exception {
    Term term = new Term(FOLDER_ID_FIELD, oldFolderId) ;
    iservice_.queueDeleteDocuments(term) ;
    iservice_.queueIndexDocument(createDocument(account, message)) ;
  }
  
  void createMesasge(Account account, MessageImpl message) throws Exception {
    iservice_.queueIndexDocument(createDocument(account, message)) ;
  }
}