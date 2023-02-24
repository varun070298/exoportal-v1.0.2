/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.text.MessageFormat;
import java.util.List;
import net.sf.hibernate.Session;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.services.communication.forum.*;
import org.exoplatform.services.communication.message.MailService;
import org.exoplatform.services.communication.message.Message;
import org.exoplatform.services.communication.message.impl.MessageImpl;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.task.BaseTask;
import org.exoplatform.services.task.TaskService ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 25, 2004
 * @version $Id$
 */
public class WatcherNotifyListener extends ForumEventListener {
  private String  forumChangeMessage_ ;
  private String  forumChangeSubject_ ;
  private String topicChangeMessage_ ;
  private String topicChangeSubject_ ;
  private String messageAuthor_ ;
  
  private MailService mservice_ ;
  private TaskService tservice_ ;
  
  public WatcherNotifyListener(ForumServiceContainer container,
                               MailService mservice,
                               TaskService tservice,
                               ConfigurationManager confService) throws Exception {
    super(container) ;
    mservice_  = mservice ;
    tservice_ = tservice ;
    ServiceConfiguration sconf = confService.getServiceConfiguration(getClass()) ;
    forumChangeSubject_ = sconf.getValueParam("forum.change.subject").getValue() ;
    forumChangeMessage_ = sconf.getValueParam("forum.change.message").getValue() ;
    topicChangeSubject_ = sconf.getValueParam("topic.change.subject").getValue() ;
    topicChangeMessage_ = sconf.getValueParam("topic.change.message").getValue() ;
    messageAuthor_ = sconf.getValueParam("message.author").getValue() ;
  }
  
  public void onSave(XResources resources, Post post) throws Exception  {
    PostImpl impl = (PostImpl) post ; 
    ObjectQuery q = new ObjectQuery(WatcherImpl.class).
    addEQ("target",  WatcherImpl.FORUM_TARGET).
    addEQ("forumId", impl.getForumId()) ;
    Session session = (Session) resources.getResource(Session.class) ;
    List watchers = session.find(q.getHibernateQuery()) ;
    if(watchers.size() == 0) return ;
    Forum forum = (Forum) resources.getResource(Forum.class) ;
    Object[] params = {forum.getForumName(), post.getSubject(), post.getMessage()} ;
    String subject = MessageFormat.format(forumChangeSubject_, params) ;
    String body  = MessageFormat.format(forumChangeMessage_, params) ;
    send(watchers, subject, body) ;
    
    q = new ObjectQuery(WatcherImpl.class).
        addEQ("target",  WatcherImpl.TOPIC_TARGET).
        addEQ("topicId", impl.getTopicId()) ;
    watchers = session.find(q.getHibernateQuery()) ;
    if(watchers.size() == 0) return ;
    Topic topic = (Topic) resources.getResource(Topic.class) ;
    Object[] topicparams = {topic.getTopic(), post.getSubject(), post.getMessage()} ;
    subject = MessageFormat.format(topicChangeSubject_, topicparams) ;
    body  = MessageFormat.format(topicChangeMessage_, topicparams) ;
    send(watchers, subject, body) ;
  }
  
  private void send(List watchers, String subject, String body) throws Exception {
    StringBuffer bcc = new StringBuffer() ;
    for(int i = 0; i < watchers.size(); i++) {
      if(i > 0) bcc.append(", ") ;
      Watcher watcher = (Watcher) watchers.get(i) ;
      bcc.append(watcher.getAddress()) ;
    }
    Message message = new MessageImpl() ;
    message.setFrom(messageAuthor_) ;
    message.setBCC(bcc.toString()) ;
    message.setSubject(subject) ;
    message.setBody(body) ;
    tservice_.queueTask(new NotifyForumWatcherTask(mservice_, message)) ;
  }
  
  static public class NotifyForumWatcherTask extends BaseTask {
    private MailService mservice_ ;
    private Message message_ ;
    
    public NotifyForumWatcherTask(MailService service, Message message) {
      mservice_ = service ;
      message_ = message ;
    }
    
    public void execute() throws Exception {
      mservice_.sendMessage(message_) ;
    }

    public String getName() { return "NotifyForumWatcherTask" ;}
    public String getDescription() { return "Send message : " + message_.getSubject(); }
  }
}