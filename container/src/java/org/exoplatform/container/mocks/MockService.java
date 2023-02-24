/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 7, 2003
 * Time: 11:39:25 PM
 */
package org.exoplatform.container.mocks;

public class MockService {

  public MockService() {
    System.out.println("MockService constructor");
  }

  public String hello() {
    return "HELLO WORLD SERVICE";
  }

  public String getTest(){
    return "heh";
  }

}
