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
 * @version: $Id: ExpectImageWithAltTextValidator.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class ExpectImageWithAltTextValidator implements Validator {
  private String text_ ;
  
  public ExpectImageWithAltTextValidator(String text) {
    text_ = text ;
  }
  
  public boolean validate(WebResponse response, ExoWebClient client) throws Exception {
    Object image = response.getImageWithAltText(text_) ;
    return image != null ;
  }
  
  public String getName() { return "ExpectImageWithAltTextValidator" ; }
  
  public String getDescription()  {  
    return  "Make sure that the return xhtml  has  the  image with alt text '" +  text_  + "'" ;
  }
}