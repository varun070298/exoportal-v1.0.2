/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.renderer.html;

import java.io.IOException;
import java.util.* ;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.faces.core.renderer.html.ThreeImagePatternButton;
import org.exoplatform.portlets.communication.message.component.*;
import org.exoplatform.services.communication.message.*;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: AccountRenderer.java,v 1.6 2004/11/01 15:06:42 tuan08 Exp $
 */
public class AccountRenderer extends HtmlBasicRenderer {
  public AccountRenderer() {
    buttonRenderer_ = new ThreeImagePatternButton("ic3-button", "ic3-select-button") ; 
  }
  
  public void encodeChildren(FacesContext context, UIComponent component ) throws IOException {
    UIAccount uiAccount = (UIAccount) component ;
    ResponseWriter w = context.getResponseWriter() ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext());
    Account account = uiAccount.getAccount() ;
    if(account == null) {
      renderNoAccountInfo(w, res) ;
      return ;
    }
    w.write("<div class='UIAccount'>") ;
    buttonRenderer_.render(w, uiAccount, res.getString("UIAccount.button.check-new-message"), 
                           UIAccount.synchronizeParams_);
    buttonRenderer_.render(w, uiAccount, res.getString("UIAccount.button.compose"), 
                         UIAccount.composeParams_);
    List folders = uiAccount.getFolders() ;
    Parameter folderNameParam =  new Parameter(UIAccount.FOLDER_NAME, "") ;
    Parameter[] changeFolderParams = {UIAccount.changeFolderParam_, folderNameParam } ;
    for(int i = 0; i  < folders.size(); i++) {
      Folder folder = (Folder) folders.get(i);
      folderNameParam.setValue(folder.getName()) ;
      buttonRenderer_.render(w, uiAccount, folder.getName(), changeFolderParams);
    }
    w.write("</div>") ;
    renderChildren(context, uiAccount) ;
  }
  
  private void renderNoAccountInfo(ResponseWriter w , ResourceBundle res) throws IOException {
    w.write("<div class='no-account'>") ;
    w.write(res.getString("UIAccount.info.need-one-account")) ;
    w.write("</div>") ;
  }
}