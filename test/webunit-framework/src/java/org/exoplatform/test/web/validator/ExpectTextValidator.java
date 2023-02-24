/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.validator;

import org.exoplatform.test.web.ExoWebClient;
import com.meterware.httpunit.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExpectTextValidator.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class ExpectTextValidator implements Validator {
  private String expectText_ ;  
  public ExpectTextValidator(String text) {
    expectText_ = text ;
  }
  
  public boolean validate(WebResponse response, ExoWebClient client)  throws Exception {
    String text = response.getText() ;
    boolean b = text.indexOf(expectText_) >= 0 ;
    if(b ==false) {
    	//System.out.println(response.getText()) ;
    	System.out.println("Expect text: " +  expectText_) ;
    }
    return b ;
  }
  
  public String getName() { return "ExpectTextvalidator" ; }
  
  public String getDescription()  { 
    return "Make sure that the return xhtml  has the following text '" + expectText_  + "'" ;
  }
}