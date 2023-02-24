package org.exoplatform.services.task.test;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.task.TaskService;
import org.exoplatform.services.task.BaseTask;
import org.exoplatform.test.BasicTestCase;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 14 nov. 2003
 * Time: 22:31:20
 */
public class TestTaskService extends BasicTestCase {

  TaskService service_ ;

  public TestTaskService(String name) {
    super(name);
  }

	protected String getDescription() {
		return "Log service test";
	}

	public void setUp() throws Exception {
    setTestNumber(1) ;
    PortalContainer manager  = PortalContainer.getInstance();
    service_ = (TaskService) manager.getComponentInstanceOfType(TaskService.class) ;
  }

  public void tearDown() throws Exception { }

	public void testTaskService() throws Exception {
    for(int i = 0; i < 10 ; i++) {
      service_.queueTask(new MockTask("task - " + i)) ;
    }
    
    for(int i = 0; i < 5 ; i++) {
      service_.queueRepeatTask(new MockTask("repeast task - " + i)) ;
    }
    
    Thread.sleep(6500) ;
	}
  
  static class MockTask extends BaseTask {
    private String task_ ;
    
    public MockTask(String task) { task_ = task ; }
    
    public void execute() {      
      System.out.println("execute mock task : " + task_) ;
    }

    public String getName() {  return "Mock task";  }

    public String getDescription() {   return "A mock task";  }
    
  }
}