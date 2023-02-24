/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import java.util.*;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.exoplatform.commons.exception.ExoMessageException;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.communication.message.*;
import org.exoplatform.services.database.DBObjectPageList;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.LogService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @author Benjamin Mestrallet (benjamin.mestrallet@exoplatform.com)
 * @since Aug 28, 2004
 * @version $Id: MessageServiceImpl.java,v 1.17 2004/11/03 04:24:56 tuan08 Exp $
 */
public class MessageServiceImpl implements MessageService {
  private static String[]  MAPPING =
  {
    "org/exoplatform/services/communication/message/impl/AccountImpl.hbm.xml" ,
    "org/exoplatform/services/communication/message/impl/FolderImpl.hbm.xml" ,
    "org/exoplatform/services/communication/message/impl/MessageHeaderImpl.hbm.xml" ,
    "org/exoplatform/services/communication/message/impl/MessageImpl.hbm.xml" ,
    "org/exoplatform/services/communication/message/impl/AttachmentImpl.hbm.xml"
  };
  
  private static final String queryAccountByName =
    "from acc in class org.exoplatform.services.communication.message.impl.AccountImpl " +
    "where acc.owner = ? and acc.accountName =  ?";

  private static final String queryFoldersByAccount =
    "from folder in class org.exoplatform.services.communication.message.impl.FolderImpl " +
    "where folder.accountId = ? order by folder.createdDate asc";

	private static final String queryFolderByName =
		"from folder in class org.exoplatform.services.communication.message.impl.FolderImpl " +
		"where folder.accountId = ? and folder.name = ?";

	private static final String queryAttachmentByName =
		"from attachment in class org.exoplatform.services.communication.message.impl.AttachmentImpl " +
		"where attachment.messageId = ? and attachment.name = ?";

  private static final String queryAttachmentsByMessage =
    "from attachment in class org.exoplatform.services.communication.message.impl.AttachmentImpl " +
    "where attachment.messageId = ?";

  private Log log_ ;
  private HibernateService hservice_ ;
  private Map plugins_ ;
  private MessageIndexerPluginImpl indexer_ ;
  private String[] supportedFlags_ = MessageHeader.SUPPORTED_FLAGS ;

  public MessageServiceImpl(HibernateService hservice,
                            LogService lservice) throws Exception {
    hservice.addMappingFiles(MAPPING) ;
    log_ =  lservice.getLog(getClass()) ;
    hservice_ = hservice ;
    plugins_ = new HashMap() ;
  }

  void setIndexer(MessageIndexerPluginImpl indexer) {
    indexer_ = indexer ;
  }
  
  public Account createAccountInstance() { return new AccountImpl() ; }
  
  public Account getAccount(String userName , String accountName) throws Exception {
  	Object[] args = new Object[]{userName, accountName};
		Type[] types = new Type[]{Hibernate.STRING, Hibernate.STRING};
		List l = hservice_.openSession().find(queryAccountByName, args, types);
     if(l.size() == 0) {
        Object[] errorargs = {accountName, userName} ;
        throw new ExoMessageException("MessageService.account-not-found", errorargs) ;
      }
    return (Account) l.get(0);
  }
  
  public List getAccounts(String userName) throws Exception {
    String queryAccountsByOwner =
      "from account in class org.exoplatform.services.communication.message.impl.AccountImpl " +
      "where account.owner = '" + userName + "'";
    Session session = hservice_.openSession();
    return session.find(queryAccountsByOwner);
  }

  public Account removeAccount(Account account) throws Exception {
    Session session = hservice_.openSession();
  	List folders = 
  	  session.find(queryFoldersByAccount, account.getId(), Hibernate.STRING);
  	for(int i = 0; i < folders.size(); i++) {
  	  FolderImpl folderImpl = (FolderImpl)folders.get(i) ;
  	  removeFolder(session, folderImpl) ;
  	}
  	session.delete(account) ;
  	session.flush() ;
    indexer_.removeAccount(account) ;
    return account ;
  }

  public void  createAccount(Account account) throws Exception {
    Session session = hservice_.openSession() ;
  	createAccount(session, account) ;
    session.flush() ;
  }
  
