/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.wiki.impl;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 19, 2004
 * @version $Id$
 */
public class LinkTokenHandler extends TokenHandler {
  
  public Token handleToken(Token parent, Token token, ParsingContext context)  {
    int index = -1; 
    for(int i = token.start + 1; i < token.end; i++) {
      if(context.buf_[i] == '>') {
        index = i ;
        break ;
      }
    }
    String text  = null , link = null ;
    if(index > token.start + 1) {
      text = context.getSubstring(token.start + 1, index -1) ;
      link = context.getSubstring(index + 1, token.end - 1) ;
    } else {
      link = context.getSubstring(token.start + 1, token.end - 1) ;
      text = link ;
    }
    StringBuffer b = context.getOutputBuffer() ;
    b.append("<a href='");
    if(!link.startsWith("http://")) b.append("http://") ;   
    b.append(link); 
    b.append("'>").append(text).append("</a>") ;
    return context.nextToken(token) ;
  }
  
  public String[] getHandleableTokenType() { return new String[]{Token.LINK_TOKEN} ;}
}