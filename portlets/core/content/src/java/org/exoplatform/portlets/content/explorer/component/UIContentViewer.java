/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentDisplayer.java,v 1.2 2004/08/07 18:11:26 tuan08 Exp $
 */
abstract public class UIContentViewer 
  extends UIExoComponentBase implements ExplorerListener {
  
	final static public String BACK_ACTION = "backAction" ;
  protected static Map supportType_ ;

  static {
    RenderKitFactory rkFactory = 
      (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = rkFactory.getRenderKit(null, RenderKitFactory.HTML_BASIC_RENDER_KIT);
    supportType_ = new HashMap() ;
    Renderer renderer = renderKit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "TextContentRenderer");
    supportType_.put("text/plain"         , renderer) ;
    supportType_.put("text/html"          , renderer) ;
    supportType_.put("text/xml"           , renderer) ;
    supportType_.put("text/properties"    , renderer) ;
    supportType_.put("text/css"           , renderer) ;
    supportType_.put("text/java"          , renderer) ;

    renderer = renderKit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "ImageRenderer");
    supportType_.put("image/jpeg"   , renderer) ;
    supportType_.put("image/gif"    , renderer) ;
    supportType_.put("image/png"    , renderer) ;
    supportType_.put("image/bmp"    , renderer) ;
    
    renderer = renderKit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "DefaultContentRenderer");
    supportType_.put("default"      , renderer) ;
  }
  
  protected String mimeType_ ;
  protected String content_ ;
  protected Renderer renderer_ ;
  
  public UIContentViewer() {
  }

  public String getContentType() { return mimeType_ ;}
  public void   setContentType(String s) {mimeType_ = s ;}
  
  public String getContent() { return content_ ; }
  public void   setContent(String s) {content_ = s ; }
  
  public String getUri()  { return "" ; }
  
  public Renderer getRenderer(String name) {
    return (Renderer) supportType_.get(name) ;
  }
  
  public void  onModify(UIExplorer uiExplorer, NodeDescriptor node)  {}
  
  public void  onAddChild(UIExplorer uiExplorer, NodeDescriptor node)  { }
  
  public void  onRemove(UIExplorer uiExplorer, NodeDescriptor node)  {  }
  
  final public void encodeBegin( FacesContext context) throws IOException {
    renderer_.encodeBegin(context, this) ;
  }
  
  final public void encodeChildren( FacesContext context) throws IOException { }
  
  final public void encodeEnd( FacesContext context) throws IOException { }
}
