/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portal.faces.renderer.html.portlet;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.faces.component.UIPortletCategories;
import org.exoplatform.portal.faces.component.model.PortletCategoryData;
import org.exoplatform.services.portletregistery.Portlet;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 16 juin 2004
 */
public class PortletCategoriesRenderer extends HtmlBasicRenderer{
  private static Parameter[] cancelActionParams_ =  
  	{ new Parameter(ACTION , CANCEL_ACTION)  };
  
  private static Parameter SELECT_PORTLET_CATEGORY =  
  	new Parameter(ACTION , UIPortletCategories.SELECT_PORTLET_CATEGORY)  ;
  private static Parameter SHOW_PORTLET_CATEGORY = 
  	new Parameter(ACTION , UIPortletCategories.SHOW_PORTLET_CATEGORY_MONITOR)  ;
  private static Parameter SHOW_PORTLET =  
  	new Parameter(ACTION , UIPortletCategories.SHOW_PORTLET_MONITOR)  ;
  
  final public static String EXPAND = "<img class='expand' src='/skin/blank.gif'/>";
  final public static String COLLAPSE = "<img class='collapse' src='/skin/blank.gif'/>";

  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UIPortletCategories uiCategories = (UIPortletCategories) component;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
    List children = component.getChildren() ;
    w.write("<table class='UIPortletRegistery'>") ;
    w.  write("<tr>") ;
    if (!uiCategories.hasCategory()) {
      w.write("<td class='no-category'>");
      w.write(res.getString("UIPortletCategories.label.no-categories"));
      w.write("</td>");
    } else {
    	w.	write("<td class='UIPortletCategoryMenu'>") ;
    	renderMenu(context, uiCategories) ;
    	w.  write("</td>") ;
      for(int i = 0; i < children.size(); i++) {
        UIComponent uiChild = (UIComponent) children.get(i);
        if(uiChild.isRendered()) {
        	w.write("<td width='*'>") ;
        	uiChild.encodeBegin(context) ;
        	uiChild.encodeChildren(context) ;
        	uiChild.encodeEnd(context) ;
        	w.  write("</td>") ;
        }
      }
      w.  write("</tr>") ;
    }
    w.write("</table>") ;
    renderFooter(uiCategories, w, res) ;
  }
  
  private void renderMenu( FacesContext context, UIPortletCategories uiCategories ) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    Parameter categoryParam = new Parameter(UIPortletCategories.PORTLET_CATEGORY_NAME_PARAM , "");
    Parameter portletIdParam = new Parameter(UIPortletCategories.PORTLET_NAME_PARAM , "");
    Parameter[] selectPortletCategoryParams = { SELECT_PORTLET_CATEGORY , categoryParam} ;
    Parameter[] showPortletCategoryParams = { SHOW_PORTLET_CATEGORY , categoryParam} ;
    Parameter[] showPortletParams = { SHOW_PORTLET , categoryParam, portletIdParam} ;
    Collection categories = uiCategories.getPortletCaterories().values() ;
    Iterator appsIterator = categories.iterator() ;
    w.write("<table>") ;
    while(appsIterator.hasNext()) {
      PortletCategoryData portletCategoryData = (PortletCategoryData) appsIterator.next() ;
      String portletCategoryName = portletCategoryData.getPortletCategoryName() ;
      categoryParam.setValue(portletCategoryName);
      String sign = EXPAND ;
      if (portletCategoryData.isSelect()) sign = COLLAPSE;
    	w.write("<tr>") ;
    	w.  write("<th>");
      linkRenderer_.render(w,uiCategories, sign, selectPortletCategoryParams) ;
      linkRenderer_.render(w,uiCategories, portletCategoryName, showPortletCategoryParams) ;
      w.write("</th>") ;
    	w.write("</tr>") ;
    	if(portletCategoryData.isSelect()) {
    		Iterator portletDatasItr = portletCategoryData.getPortlets().iterator();
    		while(portletDatasItr.hasNext()) {
    			Portlet rtd = (Portlet)portletDatasItr.next() ;
    			String portletName = rtd.getDisplayName();
    			portletIdParam.setValue(rtd.getId());
    			w.write("<tr>") ;
    			w.  write("<td>");
    			linkRenderer_.render(w,uiCategories, portletName, showPortletParams) ;
          w.  write("</td>") ;
    			w.write("</tr>") ;
    		}
      }
    }
    w.write("</table>") ;
  }
  
  protected void renderFooter(UIPortletCategories uiCategories, 
  		                        ResponseWriter w, ResourceBundle res) throws IOException {
    w.write("<div align='center'>") ;
    linkRenderer_.render(w,uiCategories, res.getString("UIPortletCategories.button.cancel"), cancelActionParams_) ;
    w.write("</div>") ;
  }
}