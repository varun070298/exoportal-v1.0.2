/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.converter.impl;

import org.exoplatform.services.grammar.converter.Text2HtmlConverter ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 4, 2005
 * @version $Id$
 */
public class Text2HtmlConverterImpl implements Text2HtmlConverter {

  public String toHtml(String javaCode) {
    return toHtml(javaCode.toCharArray());
  }

  public String toHtml(char[] javaCodeBuf) {
    StringBuffer b = new StringBuffer() ;
    toHtml(javaCodeBuf, b) ;
    return b.toString() ;
  }

  public void toHtml(char[] buf, StringBuffer b) {
    b.append("<div class='text'>") ;
    for(int i = 0; i < buf.length; i++) {
      switch(buf[i]) {
        case '\n' : b.append("<br/>") ; break ;
        case '\r' :  break ;
        case '&' : b.append("&amp;") ; break ;
        case '>' : b.append("&gt;") ; break ;
        case '<' : b.append("&lt;") ; break ;
        case ' ' : {
          b.append(" ") ;
          i++ ;
          while(i < buf.length && buf[i] == ' ') {
            b.append("&nbsp;") ;
            i++ ;
          }
          i-- ;
          break ;
        }
        default: b.append(buf[i]) ; break ;
      }
    }
    b.append("</div>") ;
  }
}
