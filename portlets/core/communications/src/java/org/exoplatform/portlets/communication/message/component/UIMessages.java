/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import java.util.List; 
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.model.PageListDataHandler;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.message.*;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIMessages.java,v 1.3 2004/11/03 04:24:54 tuan08 Exp $
 */
public class UIMessages extends UIExoCommand {
  public final static String VIEW_MESSAGE_ACTION = "viewMessage" ;
  public final static String MESSAGE_ID = "messageId" ;
  public static Parameter viewMessageParam_ =  new Parameter(ACTION, VIEW_MESSAGE_ACTION) ;
  
  private Folder folder_ ;
  private UIPageListIterator uiPageIterator_ ;
  private MessageService service_ ;
  
  public UIMessages(MessageService service) {
  	setId("UIMessageFolder") ;
    setClazz("UIMessageFolder");
    setRendererType("MessagesRenderer");
    service_ = service ;
    uiPageIterator_ = new UIPageListIterator(new PageListDataHandler()) ;
    List children = getChildren() ;
    children.add(uiPageIterator_);
    addActionListener(ViewMessageActionListener.class, VIEW_MESSAGE_ACTION) ;
  }
  
  public void changeFolder(Folder folder) throws Exception {
    folder_ = folder ;
    uiPageIterator_.setPageList(service_.getMessages(folder)) ;
  }
  
  public void update() throws Exception {
    uiPageIterator_.setPageList(service_.getMessages(folder_)) ;
  }
  
  public List getMessages() { return uiPageIterator_.getObjectInCurrentPage() ; }
  
  public Folder getFolder() { return folder_ ; }
  
  static public class ViewMessageActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIMessages uiMessages =  (UIMessages) event.getSource();
      String messageId = event.getParameter(MESSAGE_ID) ;
      Message message = uiMessages.service_.getMessage(messageId) ;
      if(message.hasFlag(MessageHeader.RECENT_FLAG)) {
        message.removeFlag(MessageHeader.RECENT_FLAG) ;
        message.addFlag(MessageHeader.SEEN_FLAG);
        uiMessages.service_.updateMessage(message);
      }
      UIAccount uiAccount = (UIAccount) uiMessages.getAncestorOfType(UIAccount.class) ;
      UIViewMessage uiView = (UIViewMessage) uiAccount.getChildComponentOfType(UIViewMessage.class) ;
      uiView.setMessage(message) ;
      uiAccount.setRenderedComponent(UIViewMessage.class) ;
    }
  } 
  
  public String getFamily() { return "org.exoplatform.portlets.communication.message.component.UIMessages" ; }
  
}