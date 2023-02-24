/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.faces;

import javax.faces.FactoryFinder;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.renderer.html.NodeTabbedPaneRenderer;
import org.exoplatform.faces.core.renderer.xhtmlmp.*;
import com.sun.faces.renderkit.RenderKitImpl ;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 4, 2004
 * @version $Id: XHTMLMPRendererConfiguration.java,v 1.2 2004/08/08 19:37:01 tuan08 Exp $
 */
public class XHTMLMPRendererConfiguration {
  static public void confiure() throws Exception {
    RenderKitFactory rfactory = 
      (RenderKitFactory) FactoryFinder.getFactory( FactoryFinder.RENDER_KIT_FACTORY );
    RenderKit rkit = 
      rfactory.getRenderKit(null, UIExoComponent.XHTMLMP_KIT);
    if(rkit == null) {
      rkit = new RenderKitImpl() ;
      rfactory.addRenderKit(UIExoComponent.XHTMLMP_KIT, rkit) ;
    }
    rkit = 
      rfactory.getRenderKit(null, UIExoComponent.XHTMLMP_KIT);
    if(rkit.getRenderer(UISimpleForm.COMPONENT_FAMILY, "SimpleFormRenderer") == null) {
      rkit.addRenderer(UISimpleForm.COMPONENT_FAMILY, "SimpleFormRenderer", new SimpleFormRenderer());
    }
    if(rkit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "TabbedPaneRenderer") == null) {
      rkit.addRenderer(UIExoComponent.COMPONENT_FAMILY, "TabbedPaneRenderer", new NodeTabbedPaneRenderer());
    }
  }
}