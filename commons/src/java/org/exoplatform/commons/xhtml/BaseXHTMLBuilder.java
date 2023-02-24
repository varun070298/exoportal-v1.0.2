/**************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved. *
 * Please look at license.txt in info directory for more license detail.  *
 *************************************************************************/
package org.exoplatform.commons.xhtml;

import java.io.IOException;
import java.io.Writer;
import org.exoplatform.Constants;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: HtmlBasicRenderer.java,v 1.22 2004/10/16 21:13:51 tuan08 Exp $
 */
abstract public  class BaseXHTMLBuilder  {
  protected Writer w_ ;
  
  public BaseXHTMLBuilder(Writer w) {
    w_ = w ;
  }
  
  final public BaseXHTMLBuilder text(String s) throws IOException {
    if(s != null)  w_.write(s) ;
    return this ;
  }
  
  final public BaseXHTMLBuilder write(String s) throws IOException {
    w_.write(s) ;
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder startHTML() throws IOException {
    w_.write("<html>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder closeHTML() throws IOException {
    w_.write("</html>") ;
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder startBODY() throws IOException {
    w_.write("<body>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder closeBODY() throws IOException {
    w_.write("</body>") ;
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder startTABLE() throws IOException {
    w_.write("<table>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startTABLE(String attributes) throws IOException {
    w_.write("<table "); w_.write(attributes); w_.write("'>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startTABLE(Attributes attrs) throws IOException {
    w_.write("<table"); w_.write(attrs.toString());   w_.write('>');
    return this ;
  }
  
  final public BaseXHTMLBuilder closeTABLE() throws IOException {
    w_.write("</table>") ;
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder startTR() throws IOException {
    w_.write("<tr>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startTR(String attributes) throws IOException {
    w_.write("<tr "); w_.write(attributes); w_.write("'>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startTR(Attributes attrs) throws IOException {
    w_.write("<tr");w_.write(attrs.toString()); w_.write('>');
    return this ;
  }
  
  final public BaseXHTMLBuilder closeTR() throws IOException {
    w_.write("</tr>") ;
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder startTD() throws IOException {
    w_.write("<td>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startTD(String attributes) throws IOException {
    w_.write("<td "); w_.write(attributes); w_.write("'>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startTD(Attributes attrs) throws IOException {
    w_.write("<td");w_.write(attrs.toString());    w_.write('>');
    return this ;
  }
  
  final public BaseXHTMLBuilder closeTD() throws IOException {
    w_.write("</td>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder TD(String text) throws IOException {
    w_.write("<td>") ; w_.write(text) ; w_.write("</td>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder TD(Attributes attrs, String text) throws IOException {
    w_.write("<td");w_.write(attrs.toString());  w_.write('>');  
    w_.write(text) ;  
    w_.write("</td>") ;
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder startTH() throws IOException {
    w_.write("<th>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startTH(String attributes) throws IOException {
      w_.write("<th "); w_.write(attributes); w_.write("'>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startTH(Attributes attrs) throws IOException {
    w_.write("<th");w_.write(attrs.toString());  w_.write('>');
    return this ;
  }
  
  final public BaseXHTMLBuilder closeTH() throws IOException {
    w_.write("</th>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder TH(String text) throws IOException {
    w_.write("<th>") ; w_.write(text) ; w_.write("</th>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder TH(Attributes attrs, String text) throws IOException {
    w_.write("<th");w_.write(attrs.toString()); w_.write('>'); 
    w_.write(text) ;  w_.write("</th>") ;
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder startDIV() throws IOException {
    w_.write("<div>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startDIV(String attributes) throws IOException {
    w_.write("<div "); w_.write(attributes); w_.write("'>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder startDIV(Attributes attrs) throws IOException {
    w_.write("<div"); w_.write(attrs.toString());  w_.write('>');
    return this ;
  }
  
  final public BaseXHTMLBuilder closeDIV() throws IOException {
    w_.write("</div>") ;
    return this ;
  }
  
  final public BaseXHTMLBuilder DIV(String text) throws IOException {
    w_.write("<div>") ;  w_.write(text) ;  w_.write("</div>") ;
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder p(String s) throws IOException {
    w_.write("<p>") ; w_.write(s) ; w_.write("</p>") ;
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder a(String href, String s) throws IOException {
    w_.write("<a href='"); w_.write(href); w_.write("'>") ; 
    w_.write(s) ; w_.write("</a>") ;
    return this ;
  }
  //============================================================================
  public BaseXHTMLBuilder link(String params, String label) throws IOException {
    w_.write("<a href='"); w_.write(getBaseURL());
      w_.write(Constants.AMPERSAND); w_.write(params) ; w_.write("'>") ; 
    w_.write(label) ; w_.write("</a>") ;
    return this ; 
  }
  
  public BaseXHTMLBuilder link(Parameter[] params, String label) throws IOException {
    w_.write("<a href='"); w_.write(getBaseURL());
    for(int i = 0; i < params.length; i++) {
      w_.write(Constants.AMPERSAND); w_.write(params[i].name_);w_.write('=');w_.write(params[i].value_);
    }
    w_.write("'>") ; 
    w_.write(label) ; w_.write("</a>") ;
    return this ;
  }
  
  public BaseXHTMLBuilder link(Parameters params, String label) throws IOException {
    return link(params.parameter_, label) ;
  }
  //============================================================================
  final public BaseXHTMLBuilder startFORM() throws IOException {
    w_.write("<form ") ; w_.write("action='") ; w_.write(getBaseURL()); w_.write("'") ;
    w_.write('>');  
    return this ;
  }
  
  final public BaseXHTMLBuilder startFORM(Attributes attrs) throws IOException {
    w_.write("<form") ; w_.write("action='") ; w_.write(getBaseURL()); w_.write("'") ;
    w_.write(attrs.toString());  
    w_.write('>');  
    return this ;
  }
  
  final public BaseXHTMLBuilder startFORM(String attrs) throws IOException {
    w_.write("<form ") ; w_.write("action='") ; w_.write(getBaseURL()); w_.write("' ") ;
    w_.write(attrs);  
    w_.write('>');  
    return this ;
  }
  
  final public BaseXHTMLBuilder closeFORM() throws IOException {
    w_.write("</form") ;  
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder INPUT(String attrs) throws IOException {
    w_.write("<input ") ; w_.write(attrs);  w_.write("/>");  
    return this ;
  }
  
  final public BaseXHTMLBuilder INPUT(Attributes attrs) throws IOException {
    w_.write("<input") ; w_.write(attrs.toString());  w_.write("/>");  
    return this ;
  }
  //============================================================================
  final public BaseXHTMLBuilder TEXTAREA(String attrs, String text) throws IOException {
    if(attrs == null)   w_.write("<textarea>") ;
    else { w_.write("<textarea ") ; w_.write(attrs); w_.write("'>") ; }
    w_.write(text);  
    w_.write("</textarea>");  
    return this ;
  }
  abstract protected String getBaseURL() ;
}