  public void  createAccount(Session session, Account account) throws Exception {
    AccountImpl impl = (AccountImpl) account ;
    impl.setId(IdentifierUtil.generateUUID(impl));
    session.save(impl);
    long time = System.currentTimeMillis() ;
    FolderImpl inbox = new FolderImpl(INBOX_FOLDER, INBOX_FOLDER , impl.getId()) ;
    inbox.setId(IdentifierUtil.generateUUID(inbox)) ;
    inbox.setRemoveable(false) ;
    inbox.setCreatedDate(new Date(time)) ;
    session.save(inbox) ;
    FolderImpl sent = new FolderImpl(SENT_FOLDER, SENT_FOLDER , impl.getId()) ;
    sent.setId(IdentifierUtil.generateUUID(sent)) ;
    sent.setRemoveable(false) ;
    sent.setCreatedDate(new Date(time + 1000)) ;
    session.save(sent) ;
    FolderImpl trash = new FolderImpl(TRASH_FOLDER, TRASH_FOLDER , impl.getId()) ;
    trash.setId(IdentifierUtil.generateUUID(trash)) ;
    trash.setRemoveable(false) ;
    trash.setCreatedDate(new Date(time + 2000)) ;
    session.save(trash) ;
    FolderImpl archived = new FolderImpl(ARCHIVED_FOLDER, ARCHIVED_FOLDER , impl.getId()) ;
    archived.setId(IdentifierUtil.generateUUID(archived)) ;
    archived.setRemoveable(false) ;
    archived.setCreatedDate(new Date(time + 3000)) ;
    session.save(archived) ;
  }
  
  public void updateAccount(Account account) throws Exception {
    hservice_.update(account) ;
  }

  public int  countNewMessages(Account account) throws Exception  {
    Folder folder = getFolder(account, INBOX_FOLDER) ;
    Session session = hservice_.openSession() ;
    String query = 
      "select count(m) from org.exoplatform.services.communication.message.impl.MessageImpl  m " +
      "where m.folderId = '" + folder.getId()   + "' "+
      "and   m.flags like '%"+ MessageHeader.RECENT_FLAG +"%'";
    List l  = session.find(query) ;
    Integer count = (Integer) l.get(0) ;
    return count.intValue() ;
  }
  
  public Folder createFolderInstance()  {  return new FolderImpl() ; }

  public Folder getFolder(Account account, String folderName) throws Exception {
		Object[] args = new Object[]{account.getId(), folderName};
		Type[] types = new Type[]{Hibernate.STRING, Hibernate.STRING};
		List l = hservice_.openSession().find(queryFolderByName, args, types);
		if (l.size() == 0) {
			throw new Exception("Folder is  not found");
		}
    return (Folder) l.get(0);
  }
  
  public List getFolders(String accountId) throws Exception {
  	Session session = hservice_.openSession();
  	return session.find(queryFoldersByAccount, accountId, Hibernate.STRING);
  }

  public Folder removeFolder(Folder folder) throws Exception {
    FolderImpl impl = (FolderImpl) folder ;
    if(!impl.getRemoveable()) {
      throw new Exception("Cannot remove folder: inbox, sent, trash, archive") ;
    }
  	Session session = hservice_.openSession();
    removeFolder(session, impl) ; 
    session.flush() ;
    return impl ;
  }
  
  private FolderImpl removeFolder(Session session, FolderImpl folder) throws Exception {
    String deleteMessageInFolder =
  		"from m in class org.exoplatform.services.communication.message.impl.MessageImpl " +
  		"where m.folderId = '" + folder.getId() + "'";
    String deleteAttachmentInFolder =
  		"from att in class org.exoplatform.services.communication.message.impl.AttachmentImpl " +
  		"where att.folderId = '" + folder.getId() + "'" ;
    session.delete(deleteMessageInFolder);
    session.delete(deleteAttachmentInFolder);
    session.delete(folder) ;
    indexer_.removeFolder(folder) ;
    return folder ;
  }
  
