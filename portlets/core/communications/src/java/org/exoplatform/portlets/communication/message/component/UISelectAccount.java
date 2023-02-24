/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import java.util.ArrayList;
import java.util.List;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UISelectBox;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.faces.core.component.model.SelectItem;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.message.Account;
import org.exoplatform.services.communication.message.MessageService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UISelectAccount.java,v 1.5 2004/09/28 18:19:22 tuan08 Exp $
 */
public class UISelectAccount extends  UISimpleForm {
  private UISelectBox accountInput_;
  private Account selectAccount_ ;
  private List accounts_ ;
  
  public UISelectAccount(MessageService service) throws Exception {
    super("selectAccountForm", "post", null) ;
  	setId("UISelectAccount") ;
    setClazz("UISelectAccount");
    String userName = SessionContainer.getInstance().getOwner();
    accounts_ = service.getAccounts(userName) ;
    if(accounts_.size() > 0) selectAccount_ = (Account) accounts_.get(0) ;
    List options = new ArrayList() ;
    options.add(new SelectItem("Select an account", ""));
    for(int i = 0; i < accounts_.size(); i++) {
      Account account = (Account) accounts_.get(i);
      String name = account.getAccountName() ;
      options.add(new SelectItem(name, name));
    }
    accountInput_ = new UISelectBox("account", "", options);
    accountInput_.setUpdateOnChangeAction("changeAccount");
    add(new Row().
        add(new ComponentCell(this, accountInput_)));
    addActionListener(ChangeAccountActionListener.class, "changeAccount") ;
  }
  
  public Account getSelectAccount() { return selectAccount_ ; }
  
  static public class ChangeAccountActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UISelectAccount uiSelectAccount = (UISelectAccount) event.getSource() ;
      String accountName = uiSelectAccount.accountInput_.getValue() ;
      List accounts = uiSelectAccount.accounts_ ;
      for (int i = 0;  i < accounts.size(); i++) {
        Account account = (Account) accounts.get(i) ;
        if(account.getAccountName().equals(accountName)) {
          uiSelectAccount.selectAccount_ = account ;
          UIAccount  uiAccount = (UIAccount) uiSelectAccount.getSibling(UIAccount.class) ;
          uiAccount.setAccount(account) ;
          break ;
        }
      }
      uiSelectAccount.accountInput_.setValue("") ;
    }
  }
}