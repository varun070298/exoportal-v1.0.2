 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.portal.faces.renderer.html.container;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.component.UIBasicComponent;
import org.exoplatform.portal.faces.component.UIBody;
import org.exoplatform.portal.faces.component.UIContainer;
import org.exoplatform.portal.faces.component.UIPortlet;


/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class TabRenderer extends ContainerRenderer{  
  
  final static protected Parameter CHANGE_TAB = 
    new Parameter(PortalConstants.PORTAL_ACTION, PortalConstants.CHANGE_CONTAINER_TAB_ACTION);     
  final static public String TAB_PARAMETER = "tab" ;
  
  protected void renderViewMode(FacesContext context, UIContainer uiContainer) 
      throws IOException {       
    ResponseWriter w = context.getResponseWriter();
    List children = uiContainer.getChildren();
    int childrenSize = children.size() ;
    if(childrenSize == 0 ) return ;
    w.write("<div class='"); w.write(uiContainer.getDecorator()); w.write("-container'");
    w.write(" id='") ;
    if(uiContainer.getDisplayTitle() == null)
      w.write(uiContainer.getId());
    else
      w.write(uiContainer.getDisplayTitle());
    w.write("'>") ;
    UIBasicComponent selectedComponent = null;
    w.  write("<div class='tabs'>");
    for(int i=0 ; i < childrenSize; i++) {
      UIBasicComponent uiChild = (UIBasicComponent) children.get(i) ;
      if(uiChild.isRendered()) {
        if(i == uiContainer.getSelectedComponent()){          
          w.write("<table><tr><td class='selected-tab-left' src='/skin/blank.gif'/>");
          w.write("<td class='selected-tab-middle'>");
          renderTabLink(w, uiContainer, uiChild);
          w.write("</td>");
          w.write("<td class='selected-tab-right' src='/skin/blank.gif'/></tr></table>\n");                             
          selectedComponent = uiChild;
        } else {
          w.write("<table><tr><td class='tab-left' src='/skin/blank.gif'/>");
          w.write("<td class='tab-middle'>");
          renderTabLink(w, uiContainer, uiChild);
          w.write("</td>");
          w.write("<td class='tab-right' src='/skin/blank.gif'/></tr></table>\n");                    
        }
      }
    }    
    w.  write("</div>"); 
    w.  write("<div class='selected-body'>"); 
    if(selectedComponent == null) {
      selectedComponent = (UIBasicComponent) children.get(0) ;
      selectedComponent.setRendered(true) ;
    }
    selectedComponent.encodeBegin(context);
    selectedComponent.encodeChildren(context);
    selectedComponent.encodeEnd(context);
    w.  write("</div>"); 
    w.write("</div>") ;      
  }
  
  private void renderTabLink(ResponseWriter w, UIContainer uiContainer, UIBasicComponent uiChild) 
    throws IOException{
    Parameter tab = new Parameter(TAB_PARAMETER, "");
    Parameter[] changeTabParams = {CHANGE_TAB, tab };
    String tabTitle = "";
    String componentId = "";    
    if(uiChild instanceof UIContainer){
      tabTitle = ((UIContainer)uiChild).getDisplayTitle();     
      componentId = uiChild.getId();
    } else if (uiChild instanceof UIPortlet ){
      tabTitle = ((UIPortlet)uiChild).getDisplayTitle();   
      componentId = uiChild.getId();
    } else if (uiChild instanceof UIBody ){
      tabTitle = uiChild.getId();
      componentId = tabTitle;
    }
    tab.setValue(componentId);
    if(tabTitle == null) tabTitle = componentId ;
    linkRenderer_.render(w, uiContainer, tabTitle, changeTabParams);    
  }
}