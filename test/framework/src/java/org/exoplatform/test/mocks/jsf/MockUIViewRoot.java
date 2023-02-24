/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.mocks.jsf;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKitFactory;
/**
 * Apr 12, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MockUIViewRoot.java,v 1.1 2004/10/11 23:27:25 tuan08 Exp $
 **/
public class MockUIViewRoot extends UIViewRoot {
  
   public void encodeBegin(FacesContext context) throws IOException {
     
   }
   
   public void encodeChildren(FacesContext context) throws IOException {
     
   }
   
   public void encodeEnd(FacesContext context) throws IOException {
     ResponseWriter w = context.getResponseWriter() ;
     //w.write("<html><body>") ;
     List children = getChildren() ;
     for (int i = 0 ; i < children.size() ; i++) {
       UIComponent child = (UIComponent)children.get(i) ;
       child.encodeBegin(context) ;
       child.encodeChildren(context) ;
       child.encodeEnd(context) ;
     }
     //w.write("</body></html>") ;
   }
   
   public String getRenderKitId() {
   	return RenderKitFactory.HTML_BASIC_RENDER_KIT ;
   }
}