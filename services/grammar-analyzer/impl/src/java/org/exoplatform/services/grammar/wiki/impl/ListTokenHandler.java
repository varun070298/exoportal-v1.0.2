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
public class ListTokenHandler extends TokenHandler {
  private int currentLevel_ = 0; 
  
  public Token handleToken(Token parent, Token token, ParsingContext context)  {
    currentLevel_ = 0 ;
    StringBuffer b = context.getOutputBuffer() ;
    TokenHandlerManager manager = context.getTokenHandlerManager() ;
    while(token != null) {
      if(token.type == Token.LIST_LEVEL_1_TOKEN) {
        li(b, 1) ;
        token = context.nextToken(token) ;
      } else if(token.type == Token.LIST_LEVEL_2_TOKEN) {
        li(b, 2) ;
        token = context.nextToken(token) ;
      } else if(token.type == Token.LIST_LEVEL_3_TOKEN) {
        li(b, 3) ;
        token = context.nextToken(token) ;
      } else if(token.type == Token.MULTIPLE_NEW_LINE_TOKEN) {
        break ;
      } else {
        token = manager.handleToken(null ,token, context) ;
      }
    }
    b.append("</li>");
    ul(b, 0);
    return token ;
  }
  
  public String[] getHandleableTokenType() {
    String[] s  = {Token.LIST_LEVEL_1_TOKEN, Token.LIST_LEVEL_2_TOKEN, Token.LIST_LEVEL_3_TOKEN } ;
    return s;
  }
  
  private void li(StringBuffer b, int level) {
    if(currentLevel_ > 0) b.append("</li>\n") ;
    ul(b, level) ;
    b.append("<li>\n") ; 
  }
  
  private void ul(StringBuffer b, int level) {
    if(currentLevel_ < level) {
      for(; currentLevel_ < level; currentLevel_++) {
        b.append("<ul>\n") ; 
      }
    } else if (currentLevel_ > level) {
      for(; currentLevel_ > level; currentLevel_--) {
        b.append("</ul>\n") ; 
      }
    }
  }
}
