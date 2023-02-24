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
 * @version: $Id: ExpectLinkWithTextValidator.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class ExpectLinkWithTextValidator implements Validator {
  private String text_ ;
  private int numberOfLink_ = -1;
  
  public ExpectLinkWithTextValidator(String text, int numberOfLink) {
    text_ = text ;
    numberOfLink_ = numberOfLink ;
  }
  
  public ExpectLinkWithTextValidator(String text) {
    text_ = text ;
  }
  
  public boolean validate(WebResponse response,  ExoWebClient client) throws Exception {
  	WebLink[] links = Util.findLinksWithText(response , null,  text_) ;
  	if(numberOfLink_ == -1) {
  	 	return links.length > 0 ;
  	}
  	return links.length == numberOfLink_ ;
  }
  
  public String getName() { return "ExpectLinkWithTextValidator" ; }
   
   public String getDescription()  { 
     if(numberOfLink_ == -1) {
       return "Make sure that the return xhtml  has at least one link with text '..." +  text_  + "...'" ;
     } 
     return  "Make sure that the return xhtml  has " + numberOfLink_ + " links with text '" +  text_  + "'" ;
   }
}