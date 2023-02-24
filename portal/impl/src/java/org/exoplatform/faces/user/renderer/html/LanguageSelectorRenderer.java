/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.user.renderer.html;

import java.util.ResourceBundle;
import java.io.IOException;
import java.util.Iterator ;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.faces.user.component.UILanguageSelector;
import org.exoplatform.services.resources.LocaleConfig;


/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UserInfoRenderer.java,v 1.15 2004/10/21 15:25:17 tuan08 Exp $
 */
public class LanguageSelectorRenderer extends  HtmlBasicRenderer {
 
  final public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UILanguageSelector uiSelector  = (UILanguageSelector) component ;
    ResponseWriter w =  context.getResponseWriter() ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    Parameter localeNameParam = new Parameter(UILanguageSelector.LOCALE_NAME, "") ;
    Parameter[] changeLocaleParams = { UILanguageSelector.CHANGE_PARAM, localeNameParam } ;
    w.write("<div class='UILanguageSelector'>") ;    
    Iterator i = uiSelector.getLocaleConfigs().iterator() ;
    while(i.hasNext()) {
      LocaleConfig config = (LocaleConfig) i.next() ; 
      localeNameParam.setValue(config.getLocaleName()) ;
      linkRenderer_.render(w, uiSelector, res.getString("UILanguageSelector.image." + config.getLocaleName()), changeLocaleParams) ;
    }
    w.write("</div>") ;
  }
}