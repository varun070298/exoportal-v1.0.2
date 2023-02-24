/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.renderer.html;

import java.util.* ;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.communication.message.component.*;
import org.exoplatform.services.communication.message.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: AccountConfigurationRenderer.java,v 1.6 2004/11/03 01:24:56 tuan08 Exp $
 */
public class AccountConfigurationRenderer extends HtmlBasicRenderer {
  
  public void encodeChildren(FacesContext context, UIComponent component ) throws IOException {
    UIAccountConfiguration uiAccConfig = (UIAccountConfiguration) component ;
    List accounts = uiAccConfig.getAccounts() ;
    ResponseWriter w = context.getResponseWriter() ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext());
    w.write("<table class='UIAccountConfiguration'>") ;
    w.	write("<tr>") ;
    w.		write("<th colspan='2'>") ; 
    w.			write(res.getString("UIAccountConfiguration.header.account-configuration")) ;  
    w.		write("</th>")  ;
    w.	write("<tr>") ;
    w.	write("<tr>") ;
    w.		write("<td>") ;
    w.			write("<h6>") ; 
    w.				write(res.getString("UIAccountConfiguration.header.your-accounts")) ; 
    w.			write("</h6>")  ;
    w.			write("<div>") ; 
    w.				write(res.getString("UIAccountConfiguration.info.account-configuration")) ; 
    w.			write("</div>")  ;
    w.      write("<ul>")  ;
    Parameter accountNameParam =  new Parameter(UIAccountConfiguration.ACCOUNT_NAME, "") ;
    Parameter[] selectAccountParams = 
      {UIAccountConfiguration.selectAccountParam, accountNameParam } ;
    for(int i = 0 ; i < accounts.size(); i++) {
      Account account = (Account) accounts.get(i) ;
      accountNameParam.setValue(account.getAccountName()) ;
      w.      write("<li>")  ;
      linkRenderer_.render(w, uiAccConfig, account.getAccountName(), selectAccountParams);
      w.      write("</li>")  ;
    }
    w.      write("</ul>")  ;
    w.			write("<div>") ; 
    linkRenderer_.render(w, uiAccConfig, res.getString("UIAccountConfiguration.button.add-account"), 
                         UIAccountConfiguration.addAccountParams_);
    w.			write("</div>")  ;
    w.		write("</td>") ; 
    w.		write("<td>") ; 
    renderSelectAccount(w, uiAccConfig, res) ;
    w.		write("</td>")  ;
    w.	write("</tr>") ;
    w.write("</table>") ;
  }
      
  private void renderSelectAccount(ResponseWriter w, UIAccountConfiguration uiAccConfig,
                                  ResourceBundle res) throws IOException {
    Account account = uiAccConfig.getSelectAccount() ;
    w.write("<h6>") ; 
    w.	write(res.getString("UIAccountConfiguration.header.account-information")) ; 
    w.write("</h6>")  ;
    if(account == null) {
      w.write("<div>") ; 
      w.  write(res.getString("UIAccountConfiguration.info.no-account")) ; 
      w.write("</div>")  ;
      return ;
    }
    w.write("<table class='account-detail'>") ;
    w.  write("<tr>") ;
    w.    write("<td><label>"); 
    w.      write(res.getString("UIAccountConfiguration.label.account-name")); 
    w.    write("</label></td>") ; 
    w.    write("<td>"); w.write(account.getAccountName()); w.write("</td>")  ;
    w.  write("</tr>") ;
    w.    write("<td><label>"); 
    w.      write(res.getString("UIAccountConfiguration.label.account-owner")); 
    w.    write("</label></td>") ; 
    w.    write("<td>"); w.write(account.getOwner()); w.write("</td>")  ;
    w.  write("</tr>") ;
    w.  write("</tr>") ;
    w.    write("<td><label>"); 
    w.      write(res.getString("UIAccountConfiguration.label.owner-name")); 
    w.    write("</label></td>") ; 
    w.    write("<td>"); w.write(ft_.format(account.getOwnerName())); w.write("</td>")  ;
    w.  write("</tr>") ;
    w.  write("</tr>") ;
    w.    write("<td><label>"); 
    w.      write(res.getString("UIAccountConfiguration.label.reply-to")); 
    w.    write("</label></td>") ; 
    w.    write("<td>"); w.write(ft_.format(account.getReplyToAddress())); w.write("</td>")  ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td><label>"); 
    w.      write(res.getString("UIAccountConfiguration.label.message-protocol")); 
    w.    write("</label></td>") ; 
    w.    write("<td>")  ; w.write(account.getProtocol());  w.write("</td>")  ;
    w.  write("</tr>") ;
    if(!MessageService.STANDALONE_PROTOCOL.equals(account.getProtocol())) {
      w.write("<tr>") ;
      w.  write("<td><label>"); 
      w.    write(res.getString("UIAccountConfiguration.label.user-name")); 
      w.  write("</label></td>") ; 
      w.  write("<td>")  ; 
      w.    write(ft_.format(account.getProperty(Account.SERVER_SETTING_USERNAME)));  
      w.  write("</td>")  ;
      w.write("</tr>") ;
      w.write("<tr>") ;
      w.  write("<td><label>"); 
      w.    write(res.getString("UIAccountConfiguration.label.password")); 
      w.  write("</label></td>") ; 
      w.  write("<td>")  ; 
      w.    write(ft_.format(account.getProperty(Account.SERVER_SETTING_PASSWORD)));  
      w.  write("</td>")  ;
      w.write("</tr>") ;
      w.write("<tr>") ;
      w.  write("<td><label>"); 
      w.    write(res.getString("UIAccountConfiguration.label.mail-server")); 
      w.  write("</label></td>") ; 
      w.  write("<td>")  ; 
      w.    write(ft_.format(account.getProperty(Account.SERVER_SETTING_HOSTNAME)));  
      w.  write("</td>")  ;
      w.write("</tr>") ;
    }
    w.  write("<tr>") ;
    w.    write("<td colspan='2'>"); 
    w.      write(res.getString("UIAccountConfiguration.label.message-boxes")); 
    List folders = uiAccConfig.getSelectAccountFolders() ;
    Parameter folderNameParam =  new Parameter(UIAccountConfiguration.FOLDER_NAME, "") ;
    Parameter[] selectFolderParams = 
      {UIAccountConfiguration.selectFolderParam, folderNameParam } ;
    w.write("<ul>") ;
    for(int i = 0; i < folders.size(); i++) {
      Folder folder = (Folder) folders.get(i) ;
      folderNameParam.setValue(folder.getName()) ;
      w.write("<li>") ; 
      linkRenderer_.render(w, uiAccConfig, folder.getLabel(), selectFolderParams);
      w.write("</li>") ;
    }
    w.  write("<li>") ; 
    linkRenderer_.render(w, uiAccConfig, res.getString("UIAccountConfiguration.button.add-folder"), 
                         UIAccountConfiguration.addFolderParams_);
    w.  write("</li>") ;
    w.write("</ul>") ;
    w.    write("</td>") ; 
    w.  write("</tr>") ;
    
    w.    write("<td colspan='2'>"); 
    w.      write("<label>"); 
    w.        write(res.getString("UIAccountConfiguration.label.signature")); 
    w.      write("</label><br/>") ;
    w.      write("<pre>") ;
    w.      write(ft_.format(account.getSignature())); 
    w.      write("</pre>") ;
    w.    write("</td>")  ;
    w.  write("</tr>") ;
    
    w.  write("<tr>") ;
    w.    write("<td colspan='2' align='center'>"); 
    linkRenderer_.render(w, uiAccConfig, res.getString("UIAccountConfiguration.button.edit-account"), 
                         UIAccountConfiguration.editAccountParams_);
    linkRenderer_.render(w, uiAccConfig , res.getString("UIAccountConfiguration.button.delete-account"), 
                         UIAccountConfiguration.deleteAccountParams_);
    w.    write("</td>") ; 
    w.  write("</tr>") ;
    w.  write("</tr>") ;
    w.write("</table>") ;
  }
}