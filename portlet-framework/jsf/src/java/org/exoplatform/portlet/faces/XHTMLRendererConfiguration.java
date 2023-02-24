/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.faces;

import javax.faces.FactoryFinder;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.renderer.html.*;
import org.exoplatform.faces.search.component.*;
import org.exoplatform.faces.search.renderer.html.*;
import org.exoplatform.faces.user.component.UILanguageSelector;
import org.exoplatform.faces.user.renderer.html.LanguageSelectorRenderer;
import org.exoplatform.portlet.faces.renderer.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 4, 2004
 * @version $Id: XHTMLRendererConfiguration.java,v 1.8 2004/10/25 03:53:06 tuan08 Exp $
 */
public class XHTMLRendererConfiguration {
  static public void confiure() throws Exception {
    RenderKitFactory rfactory = 
      (RenderKitFactory) FactoryFinder.getFactory( FactoryFinder.RENDER_KIT_FACTORY );
    RenderKit rkit = 
      rfactory.getRenderKit(null, RenderKitFactory.HTML_BASIC_RENDER_KIT);
    if(rkit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "TemplateRenderer") == null) {
      rkit.addRenderer(UIExoComponent.COMPONENT_FAMILY, "TemplateRenderer", new TemplateRenderer());
    }
    if(rkit.getRenderer(UIStringInput.COMPONENT_FAMILY, "StringInputRenderer") == null) {
      rkit.addRenderer(UIStringInput.COMPONENT_FAMILY, "StringInputRenderer", new StringInputRenderer());
    }
    if(rkit.getRenderer(UIDateInput.COMPONENT_FAMILY, "DateInputRenderer") == null) {
      rkit.addRenderer(UIDateInput.COMPONENT_FAMILY, "DateInputRenderer", new DateInputRenderer());
    }
    if(rkit.getRenderer(UITextArea.COMPONENT_FAMILY, "TextAreaRenderer") == null) {
      rkit.addRenderer(UITextArea.COMPONENT_FAMILY, "TextAreaRenderer", new TextAreaRenderer());
    }
    if(rkit.getRenderer(UISimpleForm.COMPONENT_FAMILY, "SimpleFormRenderer") == null) {
      rkit.addRenderer(UISimpleForm.COMPONENT_FAMILY, "SimpleFormRenderer", new SimpleFormRenderer());
    }
    if(rkit.getRenderer(UISimpleForm.COMPONENT_FAMILY, "SimpleFormButtonRenderer") == null) {
      rkit.addRenderer(UISimpleForm.COMPONENT_FAMILY,  "SimpleFormButtonRenderer", new SimpleFormButtonRenderer());
    }
    if(rkit.getRenderer(UIForm.COMPONENT_FAMILY, "FormRenderer") == null) {
      rkit.addRenderer(UIForm.COMPONENT_FAMILY,  "FormRenderer", new FormRenderer());
    }
    if(rkit.getRenderer(UIPageListIterator.COMPONENT_FAMILY, "PageListIteratorRenderer") == null) {
      rkit.addRenderer(UIPageListIterator.COMPONENT_FAMILY,  "PageListIteratorRenderer", new PageListIteratorRenderer());
    }    
    if(rkit.getRenderer(UIToolbar.COMPONENT_FAMILY, "ToolbarRenderer") == null) {
      rkit.addRenderer(UIToolbar.COMPONENT_FAMILY,  "ToolbarRenderer", new ToolbarRenderer());
    }    
    if(rkit.getRenderer(UIGrid.COMPONENT_FAMILY, "GridRenderer") == null) {
      rkit.addRenderer(UIGrid.COMPONENT_FAMILY, "GridRenderer", new GridRenderer());
    }
    if(rkit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "ChildrenRenderer") == null) {
      rkit.addRenderer(UIExoComponent.COMPONENT_FAMILY, "ChildrenRenderer", new ChildrenRenderer());
    }
    if(rkit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "TabbedPaneRenderer") == null) {
      rkit.addRenderer(UIExoComponent.COMPONENT_FAMILY, "TabbedPaneRenderer", new NodeTabbedPaneRenderer());
    }
    if(rkit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "SimpleTabRenderer") == null) {
      rkit.addRenderer(UIExoComponent.COMPONENT_FAMILY, "SimpleTabRenderer", new SimpleTabRenderer());
    }
    if(rkit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "PyramidTabBarRenderer") == null) {
      rkit.addRenderer(UIExoComponent.COMPONENT_FAMILY, "PyramidTabBarRenderer", new PyramidTabBarRenderer());
    }
    if(rkit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "PortletPreferencesRenderer") == null) {
      rkit.addRenderer(UIExoComponent.COMPONENT_FAMILY, "PortletPreferencesRenderer", new PortletPreferencesRenderer());
    }
    if(rkit.getRenderer(UIExoComponent.COMPONENT_FAMILY, "FileUploadRenderer") == null) {
      rkit.addRenderer(UIExoComponent.COMPONENT_FAMILY, "FileUploadRenderer", new FileUploadRenderer());
    }
    if(rkit.getRenderer(UISearchSummary.COMPONENT_FAMILY, "SearchSummaryRenderer") == null) {
      rkit.addRenderer(UISearchSummary.COMPONENT_FAMILY, "SearchSummaryRenderer", new SearchSummaryRenderer());
    }
    if(rkit.getRenderer(UIViewDocument.COMPONENT_FAMILY, "ViewDocumentRenderer") == null) {
      rkit.addRenderer(UIViewDocument.COMPONENT_FAMILY, "ViewDocumentRenderer", new ViewDocumentRenderer());
    }    
    if(rkit.getRenderer(UILanguageSelector.COMPONENT_FAMILY, "LanguageSelectorRenderer") == null) {
      rkit.addRenderer(UILanguageSelector.COMPONENT_FAMILY, "LanguageSelectorRenderer", new LanguageSelectorRenderer());
    }    
  }
}