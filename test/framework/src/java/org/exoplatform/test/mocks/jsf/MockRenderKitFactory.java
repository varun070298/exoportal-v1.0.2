/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.mocks.jsf;

import java.util.HashMap ;
import javax.faces.render.* ;
import javax.faces.context.FacesContext ;
/**
 * Apr 10, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MockRenderKitFactory.java,v 1.1 2004/10/11 23:27:25 tuan08 Exp $
 **/
public class MockRenderKitFactory extends RenderKitFactory {
	private HashMap map_ ;
    
  public MockRenderKitFactory() {
    map_ = new HashMap() ; 
  }
    
  public void addRenderKit(String renderKitId, RenderKit renderKit) {
  	map_.put(renderKitId, renderKit) ;
  }
  
  public RenderKit getRenderKit(FacesContext context, String renderKitId) {
  	return (RenderKit) map_.get(renderKitId) ;
  }
  
  public java.util.Iterator getRenderKitIds() {
   return null ; 
  }
}
