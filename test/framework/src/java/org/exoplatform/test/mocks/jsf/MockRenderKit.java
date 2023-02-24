/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.mocks.jsf;

import java.util.HashMap ;
import java.io.Writer ;
import java.io.OutputStream ;
import javax.faces.render.* ;
import javax.faces.context.* ;
/**
 * Apr 10, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MockRenderKit.java,v 1.1 2004/10/11 23:27:25 tuan08 Exp $
 **/
public class MockRenderKit extends RenderKit {
  private HashMap map_ ;
  
	public MockRenderKit() {
		map_ = new HashMap() ;
  }
  
  public void addRenderer(String family, java.lang.String rendererType, Renderer renderer) {
  	map_.put(family + "/" + rendererType, renderer ) ;
  }
  
  public  Renderer getRenderer(String family, String rendererType) {
  	return (Renderer) map_.get(family + "/" + rendererType) ;
  }
  
  public ResponseStateManager getResponseStateManager() {
  	return null ;
  }
  
  public ResponseWriter createResponseWriter(Writer writer, String contentTypeList,
                                             String characterEncoding) {
    return null ; 
  }
  
  public ResponseStream createResponseStream(OutputStream out) {
  	return null ;
  }
}