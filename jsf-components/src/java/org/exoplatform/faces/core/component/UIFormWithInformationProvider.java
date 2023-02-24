/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.component.model.Information;

/**
 * Jun 2, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UIFormWithInformationProvider extends UISimpleForm implements InformationProvider {
	private Information information_ = new Information() ;
	private int  displayMessageType_ = FOOTER_MESSAGE_TYPE ;
	
	public UIFormWithInformationProvider(String name, String method, String formId) {
	  super(name, method, formId) ;
	}
	
	public int   getDisplayMessageType() { return displayMessageType_ ; }
	public void  setDisplayMessageType(int type) { displayMessageType_ = type ;	}
	public void  addMessage(FacesMessage message) { information_.addMessage(message);	}
	public void  clearMessages() {  information_.clearMessages() ;}
  public List getMessages() { return information_.getMessages() ; }
	public boolean hasMessage() { return information_.hasMessage() ; }
	
	public void encodeChildren(FacesContext context) throws IOException {
    if(information_.hasMessage()) {
      if(displayMessageType_ == BODY_MESSAGE_TYPE) {
        Information.renderBodyInformation(context, this) ;
        return ; 
      }
    }
    super.encodeChildren(context);
    if(information_.hasMessage()) {
      if(displayMessageType_ == FOOTER_MESSAGE_TYPE) {
        Information.renderFooterInformation(context, this) ;
        return ; 
      }
    }
  }
}