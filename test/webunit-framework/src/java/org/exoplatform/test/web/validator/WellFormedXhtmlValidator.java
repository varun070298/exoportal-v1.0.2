/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.validator;

import java.io.* ;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.exoplatform.test.web.ExoWebClient;
import com.meterware.httpunit.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: WellFormedXhtmlValidator.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class WellFormedXhtmlValidator implements Validator {
  
  public boolean validate(WebResponse response, ExoWebClient client)  throws Exception {
    String xhtml =  response.getText();
    try {
      ByteArrayInputStream is = new ByteArrayInputStream(xhtml.getBytes()) ;
      SAXReader reader = new SAXReader();
      Document document = reader.read(is);
    } catch (Exception ex) {
     System.out.println("Validate xhtml error: " + ex.getMessage()) ;
     FileOutputStream out = new FileOutputStream("./target/output.html") ;
     out.write(xhtml.getBytes()) ;
     return false ;
    }
    return true ;
  }
  
  public String getName() { return "WellFormedXhtmlValidator" ; }
  
  public String getDescription()  { 
    return "Make sure that the return xhtml is well formed." ;
  }
}