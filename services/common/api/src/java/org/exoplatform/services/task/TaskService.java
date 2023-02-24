/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.task;

import java.util.Collection ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 30, 2004
 * @version $Id$
 */
public interface TaskService {
  public void queueTask(Task task) ;
  public Collection getTasks() ;
  
  public void queueRepeatTask(Task task) ;
  public void removeRepeatTask(Task task) ;
  public Collection getRepeatTasks() ;
  
}