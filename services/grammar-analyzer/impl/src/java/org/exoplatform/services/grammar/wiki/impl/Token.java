/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.wiki.impl;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 17, 2004
 * @version $Id$
 */
public class Token {
  final static public String DEFAULT_TOKEN                        = "default" ;
  final static public String BOLD_TOKEN                           = "**" ;
  final static public String ITALIC_TOKEN                         = "~~" ;
  final static public String STRIKE_TOKEN                         = "--" ;
  final static public String UNDERLINE_TOKEN                      = "__" ;
  final static public String HORIZONTAL_LINE_TOKEN                = "---" ;
  final static public String SINGLE_NEW_LINE_TOKEN                = "\n" ;
  final static public String MULTIPLE_NEW_LINE_TOKEN              = "\n+" ;
  final static public String SMILEY_TOKEN                         = "smiley" ;
  
  final static public String TITLE_1_TOKEN                        = "\n 1 " ;
  final static public String TITLE_1_1_TOKEN                      = "\n 1.1" ;
  
  final static public String LINK_TOKEN                           = "[.+]" ;
  
  final static public String LIST_LEVEL_1_TOKEN                   = "\n *" ;
  final static public String LIST_LEVEL_2_TOKEN                   = "\n **" ;
  final static public String LIST_LEVEL_3_TOKEN                   = "\n ***" ;
  
  final static public String ENUMERATED_TOKEN                     = "\n 1." ;
  final static public String ALPHABETICAL_ENUMERATED_TOKEN        = "\n a." ;
  final static public String ROMAN_ENUMERATED_TOKEN               = "\n i." ;
  
  final static public String CURLY_BRACES_TOKEN                   = "{.+}" ;
  
  final static public String SINGLE_TOKEN_GROUP                   = "single" ;
  final static public String INLINE_TOKEN_GROUP                   = "inline" ;
  final static public String BLOCK_TOKEN_GROUP                    = "block" ;
  
  int start ;
  int end ;
  String type ;
  String group ;
  Token parent ;
  
  Token(int start, int end) {
    this.start = start ;
    this.end = end ;
  }
  
  Token(int start, int end, String type) {
    this.start = start ;
    this.end = end ;
    this.type = type ;
  }
  
  Token(Token token) {
    this.start = token.start ;
    this.end = token.end ;
    this.type = token.type ;
    this.group = token.group ;
    parent  =  token.parent ;
  }
  
  public void clone(Token token) {
    this.start = token.start ;
    this.end = token.end ;
    this.type = token.type ;
    this.group = token.group ;
    this.parent = token.parent ;
  }
  
  final public String getTokenImage(ParsingContext context) {
    return context.getSubstring(start, end) ;
  }
  
  public String getTokenType() {  return type ;  }
  
  public String getTokenGroup() {  return group ;  }
  
  public boolean hasAncestor(String type) {
    Token p = parent ;
    while(p != null) {
      if(p.type == type) return true ;
      p = p.parent ;
    }
   return false ; 
  }
}