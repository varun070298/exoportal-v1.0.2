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
public class BoldTokenHandler extends TokenHandler {
  private Token startToken_  = new Token(0,0);
  
  public Token handleToken(Token parent, Token token, ParsingContext context)  {
    startToken_.clone(token) ;
    startToken_.parent = parent ;
    token = context.nextToken(token) ;
    if(token.type == Token.DEFAULT_TOKEN)  {
      context.out("<span class='bold'>") ;
      while(token != null) {
        if(token.type == Token.DEFAULT_TOKEN) {
          token = context.getTokenHandlerManager().handleToken(startToken_, token, context) ;
        } else if(token.type == Token.BOLD_TOKEN) {
          token = context.nextToken(token) ;
          break ;
        } else if(token.group == Token.SINGLE_TOKEN_GROUP || token.group == Token.INLINE_TOKEN_GROUP) {
          if(startToken_.hasAncestor(token.type)) {
            break ;
          }
          token = context.getTokenHandlerManager().handleToken(startToken_, token, context) ;
        } else {
          break ;
        }
      }
      context.out("</span>") ;
      return token ;
    } 
    context.out(startToken_) ;
    return token ;
  }
  
  public String[] getHandleableTokenType() { return new String[]{Token.BOLD_TOKEN} ;}
}