/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.cache.test;

import org.exoplatform.services.cache.*;
import org.exoplatform.services.cache.impl.*;
/*
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestSimpleCacheService.java,v 1.1.1.1 2004/03/05 21:56:52 benjmestrallet Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestSimpleCacheService extends BaseCacheTest {
  
  public TestSimpleCacheService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
		service_ = new SimpleCacheService() ;
  }

  protected String getDescription() {
    return "Test Simple cache Service" ;
  }
}
