/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.validator;

import org.exoplatform.test.web.*;
import com.meterware.httpunit.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExpectFormNameValidator.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class ExpectFormNameValidator implements Validator {
  private String formName_ ;
  
  public ExpectFormNameValidator(String formName) {
    formName_ = formName ;
  }
  
  public boolean validate(WebResponse response, ExoWebClient client) throws Exception {
  	WebForm form = Util.findFormWithName(response, null, formName_) ;
    if(form == null) return false ;
    return true ;
  }
  
  public String getName() { return "ExpectFormNameValidator" ; }
  
  public String getDescription()  {  
    return  "Make sure that the return xhtml  has  the  form with the name '" +  formName_  + "'" ;
  }
}