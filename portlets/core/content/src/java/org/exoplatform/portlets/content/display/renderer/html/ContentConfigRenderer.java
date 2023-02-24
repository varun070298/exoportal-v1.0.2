/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.renderer.html;

import java.util.* ;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.content.display.component.UIContentConfig;
import org.exoplatform.portlets.content.display.component.model.ContentConfig;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ContentConfigRenderer.java,v 1.3 2004/07/26 02:27:19 tuan08 Exp $
 */
public class ContentConfigRenderer extends HtmlBasicRenderer {

  final public static String ADD_ICON =	"<img class='add-icon' src='/skin/blank.gif'/>";

	private static Parameter[] NEW = { new Parameter(ACTION , "new") };
	private static Parameter EDIT = new Parameter(ACTION , "edit") ;
	private static Parameter DELETE = new Parameter(ACTION , "delete") ;
	private static Parameter MODIFY = new Parameter(ACTION , "modify") ;
    
  final public void encodeChildren(FacesContext context, UIComponent component ) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    UIContentConfig uiContentConfig = (UIContentConfig) component ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    Parameter configName = new Parameter("name", "") ;
    Parameter[] editParams = {EDIT , configName } ; 
    Parameter[] modifyParams = {MODIFY , configName } ;
    Parameter[] deleteParams = { DELETE , configName } ; 
    String deleteButton = res.getString("UIContentConfig.button.delete") ;
    String editButton = res.getString("UIContentConfig.button.edit") ;
    String modifyButton = res.getString("UIContentConfig.button.modify") ;
    String titleLabel = res.getString("UIContentConfig.label.title") ;
    String uriLabel = res.getString("UIContentConfig.label.uri") ;
    String encodingLabel = res.getString("UIContentConfig.label.encoding") ;
    Map map = uiContentConfig.getAllConfigs() ;
    String baseURL = uiContentConfig.getBaseURL(context) ;
    Iterator i = map.values().iterator() ;
    w.write("<div class='UIContentConfig'>") ;
    while(i.hasNext()) {
      ContentConfig config = (ContentConfig) i.next();   
      configName.setValue(config.getName()) ;
      w.write("<table>") ;
      w.write("<tr>") ;
      w.  write("<th align='left'>") ; w.write(config.getName()) ; w.write("</th>") ;
      w.write("<th align='right'>"); 
      appendLink(w, editButton, baseURL, editParams, "") ;
      w.  write("-"); 
      if(uiContentConfig.isModificationAllowed()){
        appendLink(w, modifyButton, baseURL, modifyParams, "") ;
        w.  write("-");
      }  
      appendLink(w, deleteButton, baseURL, deleteParams, "") ;
      w. write("</th>") ;
      w.write("</tr>") ;
      w.write("<tr>") ;
      w.  write("<td><label>") ; w.write(titleLabel) ; w.write("</label></td>") ;
      w.  write("<td>") ; w.write(config.getTitle()) ; w.write("</td>") ;
      w.write("</tr>") ;
      w.write("<tr>") ;
      w.  write("<td><label>") ; w.write(uriLabel) ; w.write("</label></td>") ;
      w.  write("<td>") ; w.write(config.getUri()) ; w.write("</td>") ;
      w.write("</tr>") ;
      w.write("<tr>") ;
      w.  write("<td><label>") ; w.write(encodingLabel) ; w.write("</label></td>") ;
      w.  write("<td>") ; w.write(config.getEncoding()) ; w.write("</td>") ;
      w.write("</tr>") ;
      w.write("</table>") ;
    }
    w.write("</div>") ;
    w.write("<div>") ;
    linkRenderer_.render(w,uiContentConfig, ADD_ICON + res.getString("UIContentConfig.button.new-entry"), NEW) ;
    w.write("</div>") ;
  }
}