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
public class DefaultTokenHandler extends TokenHandler {
  
  public Token handleToken(Token parent, Token token, ParsingContext context)  {
    context.out(token) ;
    return context.nextToken(token) ;
  }
  
  public String[] getHandleableTokenType() { return new String[]{Token.DEFAULT_TOKEN} ;}
}