/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.wiki.impl;

import org.exoplatform.services.grammar.wiki.WikiEngineService ;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool ;
import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.container.configuration.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 17, 2004
 * @version $Id$
 */
public class WikiEngineServiceImpl implements WikiEngineService {
  private ObjectPool pool;
  
  public WikiEngineServiceImpl(ConfigurationManager manager) throws Exception {
    ServiceConfiguration sconf = manager.getServiceConfiguration(WikiEngineService.class) ;
    pool = new StackObjectPool(new ParsingContextFactory(sconf)) ;
    
  }
  
  public String toXHTML(String text) {
    ParsingContext context = null ;
    String result = null ;
    try {
      context = (ParsingContext) pool.borrowObject() ;
      context.transform(text) ;
      result = context.getOutputBuffer().toString() ;
    } catch (Exception ex) {
      result = text + "<br/><br/>=========-CANNOT CONVERT WIKI TO HTML===========<br/>"  +
               ExceptionUtil.getStackTrace(ex, 15) ;
    }
    return result;
  }
  
  static class ParsingContextFactory extends BasePoolableObjectFactory {  
    ServiceConfiguration sconf_ ;
    
    ParsingContextFactory(ServiceConfiguration sconf) {
      sconf_ = sconf ;
    }
    
    public Object makeObject() { 
      return new ParsingContext(sconf_); 
    } 
    
    public void passivateObject(Object obj) {  
    }  
  }
}
