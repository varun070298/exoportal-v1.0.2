/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.jvm.component;

import java.lang.management.* ;
import org.exoplatform.container.RootContainer ;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.text.template.ListBeanDataHandler;
import org.exoplatform.text.template.DataHandler;
import org.exoplatform.text.template.xhtml.* ;
/**
 * May 31, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UIProcessInfo extends UIExoCommand {
  static private String   SHOW_PROCESS_INFO_ACTION = "showProcessInfo" ;
  static private String   LIST_PROCESS_INFO_ACTION = "listProcessInfo" ;
  
  static private Element  PROCESS_INFO_TEMPLATE  = 
    new Div().
      add(new Properties().
          add("#{UIProcesseInfo.label.thread-name}", "${threadName}").
          add("#{UIProcesseInfo.header.thread-id}", "${threadId}").
          
          add("#{UIProcesseInfo.header.thread-state}", "${threadState}").
          add("#{UIProcesseInfo.header.is-native}", "${isInNative()}").
          add("#{UIProcesseInfo.header.is-suspended}", "${isSuspended()}").
          
          add("#{UIProcesseInfo.header.blocked-count}", "${blockedCount}").
          add("#{UIProcesseInfo.header.blocked-time}", "${blockedTime}").
          add("#{UIProcesseInfo.header.waited-count}", "${waitedCount}").
          add("#{UIProcesseInfo.header.waited-time}", "${waitedTime}").
          
          add("#{UIProcesseInfo.header.lock-name}", "${lockName}").
          add("#{UIProcesseInfo.header.lock-owner-id}", "${lockOwnerId}").
          add("#{UIProcesseInfo.header.lock-owner-name}", "${lockOwnerName}")).
      add(new Button("#{UIProcesseInfo.button.cancel}").
          addParameter(ACTION, LIST_PROCESS_INFO_ACTION)).
      optimize();
  
  static private Element  PROCESSES_SUMMARY_TEMPLATE = 
    new Table().setCssClass("UIGrid").
      add(new Rows().setShowHeader(true).
          add(new LinkColumn("#{UIProcesseInfo.label.thread-name}", "${threadName}", "${threadId}").
              addParameter(ACTION, SHOW_PROCESS_INFO_ACTION)).
          add(new Column("#{UIProcesseInfo.header.thread-id}", "${threadId}")).
          add(new Column("#{UIProcesseInfo.header.thread-state}", "${threadState}")).
          add(new Column("#{UIProcesseInfo.header.is-native}", "${isInNative()}")).
          add(new Column("#{UIProcesseInfo.header.is-suspended}", "${isSuspended()}"))).
      optimize() ;
  
  private ListBeanDataHandler threadInfo_ ;
  private Element template_ = PROCESSES_SUMMARY_TEMPLATE ;
  
	public UIProcessInfo() {
		setRendererType("TemplateRenderer") ;
    threadInfo_ = new ListBeanDataHandler(ThreadInfo.class);
    ThreadMXBean threadMXBean = 
      (ThreadMXBean)RootContainer.getComponent(ThreadMXBean.class) ;
    threadInfo_.setBeans(threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds())) ;
    addActionListener(ShowThreadInfoActionListener.class, SHOW_PROCESS_INFO_ACTION) ;
    addActionListener(ListThreadInfoActionListener.class, LIST_PROCESS_INFO_ACTION) ;
	}
  
	public DataHandler getDataHandler(Class type) {
	  return threadInfo_ ;
	}
	
  public Element getTemplate() { return template_ ; }
  
  static public class ShowThreadInfoActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIProcessInfo uicomp = (UIProcessInfo) event.getComponent() ;
      String id = event.getParameter(OBJECTID) ;
      long threadId = Long.parseLong(id) ;
      ThreadMXBean threadMXBean = 
        (ThreadMXBean)RootContainer.getComponent(ThreadMXBean.class) ;
      ThreadInfo tinfo = threadMXBean.getThreadInfo(threadId) ;
      uicomp.threadInfo_.setBean(tinfo) ;
      uicomp.template_ = PROCESS_INFO_TEMPLATE ;
    }
  }
  
  static public class ListThreadInfoActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIProcessInfo uicomp = (UIProcessInfo) event.getComponent() ;
      ThreadMXBean threadMXBean = 
        (ThreadMXBean)RootContainer.getComponent(ThreadMXBean.class) ;
      uicomp.threadInfo_.setBean(threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds())) ;
      uicomp.template_ = PROCESSES_SUMMARY_TEMPLATE;
    }
  }
}