  public void createFolder(Account account, Folder folder) throws Exception {
    FolderImpl impl = (FolderImpl) folder;
    impl.setId(IdentifierUtil.generateUUID(impl));
    impl.setAccountId(account.getId());
    impl.setCreatedDate(new Date()) ;
    impl.setRemoveable(true) ;
    hservice_.create(impl);
  }

  public void updateFolder(Folder folder) throws Exception {
    hservice_.save(folder) ;
  }
  
  public Message createMessageInstance() {  return new MessageImpl() ; }

  public PageList getMessages(Folder folder) throws Exception {
    String queryMessageByFolder =
      "from message in class org.exoplatform.services.communication.message.impl.MessageImpl " +
      "where message.folderId = '" + folder.getId() + "' order by message.receivedDate desc";
    String countMessageInFolder =
      "select count(m) " +
      "from m in class org.exoplatform.services.communication.message.impl.MessageImpl " +
      "where m.folderId = '" + folder.getId() + "'";
    return new DBObjectPageList(hservice_, 15,queryMessageByFolder, countMessageInFolder ) ;
  }

  public Message getMessage(String messageId) throws Exception {
		return (Message)hservice_.findOne(MessageImpl.class, messageId) ;    
  }

  public void moveMessage(Account account, Folder folder,  Message message) throws Exception  {
    FolderImpl folderImpl = (FolderImpl) folder ;
    MessageImpl messageImpl = (MessageImpl) message ;
    String oldFolderId = messageImpl.getFolderId() ;
    messageImpl.setFolderId(folderImpl.getId()) ;
    hservice_.save(messageImpl) ;
    indexer_.moveMesasge(account, oldFolderId, messageImpl) ;
  } 
  
  public Message removeMessage(Message message) throws Exception {
    Session session = hservice_.openSession();
    session.delete(queryAttachmentsByMessage, ((MessageImpl)message).getId(), Hibernate.STRING);
    session.delete(message) ;
    session.flush() ;
    indexer_.removeMessage(message) ;
    return message;
  }

  public void createMessage(Account account, Folder folder, Message message) throws Exception {
    Session session = hservice_.openSession() ;
    MessageImpl messageImpl = (MessageImpl) message;
    FolderImpl folderImpl = (FolderImpl)folder ;
    messageImpl.setId(IdentifierUtil.generateUUID(messageImpl)) ;
    messageImpl.setFolderId(folderImpl.getId()) ;
    session.save(messageImpl) ;
    List attachments = messageImpl.getAttachment() ;
    if(attachments != null) {
      for(int i = 0; i < attachments.size(); i++) {
        AttachmentImpl attachment = (AttachmentImpl) attachments.get(i) ;
        attachment.setMessageId(messageImpl.getId()) ;
        attachment.setFolderId(messageImpl.getFolderId()) ;
        attachment.setId(IdentifierUtil.generateUUID(attachment)) ;
        session.save(attachment) ;
      }
    }
    session.flush() ;
    indexer_.createMesasge(account, messageImpl) ;
  }
  
  public void updateMessage(Message message) throws Exception {
    hservice_.update(message) ;
  }
  
  public List  searchMessages(Folder folder, SearchTerm term)  {
    return null ;
  }
  
  public void sendMessage(Account account, Message message) throws Exception {
    MessageProtocolPlugin plugin = 
      (MessageProtocolPlugin)plugins_.get(account.getProtocol()) ;
    plugin.sendMessage(account, message) ;
  }

  public Attachment createAttachment() {  return new AttachmentImpl(); }

  public List getAttachments(Message message) throws Exception {
  	Session session = hservice_.openSession();
  	return session.find(queryAttachmentsByMessage, ((MessageImpl)message).getId(), Hibernate.STRING);
  }

  public void  synchronizeAccount(Account account) throws Exception {
    MessageProtocolPlugin plugin = 
      (MessageProtocolPlugin)plugins_.get(account.getProtocol()) ;
    plugin.synchronize(account) ;
  }
  
  public synchronized void  addMessageProtocolPlugin(MessageProtocolPlugin plugin) {
    plugins_.put(plugin.getProtocol(), plugin) ;
  }
  
  public Collection  getMessageProtocolPlugins() { return plugins_.values() ; }
  
  public String[] getSupportedFlags() {  return supportedFlags_ ; }
}
