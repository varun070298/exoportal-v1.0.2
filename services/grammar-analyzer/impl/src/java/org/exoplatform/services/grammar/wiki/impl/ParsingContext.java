/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.wiki.impl;

import org.exoplatform.container.configuration.ServiceConfiguration;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 17, 2004
 * @version $Id$
 */
public class ParsingContext {
  char[] buf_ ;
  int currentPosition_  ;
  Token currentToken_ ;
  private TokenHandlerManager tokenManager_ ;
  private StringBuffer out_ ;
  
  public ParsingContext(ServiceConfiguration sconf) {
    currentToken_ = new Token(0, 0);
    tokenManager_ = new TokenHandlerManager(sconf) ;
  }
  
  public StringBuffer getOutputBuffer() { return out_ ; }
  public void  SetOutputBuffer(StringBuffer out) { out_ = out ; }
  
  public TokenHandlerManager getTokenHandlerManager() { return tokenManager_; }
  
  public void transform(String text)  {
    buf_ = text.toCharArray() ;
    out_  = new StringBuffer() ;
    currentToken_.end = -1 ;
    Token token = nextToken(currentToken_) ;
    while(token != null) {
      token = tokenManager_.handleToken(null, token, this) ;
    }
  }
  
  public Token nextToken(Token previous)  {
    currentPosition_ = previous.end + 1;
    if(currentPosition_ >= buf_.length) return null ;
    currentToken_.start = currentPosition_ ;
    switch(buf_[currentPosition_]) {
      case '{' : return  readCurlyBracePrefixToken() ; 
      case '-' : return readDashPrefixToken() ; 
      case '_' : return readUnderscorePrefixToken() ;
      case '*' : return readStarPrefixToken() ;
      case '~' : return  readTildePrefixToken() ; 
      case ':' : return readColonPrefixToken();
      case '[' : return readSquareBracePrefixToken() ;
      case '\n': case '\r': return readNewLinePrefixToken() ;
      default :  return readDefaultToken() ;
    }
  }
  
