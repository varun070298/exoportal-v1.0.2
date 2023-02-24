/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum;

import java.util.List;
import org.exoplatform.services.database.XResources;
import org.picocontainer.Startable ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 25, 2004
 * @version $Id$
 */
public class ForumEventListener implements Startable {
  
  public ForumEventListener(ForumServiceContainer container) {
    container.addForumEventListener(this) ;
  }
  
  public void start() {} 
  public void stop() {} 
  
  public void onSave(XResources resources, Category category) throws Exception  {  }
  
  public void onDelete(XResources resources, Category category) throws Exception  {  }
  
  public void onSave(XResources resources, Forum forum) throws Exception  { }
  
  public void onDelete(XResources resources, Forum forum) throws Exception  {}
  
  public void onSave(XResources resources, Topic topic) throws Exception  {}
  
  public void onDelete(XResources resources, Topic topic) throws Exception  {}
  
  public void onSave(XResources resources, Post post) throws Exception  { }
  
  public void onDelete(XResources resources, Post post) throws Exception  { }
  
  public static void onSave(List listeners, XResources resources, Category category) throws Exception  {
    for(int i = 0 ; i < listeners.size(); i++) {
      ForumEventListener listener = (ForumEventListener) listeners.get(i);
      listener.onSave(resources, category) ;
    }
  }
  
  public static void onDelete(List listeners, XResources resources, Category category) throws Exception  {
    for(int i = 0 ; i < listeners.size(); i++) {
      ForumEventListener listener = (ForumEventListener) listeners.get(i);
      listener.onDelete(resources, category) ;
    }
  }
  
  public static void onSave(List listeners, XResources resources, Forum forum) throws Exception  {
    for(int i = 0 ; i < listeners.size(); i++) {
      ForumEventListener listener = (ForumEventListener) listeners.get(i);
      listener.onSave(resources, forum) ;
    }
  }
  
  public static void onDelete(List listeners, XResources resources, Forum forum) throws Exception  {
    for(int i = 0 ; i < listeners.size(); i++) {
      ForumEventListener listener = (ForumEventListener) listeners.get(i);
      listener.onDelete(resources, forum) ;
    }
  }
  
  public static void onSave(List listeners, XResources resources, Topic topic) throws Exception  {
    for(int i = 0 ; i < listeners.size(); i++) {
      ForumEventListener listener = (ForumEventListener) listeners.get(i);
      listener.onSave(resources, topic) ;
    }
  }
  
  public static void onDelete(List listeners, XResources resources, Topic topic) throws Exception  {
    for(int i = 0 ; i < listeners.size(); i++) {
      ForumEventListener listener = (ForumEventListener) listeners.get(i);
      listener.onDelete(resources, topic) ;
    }
  }
  
  public static void onSave(List listeners, XResources resources, Post post) throws Exception  {
    for(int i = 0 ; i < listeners.size(); i++) {
      ForumEventListener listener = (ForumEventListener) listeners.get(i);
      listener.onSave(resources, post) ;
    }
  }
  
  public static void onDelete(List listeners, XResources resources, Post post) throws Exception  {
    for(int i = 0 ; i < listeners.size(); i++) {
      ForumEventListener listener = (ForumEventListener) listeners.get(i);
      listener.onDelete(resources, post) ;
    }
  }
}