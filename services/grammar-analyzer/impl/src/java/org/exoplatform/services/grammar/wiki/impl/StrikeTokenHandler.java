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
public class StrikeTokenHandler extends TokenHandler {
  
  private Token parent_  = new Token(0,0);
  
  public Token handleToken(Token parent, Token token, ParsingContext context)  {
    StringBuffer b = context.getOutputBuffer() ;
    parent_.clone(token) ;
    parent_.parent = parent ;
    token = context.nextToken(token) ;
    if(token.type == Token.DEFAULT_TOKEN)  {
      b.append("<strike>") ;
      while(token != null) {
        if(token.type == Token.DEFAULT_TOKEN) {
          token = context.getTokenHandlerManager().handleToken(parent_, token, context) ;
        } else if(token.type == Token.STRIKE_TOKEN) {
          token = context.nextToken(token) ;
          break ;
        } else if(token.group == Token.SINGLE_TOKEN_GROUP || token.group == Token.INLINE_TOKEN_GROUP) {
          if(parent_.hasAncestor(token.type)) {
            break ;
          }
          token = context.getTokenHandlerManager().handleToken(parent_, token, context) ;
        } else {
          break ;
        }
      }
      b.append("</strike>") ;
      return token ;
    } 
    b.append(context.buf_, parent_.start, parent_.end - parent_.start + 1) ;
    return token ;
  }
  
  public String[] getHandleableTokenType() { return new String[]{Token.STRIKE_TOKEN} ;}
}