/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle; 

import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.UIGrid;

/**
 * Jun 30, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ActionColumn.java,v 1.4 2004/07/02 04:41:04 tuan08 Exp $
 */
public class ActionColumn extends Column {
  private List buttons_ ;
    
	public ActionColumn(String header, String fieldName) {
		super(header, fieldName) ;
    buttons_ = new ArrayList(3) ;
  }
  
  public ActionColumn add(Button button) { 
    buttons_.add(button) ; 
    return this ;
  } 
  
  public ActionColumn add(boolean activate, Button button) {
    if(!activate) {
      button.setActivate(false) ;  
    }
    buttons_.add(button) ; 
    return this ;
  } 
  
  public void render(ResponseWriter w, ResourceBundle res, 
      UIGrid uiParent, DataHandler dhandler) throws IOException {
    if(clazz_ == null) {
      w.write("<td>");
    } else {
      w.write("<td class='"); w.write(clazz_); w.write("'>"); 
    }
    for(int i = 0; i < buttons_.size(); i++) {
      Button button = (Button)buttons_.get(i);
      button.render(w, res, uiParent, dhandler.getData(fieldName_)) ;
    }
    w.write("</td>") ;
  }
}