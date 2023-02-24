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
 * @version: $Id: ExpectNoTextValidator.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class ExpectNoTextValidator implements Validator {
  private String expectText_ ;  
  public ExpectNoTextValidator(String text) {
    expectText_ = text ;
  }
  
  public boolean validate(WebResponse response, ExoWebClient client)  throws Exception {
    String text = response.getText() ;
    boolean b = text.indexOf(expectText_) < 0 ;
    return b ;
  }
  
  public String getName() { return "ExpectNoTextvalidator" ; }
  
  public String getDescription()  { 
    return "Make sure that the return xhtml  do not have  the following text '" + expectText_  + "'" ;
  }
}