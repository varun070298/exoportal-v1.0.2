/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message;

import java.util.* ;
import org.exoplatform.commons.utils.PageList;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public interface MessageService  {
  public static String INBOX_FOLDER = "Inbox" ;
  public static String SENT_FOLDER = "Sent" ;
  public static String TRASH_FOLDER = "Trash" ;
  public static String ARCHIVED_FOLDER = "archived" ;
  
  public static String IMAP_PROTOCOL = "imap" ;
  public static String POP3_PROTOCOL = "pop3" ;
  public static String STANDALONE_PROTOCOL = "standalone" ;
  
  public Account createAccountInstance() ;
  public Account getAccount(String userName , String accountName) throws Exception ;
  public List getAccounts(String userName) throws Exception;
  public Account removeAccount(Account account) throws Exception;
  public void createAccount(Account account) throws Exception;
  public void updateAccount(Account account) throws Exception;
  public int  countNewMessages(Account account) throws Exception ;
  
  public Folder createFolderInstance() ;
  public Folder getFolder(Account account, String folderName) throws Exception ; 
  public List getFolders(String accountId) throws Exception;
  public Folder removeFolder(Folder folder) throws Exception;
  public void createFolder(Account account, Folder folder) throws Exception;
  public void updateFolder(Folder folder) throws Exception;
  
  public Message createMessageInstance() ;
  public PageList getMessages(Folder folder) throws Exception;
  public List searchMessages(Folder folder, SearchTerm term) ;
  public Message getMessage(String messageId) throws Exception;
  public Message removeMessage(Message message) throws Exception;
  public void moveMessage(Account account, Folder folder,  Message message) throws Exception  ;
  public void createMessage(Account account, Folder folder, Message message) throws Exception;
  public void updateMessage(Message message) throws Exception;
  public void sendMessage(Account account, Message message) throws Exception ;

  public Attachment createAttachment();
  public List getAttachments(Message message) throws Exception;

  public void  synchronizeAccount(Account account) throws Exception ;
  public void  addMessageProtocolPlugin(MessageProtocolPlugin plugin) ;
  public Collection  getMessageProtocolPlugins() ;
  
  public String[] getSupportedFlags() ;
}