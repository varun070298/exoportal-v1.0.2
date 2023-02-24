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
 * @version: $Id: ExpectLinkWithURLValidator.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class ExpectLinkWithURLValidator implements Validator {
	 private String partOfURL_ ;
	 private int numberOfLink_ = -1;
	 
	 public ExpectLinkWithURLValidator(String text, int numberOfLink) {
    partOfURL_ = text ;
    numberOfLink_ = numberOfLink ;
   }
	 
	 public ExpectLinkWithURLValidator(String text) {
	    partOfURL_ = text ;
	 }
	  
	 public boolean validate(WebResponse response, ExoWebClient client) throws Exception {
	 	 WebLink[] links = Util.findLinksWithURL(response,null, partOfURL_) ;
	 	 if(numberOfLink_ == -1) {
	 	 	return  links.length > 0;
	 	 }
	   return links.length == numberOfLink_ ;
	 }
   
	 public String getName() { return "ExpectLinkWithURLValidator" ; }
	 
	 public String getDescription()  { 
	   return "Make sure that the return xhtml  has the link with url '..." + partOfURL_  + "...'" ;
	 }
}