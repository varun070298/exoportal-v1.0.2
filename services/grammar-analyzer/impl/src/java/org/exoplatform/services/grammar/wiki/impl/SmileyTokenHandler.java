/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.wiki.impl;

import java.util.* ;
import org.exoplatform.container.PortalContainer ;
import org.exoplatform.container.configuration.*;
import org.exoplatform.services.grammar.wiki.WikiEngineService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 19, 2004
 * @version $Id$
 */
public class SmileyTokenHandler extends TokenHandler {
 
  private Map smileys_ ;
  
  public SmileyTokenHandler() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    ConfigurationManager manager = 
      (ConfigurationManager)pcontainer.getComponentInstanceOfType(ConfigurationManager.class) ;
    ServiceConfiguration sconf = manager.getServiceConfiguration(WikiEngineService.class) ;
    smileys_ = sconf.getPropertiesParam("smiley.configuration").getProperties();
  }
  
  public Token handleToken(Token parent, Token token, ParsingContext context)  {
    String timage = token.getTokenImage(context) ;
    String image = (String) smileys_.get(timage) ;
    if(image == null)  context.getOutputBuffer().append(timage) ;
    else  context.getOutputBuffer().append(image) ;
    return context.nextToken(token) ;
  }
  
  public String[] getHandleableTokenType() { return new String[] { Token.SMILEY_TOKEN}  ;}
}