  private Token  readDefaultToken() {
    for(; currentPosition_ < buf_.length; currentPosition_++) {
      switch(buf_[currentPosition_]) {
        case '{' :  case '-' : case '_' : case '*' : case '~' : 
        case ':' :  case '[' :  case '\n' : case '\r' :{
          return createToken(--currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
        }
        default :  
      }
    }
    return createToken(--currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
  }
  
  private Token  readCurlyBracePrefixToken() {
    for(; currentPosition_ < buf_.length; currentPosition_++) {
      switch(buf_[currentPosition_]) {
        case '}' :  {
          currentToken_ = createToken(currentPosition_, Token.CURLY_BRACES_TOKEN, Token.BLOCK_TOKEN_GROUP) ;
          currentToken_.type = getSubstring(currentToken_.start, currentToken_.end) ;
          return currentToken_ ;
        }
        case  '\n' : {
          return createToken(--currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
        }
      }
    }
    return createToken(currentPosition_--, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
  }
  
  private Token  readSquareBracePrefixToken() {
    for(; currentPosition_ < buf_.length; currentPosition_++) {
      switch(buf_[currentPosition_]) {
        case ']' :  {
          return createToken(currentPosition_, Token.LINK_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
        }
        case  '\n' : case '\r' :{
          return createToken(--currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
        }
      }
    }
    return createToken(--currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
  }
  
  private Token  readColonPrefixToken() {
    for(; currentPosition_ < buf_.length; currentPosition_++) {
      switch(buf_[currentPosition_]) {
        case  '\n' : case '\r' : case ' ': case  '\t': {
          return createToken(--currentPosition_, Token.SMILEY_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
        }
      }
    }
    return createToken(--currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
  }
  
  private Token  readDashPrefixToken() {
    if(currentPosition_ + 1 >=  buf_.length)  {
      return createToken(currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
    }
    if(buf_[currentPosition_] == '-' && buf_[currentPosition_ + 1] == '-') {
      int i = currentPosition_  ;
      while(i < buf_.length && buf_[i] == '-') i++;
      i-- ;
      if(i > currentPosition_ + 2) {
        currentPosition_ = i ;
        return createToken(currentPosition_, Token.HORIZONTAL_LINE_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
      }
      currentPosition_ = i ;
      return createToken(currentPosition_ , Token.STRIKE_TOKEN, Token.INLINE_TOKEN_GROUP) ;
    }
    return createToken(currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
  }
  
  private Token readUnderscorePrefixToken() {
    if(currentPosition_ + 1 >=  buf_.length)  {
      return createToken(currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
    }
    if(buf_[currentPosition_] == '_' && buf_[currentPosition_ + 1] == '_') {
      return createToken(++currentPosition_ , Token.UNDERLINE_TOKEN, Token.INLINE_TOKEN_GROUP) ;
    }
    return createToken(currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
  }
  
  private Token readStarPrefixToken() {
    if(currentPosition_ + 1 >=  buf_.length)  {
      return createToken(currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
    }
    if(buf_[currentPosition_] == '*' && buf_[currentPosition_ + 1] == '*') {
      return createToken(++currentPosition_ , Token.BOLD_TOKEN, Token.INLINE_TOKEN_GROUP) ;
    }
    
    if(buf_[currentPosition_] == '*') {
      return createToken(currentPosition_ , Token.BOLD_TOKEN, Token.INLINE_TOKEN_GROUP) ;
    }
    return createToken(currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
  }
  
  private Token  readTildePrefixToken() {
    if(currentPosition_ + 1 >=  buf_.length) {
      return createToken(currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
    }
    if(buf_[currentPosition_] == '~' && buf_[currentPosition_ + 1] == '~') {
      return createToken(++currentPosition_ , Token.ITALIC_TOKEN, Token.INLINE_TOKEN_GROUP) ;
    }
    return createToken(currentPosition_, Token.DEFAULT_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
  }
  
  private Token readNewLinePrefixToken() {
    int i = currentPosition_ ;
    int newLineCounter = 0 ;
    while(i < buf_.length && (buf_[i] == '\n' || buf_[i] == '\r'))  {
      if(buf_[i] == '\n') newLineCounter++ ;
      i++ ;
    }
    if(newLineCounter > 1) {
      if(i == buf_.length)  i-- ;
      if(buf_[i] != '\n' && buf_[i] != '\r') i-- ;
      currentPosition_ = i - 1;
      return createToken(currentPosition_, Token.MULTIPLE_NEW_LINE_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
    } 
    //  escape ' '
    while(i < buf_.length && (buf_[i] == ' ' || buf_[i] == '\t')) i++ ;
    if(i + 2 < buf_.length)  { 
      if(buf_[i] == '*') {
        if(buf_[i] == '*' && buf_[i + 1] == '*' &&  buf_[i + 2] == '*') {
          currentPosition_ = i + 2 ;
          return createToken(currentPosition_, Token.LIST_LEVEL_3_TOKEN, Token.BLOCK_TOKEN_GROUP) ;
        }
        if(buf_[i] == '*' && buf_[i + 1] == '*') {
          currentPosition_ = i + 1 ;
          return createToken(currentPosition_, Token.LIST_LEVEL_2_TOKEN, Token.BLOCK_TOKEN_GROUP) ;
        }
        currentPosition_ = i ;
        return createToken(currentPosition_, Token.LIST_LEVEL_1_TOKEN, Token.BLOCK_TOKEN_GROUP) ;
      }
      
      if(buf_[i] == '1' ) {
        if(buf_[i] == '1' && buf_[i + 1] == '.' &&  buf_[i + 2] == '1') {
          currentPosition_ = i + 2 ;
          return createToken(currentPosition_, Token.TITLE_1_1_TOKEN, Token.BLOCK_TOKEN_GROUP) ;
        } else if(buf_[i] == '1' && buf_[i + 1] == '.') {
          currentPosition_ = i + 1 ;
          return createToken(currentPosition_, Token.ENUMERATED_TOKEN, Token.BLOCK_TOKEN_GROUP) ;
        } else if(buf_[i] == '1' && buf_[i + 1] == ' ') {
          currentPosition_ = i + 1 ;
          return createToken(currentPosition_, Token.TITLE_1_TOKEN, Token.BLOCK_TOKEN_GROUP) ;
        }
      }
      
      if(buf_[i] == 'a' && buf_[i + 1] == '.') {
        currentPosition_ = i + 1 ;
        return createToken(currentPosition_, Token.ALPHABETICAL_ENUMERATED_TOKEN, Token.BLOCK_TOKEN_GROUP) ;
      }
      if(buf_[i] == 'i' && buf_[i + 1] == '.') {
        currentPosition_ = i + 1 ;
        return createToken(currentPosition_, Token.ROMAN_ENUMERATED_TOKEN, Token.BLOCK_TOKEN_GROUP) ;
      }
    }
    if(i == buf_.length)  i-- ;
    if(buf_[i] != '\n' && buf_[i] != '\r') i-- ;
    currentPosition_ = i ;
    return createToken(currentPosition_, Token.SINGLE_NEW_LINE_TOKEN, Token.SINGLE_TOKEN_GROUP) ;
  }
  
  final Token createToken(int nextEnd, String type, String group) {
    currentToken_.end = nextEnd ;
    currentToken_.type = type ;
    currentToken_.group = group ;
    return currentToken_ ;
  }
  
  final public String getSubstring(int offset , int end)  {
    return new String(buf_, offset, end - offset + 1) ;
  }
  
  final public char[] getSubBuffer(int offset , int end)  {
    char[] sub = new char[end - offset + 1] ;
    System.arraycopy(buf_, offset, sub, 0, sub.length);
    return sub;
  }
  
  final public void out(String s) {
    out_.append(s) ;
  }
  
  final public void out(Token token) {
    out_.append(buf_, token.start, token.end - token.start + 1) ;
  }
}