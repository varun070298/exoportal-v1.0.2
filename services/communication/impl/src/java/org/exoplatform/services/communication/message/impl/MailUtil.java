/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import javax.mail.*;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import org.exoplatform.services.communication.message.MessageHeader;



/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 11, 2004
 * @version $Id: MailUtil.java,v 1.4 2004/10/14 23:29:22 tuan08 Exp $
 */
public class MailUtil {
  static public boolean hasSystemFlag(Flags flags, Flags.Flag flag) {
    Flags.Flag[] sf = flags.getSystemFlags();
    for (int i = 0; i < sf.length; i++) {
      if(sf[i] == flag) return true ;
    }
    return false ; 
  }
  
  static public boolean hasUserFlag(Flags flags, String flag) {
    String[] uf = flags.getUserFlags();
    for (int i = 0; i < uf.length; i++) {
      if(uf[i].equals(flag)) return true ;
    }
    return false ; 
  }
  
  static public String convertAddressToString(Address[] addrs) {
    if (addrs == null || addrs.length == 0)
      return "Not Available <root@localhost>";
    StringBuffer sb = new StringBuffer();
    int last = addrs.length - 1;
    for (int i = 0; i < addrs.length; i++) {
      InternetAddress addr = (InternetAddress) addrs[i];
      String name = addr.getPersonal();
      String email = addr.getAddress();
      if (name == null)
        name = email;
      sb.append(name);
      sb.append(" <").append(email).append(">");
      if (i != last) {
        sb.append(',');
      }
    }
    return sb.toString();
  }
  
  static public org.exoplatform.services.communication.message.Message createMessage(Message msg) throws Exception {
    org.exoplatform.services.communication.message.Message message = 
      new org.exoplatform.services.communication.message.impl.MessageImpl() ; 
    message.setFrom(convertAddressToString(msg.getFrom()));
    message.setSubject(msg.getSubject());
    java.util.Date rdate = msg.getReceivedDate();
    if (rdate == null) rdate = msg.getSentDate();
    message.setReceivedDate(rdate) ;
    message.setTo(convertAddressToString(msg.getRecipients(Message.RecipientType.TO)));
    message.setCC(convertAddressToString(msg.getRecipients(Message.RecipientType.CC)));
    
    setMessageFlags(message, msg.getFlags());
    Object body = msg.getContent();
    String bodyStr = null ;
    if (body instanceof String) {
      bodyStr = (String) body;
    } else if (body instanceof Multipart) {
      Multipart multipart = (Multipart) body;
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < multipart.getCount(); i++) {
        BodyPart bodypart = multipart.getBodyPart(i);
        Object content = bodypart.getContent();
        if (content instanceof String) {
          sb.append('\n').append((String) content);
        }
        
      }
      bodyStr = sb.toString();
    }
    message.setBody(bodyStr) ;
    return message ;
  }
 
  private static void setMessageFlags(org.exoplatform.services.communication.message.Message message, Flags flags) {
     Flag[] flagsArray = flags.getSystemFlags();
     for (int i = 0; i < flagsArray.length; i++) {
      Flag flag = flagsArray[i];
      if(Flags.Flag.ANSWERED == flag) {
        message.addFlag(MessageHeader.ANSWERED_FLAG);
      } else if(Flags.Flag.DELETED == flag) {
        message.addFlag(MessageHeader.DELETED_FLAG);
      }else  if(Flags.Flag.DRAFT == flag) {
        message.addFlag(MessageHeader.DRAFT_FLAG);
      }else if(Flags.Flag.FLAGGED == flag) {
        message.addFlag(MessageHeader.FLAGGED_FLAG);
      } else  if(Flags.Flag.RECENT == flag) {
        message.addFlag(MessageHeader.RECENT_FLAG);
      } else  if(Flags.Flag.SEEN == flag) {
        message.addFlag(MessageHeader.SEEN_FLAG);
      }else  if(Flags.Flag.USER == flag) {
        message.addFlag(MessageHeader.USER_FLAG);
      }             
    }
    String[] userFlags = flags.getUserFlags();
    for (int i = 0; i < userFlags.length; i++) {
      message.addFlag(userFlags[i]);
    }
  }


  static public void printFolderSupportFlag(Folder folder) {
    Flags supportFlags = folder.getPermanentFlags() ;
    Flags.Flag[] sf = supportFlags.getSystemFlags();
    for (int i = 0; i < sf.length; i++) {
      if(Flags.Flag.ANSWERED == sf[i]) {
        System.out.println("flag answer") ;
      } else if(Flags.Flag.DELETED == sf[i]) {
        System.out.println("flag delete") ;
      }else  if(Flags.Flag.DRAFT == sf[i]) {
        System.out.println("flag draft") ;
      }else if(Flags.Flag.FLAGGED == sf[i]) {
        System.out.println("flag flag") ;
      } else  if(Flags.Flag.RECENT == sf[i]) {
        System.out.println("flag recent") ;
      } else  if(Flags.Flag.SEEN == sf[i]) {
        System.out.println("flag seen") ;
      }else  if(Flags.Flag.USER == sf[i]) {
        System.out.println("flag user") ;
      } else {
        System.out.println("") ;
      }
    }
  }
}