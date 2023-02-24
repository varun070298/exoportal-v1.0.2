/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.test;

import org.exoplatform.test.BasicTestCase;
import org.exoplatform.faces.core.validator.*;
import org.exoplatform.faces.core.component.UIStringInput;
/**
 * Thu, May 15, 2004 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestUIComponentFactory.java,v 1.4 2004/08/05 14:58:42 tuan08 Exp $
 * @email: tuan08@yahoo.com
 */
public class TestValidator extends BasicTestCase {
    
  public TestValidator(String name) {
    super(name);
  }

  public void setUp() throws Exception {
  	
  }
  
  public void tearDown() throws Exception {

  }
  
  public void testEmailValidator() throws Exception {
    UIStringInput uiComponent = new UIStringInput("name", "value") ;
    EmailAddressValidator validator = new EmailAddressValidator() ;
    assertTrue("valid ", validate(uiComponent, validator, "test@localhost")) ;
    assertTrue("valid ", validate(uiComponent, validator, "test@localhost.com")) ;
    assertTrue("valid ", validate(uiComponent, validator, "test.test@localhost.com")) ;
    assertTrue("valid ", validate(uiComponent, validator, "test.test@localhost.abc.com")) ;
    assertTrue("valid ", validate(uiComponent, validator, "test-test@localhost.com")) ;
    assertTrue("valid ", validate(uiComponent, validator, "test_test@localhost.com")) ;
    assertTrue("valid ", validate(uiComponent, validator, "test.test.test@localhost.com")) ;
    assertTrue("valid ", validate(uiComponent, validator, "test-test-test@localhost.com")) ;
    assertTrue("valid ", validate(uiComponent, validator, "test_test_test@localhost.com")) ;
    
    assertTrue("invalid ", !validate(uiComponent, validator, "test@localhost.")) ;
    assertTrue("invalid ", !validate(uiComponent, validator, "test__@localhost.")) ;
    assertTrue("invalid ", !validate(uiComponent, validator, "test..oan@localhost.com")) ;
    assertTrue("invalid ", !validate(uiComponent, validator, "test--oan@localhost.com")) ;
  }
  
  private boolean validate(UIStringInput input, EmailAddressValidator validator, String s ) {
    try {
      validator.validate(null, input, s);
      return true ;
    } catch (javax.faces.validator.ValidatorException ex) {
      return false ;
    }
  }
}