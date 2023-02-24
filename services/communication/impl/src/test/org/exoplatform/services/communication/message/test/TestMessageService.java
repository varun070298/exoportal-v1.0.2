/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.test;

import java.util.List;
import java.util.Iterator;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.communication.message.*;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.test.BasicTestCase;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @author Benjamin Mestrallet (benjamin.mestrallet@exoplatform.com)
 * @since Aug 28, 2004
 * @version $Id: TestMessageService.java,v 1.16 2004/11/03 04:24:57 tuan08 Exp $
 */
public class TestMessageService extends BasicTestCase {
  static private String EMAIL = "exo.platform@laposte.net" ;
  static protected MessageService service_ ;

  public TestMessageService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    if (service_ == null) {
      PortalContainer pcontainer  = PortalContainer.getInstance();
      service_ =
        (MessageService) pcontainer.getComponentInstanceOfType(MessageService.class) ;
    }
  }

  public void tearDown() throws Exception {
    PortalContainer manager  = PortalContainer.getInstance();
    HibernateService hservice =
        (HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
    hservice.closeSession();
  }

  public void testAccountOperations() throws Exception {
    //test creates accounts
    Account account = createAccount("accountName", "userName");
    service_.createAccount(account);
    //test getAccounts by user name
    List accounts = service_.getAccounts("userName");
    assertEquals(1, accounts.size());
    for (Iterator iterator = accounts.iterator(); iterator.hasNext();) {
      Account account1 = (Account) iterator.next();
      assertTrue(account1.getProperty(Account.SERVER_SETTING_HOSTNAME) != null) ;
      assertSame(account, account1);
    }
    //test remove account 
    service_.removeAccount((Account) accounts.get(0));
    accounts = service_.getAccounts("userName");
    assertTrue(accounts.isEmpty());
    List folders = service_.getFolders(account.getId());
    assertTrue(folders.isEmpty());
    //test multiple accounts per user
    account = createAccount("accountName", "userName");
    service_.createAccount(account);
    account = createAccount("accountName2", "userName");
    service_.createAccount(account);
    account = createAccount("accountName3", "userName");
    service_.createAccount(account);
    accounts = service_.getAccounts("userName");
    assertEquals(3, accounts.size());
    service_.removeAccount((Account) accounts.get(0));
    service_.removeAccount((Account) accounts.get(1));
    service_.removeAccount((Account) accounts.get(2));
    accounts = service_.getAccounts("userName");
    assertTrue(accounts.isEmpty());
  }

  public void testFolderOperations() throws Exception {
    Account account = createAccount("accountName", "userName");
    service_.createAccount(account);
    // test create new folder
    Folder folder = createFolder("folderName");
    service_.createFolder(account, folder);
    assertSame(folder, service_.getFolder(account, "folderName"));
    //test remove folder
    service_.removeFolder(service_.getFolder(account, "folderName"));
    List folders = service_.getFolders(account.getId()) ;
    assertEquals("Expect 0 folder in the account", 4, folders.size()) ;
    //Test multiple folders
    Folder folder1 = createFolder("folderName");
    service_.createFolder(account, folder1);
    Folder folder2 = createFolder("folderName2");
    service_.createFolder(account, folder2);
    Folder folder3 = createFolder("folderName3");
    service_.createFolder(account, folder3);

    folders = service_.getFolders(account.getId());
    assertEquals("Expect 7 folder in the account", 7, folders.size());
    service_.removeFolder(folder1);
    service_.removeFolder(folder2);
    service_.removeFolder(folder3);
    folders = service_.getFolders(account.getId());
    assertTrue(4 == folders.size());
    service_.removeAccount(account) ;
    folders = service_.getFolders(account.getId());
    assertTrue(0 == folders.size());
  }  

  
  public void testStandaloneMessageProtocolProtocolOperations() throws Exception {
    Account senderAccount = createAccount("sender", "sender");
    senderAccount.setProtocol(MessageService.STANDALONE_PROTOCOL) ;
    service_.createAccount(senderAccount);
    
    Account receiverAccount = createAccount("receiver", "receiver");
    receiverAccount.setProtocol(MessageService.STANDALONE_PROTOCOL) ;
    service_.createAccount(receiverAccount);
    
    Message m = createMessage("receiver#receiver") ;
    runMessageOperations(senderAccount, receiverAccount, m, 0) ; 
    //assertTrue(m.hasFlag(MessageHeader.ANSWERED_FLAG));
    //assertTrue(m.hasFlag(MessageHeader.RECENT_FLAG));
    //assertTrue(!m.hasFlag(MessageHeader.DELETED_FLAG));
    //assertTrue(m.isNew());    
    //m.removeFlag(MessageHeader.RECENT_FLAG);
    //m.addFlag(MessageHeader.SEEN_FLAG);
    //assertTrue(!m.hasFlag(MessageHeader.RECENT_FLAG));
    //assertTrue(!m.isNew());
    
    service_.removeAccount(senderAccount) ;
    service_.removeAccount(receiverAccount) ;
  }
  
  /*
  public void testPOP3MessageProtocolProtocolOperations() throws Exception {
    Account account = createAccount("account", "account");
    account.setProtocol(MessageService.POP3_PROTOCOL) ;
    account.setReplyToAddress("exo.platform@laposte.net") ;
    account.setProperty(Account.SERVER_SETTING_HOSTNAME, "pop.laposte.net") ;
    account.setProperty(Account.SERVER_SETTING_USERNAME, "exo.platform") ;
    account.setProperty(Account.SERVER_SETTING_PASSWORD, "exo2004") ;
    service_.createAccount(account);
    
    Message m = createMessage("exo.platform@laposte.net") ;
    runMessageOperations(account, account, m, 5000) ; 

    service_.removeAccount(account) ;
  }
  */
  /*
  public void testIMAPMessageProtocolProtocolOperations() throws Exception {
    Account account = createAccount("account", "account");
    account.setProtocol(MessageService.IMAP_PROTOCOL) ;
    account.setReplyToAddress("exo.platform@laposte.net") ;
    account.setProperty(Account.SERVER_SETTING_HOSTNAME, "imap.laposte.net") ;
    account.setProperty(Account.SERVER_SETTING_USERNAME, "exo.platform") ;
    account.setProperty(Account.SERVER_SETTING_PASSWORD, "exo2004") ;
    service_.createAccount(account);
    
    Message m = createMessage("exo.platform@laposte.net") ;
    runMessageOperations(account, account, m, 5000) ; 

    service_.removeAccount(account) ;
  }    
  */
  
  private void runMessageOperations(Account senderAccount, 
                                    Account receiverAccount, 
                                    Message m, long delay) throws Exception {
    //test send message
    service_.sendMessage(senderAccount, m) ;
    if(delay > 0)  Thread.sleep(delay) ;
    service_.synchronizeAccount(receiverAccount) ;
    Folder receiverInbox = service_.getFolder(receiverAccount, MessageService.INBOX_FOLDER);
    PageList messages = service_.getMessages(receiverInbox) ;
    if(messages.getAvailable() != 1)  {
      fail("Expect one message. Maybe there is some delay on the smpt mail " +
           "server so you do not get the message, or you have other message in or account " +
           "Number of message in your account : " + messages.getAvailable());
    }
    if(service_.countNewMessages(receiverAccount) != 1)  {
      fail("Expect one message. Maybe there is some delay on the smpt mail " +
           "server so you do not get the message, or you have other message in or account " +
           "Number of message in your account : " + messages.getAvailable());
    }
    List attachments = service_.getAttachments((Message) messages.currentPage().get(0)) ;
    //assertEquals("Expect 1 attachment", 1, attachments.size()) ;
    
    Folder senderInbox = service_.getFolder(senderAccount, MessageService.SENT_FOLDER);
    messages = service_.getMessages(senderInbox) ;
    assertEquals("Expect 1 message", 1, messages.getAvailable()) ;
    
    //test move message
    Folder senderTrash =  service_.getFolder(senderAccount, MessageService.TRASH_FOLDER);
    service_.moveMessage(senderAccount, senderTrash, (Message) messages.currentPage().get(0)) ;
    messages = service_.getMessages(senderInbox) ;
    assertEquals("Expect 0 message", 0, messages.getAvailable()) ;
    messages = service_.getMessages(senderTrash) ;
    assertEquals("Expect 1 message", 1, messages.getAvailable()) ;
    attachments = service_.getAttachments((Message) messages.currentPage().get(0)) ;
    assertEquals("Expect 1 attachment", 1, attachments.size()) ;
    
    //test remove message
    service_.removeMessage((Message) messages.currentPage().get(0)) ;
    messages = service_.getMessages(senderTrash) ;
    assertEquals("Expect 0 message", 0, messages.getAvailable()) ;
    //  make sure that the indexing thread is finished
    Thread.sleep(1000) ;
  }

  private Account createAccount(String accountName, String username){
    Account account = service_.createAccountInstance() ;
    account.setAccountName(accountName);
    account.setOwner(username) ;
    account.setProtocol("imap");
    account.setProperty(Account.SERVER_SETTING_HOSTNAME, "localhost");
    account.setProperty(Account.SERVER_SETTING_USERNAME, username);
    account.setProperty(Account.SERVER_SETTING_PASSWORD, "password");
    return account;
  }

  private Folder createFolder(String name) {
    Folder folder = service_.createFolderInstance();
    folder.setName(name);
    return folder;
  }
  
  private Message createMessage(String to) {
    Message m = service_.createMessageInstance() ;
    m.setFrom(EMAIL) ;
    m.setTo(to) ;
    m.setSubject("subject") ;
    m.setBody("Body message: This is a test") ;
    Attachment attachment = service_.createAttachment() ;
    attachment.setName("attachment") ;
    attachment.setContent("this is an attachment".getBytes()) ;
    m.addAttachment(attachment);
    m.addFlag(MessageHeader.ANSWERED_FLAG);
    m.addFlag(MessageHeader.RECENT_FLAG);
    return m ;
  }
}