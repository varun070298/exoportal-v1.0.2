/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import java.util.ArrayList;
import java.util.List;
import org.exoplatform.faces.core.component.UISelectBox;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener ;
import org.exoplatform.services.communication.forum.ForumService;
import org.exoplatform.services.communication.forum.Watcher ;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UICategoryForm.java,v 1.7 2004/10/17 13:46:25 tuan08 Exp $
 */
public class UIWatchForm extends UISimpleForm {
  private static List protocols ;

  static {
    protocols = new ArrayList(10) ;
    protocols.add(new SelectItem("smtp(email)", "smtp")) ;
    protocols.add(new SelectItem("sms(Not Support Yet)", "sms")) ;
  }
  
  private ForumService service_ ;
  private Cell  userName ;
  private UISelectBox protocol_ ;
  private UIStringInput  address_ ;
  private Watcher watcher_ ;
  
  public UIWatchForm(ForumService service) throws Exception {
    super("watchForm", "post", null) ;
    setId("UIWatchForm") ;
    setClazz("UIWatchForm") ;
    service_ = service ;
    address_ = new UIStringInput("address", "") ;
    protocol_ = new UISelectBox("protocol", "smtp", protocols) ;
    userName = new Cell("") ;
    add(new HeaderRow().
        add(new Cell("#{UIWatchForm.header}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIWatchForm.label.username}")).
        add(userName));
    add(new Row().
        add(new LabelCell("#{UIWatchForm.label.protocol}")).
        add(new ComponentCell(this, protocol_)));
    add(new Row().
        add(new LabelCell("#{UIWatchForm.label.address}")).
        add(new ComponentCell(this, address_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UIWatchForm.button.save}", SAVE_ACTION)).
            add(new FormButton("#{UIWatchForm.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION)  ;
    addActionListener(SaveActionListener.class, SAVE_ACTION)  ;
  }
  
  public void setWatcher(Watcher watcher) throws Exception {
    watcher_ = watcher ;
    userName.setValue(watcher.getUserName()) ;
    address_.setValue(watcher.getAddress()) ;
  }
  
  static public class CancelActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIWatchForm uiForm = (UIWatchForm) event.getComponent() ;
      uiForm.setRenderedSibling(UITopics.class) ;
		}
  }
  
  static public class SaveActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIWatchForm uiForm = (UIWatchForm) event.getComponent() ;
      uiForm.watcher_.setMessageProtocol(uiForm.protocol_.getValue()) ;
      uiForm.watcher_.setAddress(uiForm.address_.getValue()) ;
      uiForm.service_.saveWatcher(uiForm.watcher_) ;
      uiForm.setRenderedSibling(UITopics.class) ;
		}
  }
}