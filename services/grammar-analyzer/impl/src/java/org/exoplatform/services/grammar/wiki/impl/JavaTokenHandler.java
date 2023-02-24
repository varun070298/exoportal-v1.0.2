/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.wiki.impl;

import org.exoplatform.container.PortalContainer; 
import org.exoplatform.services.grammar.converter.Text2HtmlConverter ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 19, 2004
 * @version $Id$
 */
public class JavaTokenHandler extends TokenHandler {
  private Text2HtmlConverter converter_ ;
  
  public  JavaTokenHandler() {
    PortalContainer container = PortalContainer.getInstance() ;
    converter_  = 
      (Text2HtmlConverter) container.getComponentInstanceOfType(Text2HtmlConverter.class) ;
  }
  
  public Token handleToken(Token parent, Token token, ParsingContext context)  {
    int position = token.end + 1 ;
    char[] buf = context.buf_ ;
    for(; position < buf.length; position++) {
      switch(buf[position]) {
        case '{' : {
          if(position + 5 < buf.length && 
             buf[position + 1] ==  'j' && 
             buf[position + 2] ==  'a' &&
             buf[position + 3] ==  'v' &&
             buf[position + 4] ==  'a' &&
             buf[position + 5] ==  '}') {
            StringBuffer b = context.getOutputBuffer() ;
            b.append("<div class='text'>") ;
            converter_.toHtml(context.getSubBuffer(token.end + 1, position - 1), b) ;
            b.append("</div>") ;
            token.end = position + 5;
            return context.nextToken(token) ;
          }
        }
      }
    }
    context.out(token) ;
    return context.nextToken(token) ;
  }  
  
  public String[] getHandleableTokenType() { return new String[]{"{java}"} ;}
}