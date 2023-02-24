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
public class AlphabeticalEnumeratedTokenHandler extends TokenHandler {
  
  public Token handleToken(Token parent, Token token, ParsingContext context)  {
    StringBuffer b = context.getOutputBuffer() ;
    TokenHandlerManager manager = context.getTokenHandlerManager() ;
    b.append("<ul class='alphabetical-enumerated'>") ;
    boolean closeLI = false ;
    while(token != null) {
      if(token.type == Token.ALPHABETICAL_ENUMERATED_TOKEN) {
        if(closeLI) b.append("</li>") ;
        closeLI = true ;
        b.append("<li>") ;
        token = context.nextToken(token) ;
      } else if(token.type == Token.MULTIPLE_NEW_LINE_TOKEN) {
        break ;
      } else {
        token = manager.handleToken(null ,token, context) ;
      }
    }
    b.append("</li>") ;
    b.append("</ul>") ;
    return token ;
  }
  
  public String[] getHandleableTokenType() {
    String[] s  = { Token.ALPHABETICAL_ENUMERATED_TOKEN } ;
    return s;
  }
}