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
 * @version: $Id: ExpectBlockValidator.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class ExpectBlockValidator implements Validator {
	static private WebTable[] EMPTY = new WebTable[0] ;
  private String blockId_ ;  
  private int numberOfBlock_ = -1 ;
  
  public ExpectBlockValidator(String text) {
    blockId_ = text ;
  }
  
  public ExpectBlockValidator(String text, int num) {
    blockId_ = text ;
    numberOfBlock_ = num ;
  }
  
  
  public boolean validate(WebResponse response, ExoWebClient client)  throws Exception {
  	WebTable[] tables = response.getMatchingTables(WebTable.MATCH_ID, blockId_) ;
  	if(tables == null) tables = EMPTY ;
  	if(numberOfBlock_ == -1) {
  		return tables.length > 0 ;
  	}
  	return tables.length == numberOfBlock_ ;
  }
  
  public String getName() { return "ExpectBlockValidator" ; }
  
  public String getDescription()  {  
    return  "Make sure that the return xhtml  has  a block(it can be a div, table.. block) with the id '" +  blockId_  + "'" ;
  }
}