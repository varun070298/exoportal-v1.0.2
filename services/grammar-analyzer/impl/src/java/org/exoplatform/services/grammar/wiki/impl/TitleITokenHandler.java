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
public class TitleITokenHandler extends TokenHandler {
  
  public Token handleToken(Token parent, Token token, ParsingContext context)  {
    token = context.nextToken(token) ;
    context.out("<h1>") ;
    while(token != null) {
      if(token.type == Token.DEFAULT_TOKEN || token.group == Token.INLINE_TOKEN_GROUP) {
        token = context.getTokenHandlerManager().handleToken(null, token, context) ;
      } else {
        break ;
      }
    }
    context.out("</h1>") ;
    return token ;
  }
  
  public String[] getHandleableTokenType() { 
    return new String[]{Token.TITLE_1_TOKEN} ;
  }
}