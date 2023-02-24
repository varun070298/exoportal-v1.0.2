/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.portal;

import org.exoplatform.test.web.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 2, 2004
 * @version $Id: UserSuites.java,v 1.4 2004/10/21 15:21:50 tuan08 Exp $
 */
public class Runner  implements Runnable {
  public void run() {
//  run the admin suites 
    WebLoadRunner runner = new WebLoadRunner() ;
    runner.setDefaultURL("http://localhost:8080/portal") ;
    AdminSuites adminSuites = new AdminSuites() ;
    runner.setNumberOfWorker(1) ;
    runner.setNumberOfTask(1) ;
    runner.setValidateWebUnit(true) ;
    runner.setValidateWellFormedXhtml(false) ;
    runner.run(adminSuites) ;
    System.out.print(adminSuites.getTextSummary()) ;

    // run the user suites 
    UserSuites userSuites = new UserSuites() ;
    runner.setNumberOfWorker(1) ;
    runner.setNumberOfTask(1) ;
    runner.setValidateWebUnit(true) ;
    runner.setValidateWellFormedXhtml(false) ;
    runner.run(userSuites) ;
    System.out.print(userSuites.getTextSummary()) ;
  }
  
  static void main(args) {
    new Runner().run() ; 
  }
}
