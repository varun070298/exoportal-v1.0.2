/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.wiki.impl;

import java.util.* ;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.services.log.LogUtil;
import net.sf.cglib.util.StringSwitcher;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 19, 2004
 * @version $Id$
 */
public class TokenHandlerManager {
  private TokenHandler[] handlers_ ;
  private TokenHandler defaultHandler_ ;
  private List monitors_ ;
  private StringSwitcher switcher_ ;
  //private List 
  
  public TokenHandlerManager(ServiceConfiguration sconf) {
    Map map = initHandlers(sconf) ; 
    defaultHandler_ = (TokenHandler) map.get(Token.DEFAULT_TOKEN) ;
    handlers_ = new TokenHandler[map.size()] ;
    String[] keys = new String[map.size()] ;
    int[] index = new int[map.size()] ;
    int counter = 0 ;
    Iterator i = map.entrySet().iterator() ;
    while(i.hasNext()) {
      Map.Entry  entry = (Map.Entry)i.next() ;
      handlers_[counter] = (TokenHandler) entry.getValue() ;
      keys[counter] = (String) entry.getKey() ;
      index[counter] = counter ;
      counter++ ;
    }
    switcher_ = StringSwitcher.create(keys, index, false) ;
    monitors_ = new ArrayList() ;
  }
  
  private Map initHandlers(ServiceConfiguration sconf)  {
    ExtMap map = new ExtMap() ;
    List handlers = sconf.getValuesParam("token.handlers").getValues() ;
    for(int i = 0; i < handlers.size(); i++) {
      String handler =  (String) handlers.get(i) ;
      try {
        Class clazz = Class.forName(handler) ;
        TokenHandler thandler = (TokenHandler)clazz.newInstance() ;
        map.put(thandler) ;
      } catch (Exception ex) {
        LogUtil.getLog(getClass()).error("create handler", ex) ;
      }
    }
    return map ;
  }
  
  public void reinit(ParsingContext context)  {
    for(int i = 0; i < monitors_.size(); i++) {
      TokenHandler handler = (TokenHandler) monitors_.get(i) ;
      handler.reinit(context) ;
    }
    monitors_.clear();
  }
  
  public void addMonitor(TokenHandler handler) {
    if(monitors_.contains(handler)) return ;
    monitors_.add(handler) ;
  }
  
  public void removeMonitor(TokenHandler handler) {
    monitors_.remove(handler) ;
  }
  
  public Token handleToken(Token parent, Token token, ParsingContext context) {
    int index = switcher_.intValue(token.getTokenType()) ;
    if(index < 0) {
      return defaultHandler_.handleToken(parent, token, context) ;
    }
    return handlers_[index].handleToken(parent, token, context) ;
  }
  
  class ExtMap extends HashMap {
    public void put(TokenHandler handler) {
      String[] key = handler.getHandleableTokenType() ;
      for(int i = 0; i < key.length; i++) {
        put(key[i], handler) ;
      }
    }
  }
}