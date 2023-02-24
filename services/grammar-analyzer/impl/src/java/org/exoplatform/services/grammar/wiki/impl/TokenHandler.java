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
public class TokenHandler {
  public Token handleToken(Token parent, Token token, ParsingContext context)   {
    throw new IllegalAccessError("You need to implmenent this method in: " + getClass().getName()) ;
  }
  
  public void reinit(ParsingContext context)   {
    
  }
  
  public String[] getHandleableTokenType() {
    throw new IllegalAccessError("You need to implmenent this method in: " + getClass().getName()) ;
  }
  
  protected boolean hasAncestor(Token token , String type) {
    if(token == null) return false ;
    return token.hasAncestor(type) ;
  }
}