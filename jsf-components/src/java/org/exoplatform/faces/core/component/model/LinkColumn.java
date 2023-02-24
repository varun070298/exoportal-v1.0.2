/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.context.ResponseWriter;
import org.exoplatform.Constants;
import org.exoplatform.faces.core.component.UIGrid;

/**
 * Jun 30, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ActionColumn.java,v 1.4 2004/07/02 04:41:04 tuan08 Exp $
 */
public class LinkColumn extends Column {
  private String fieldId_ ;
  protected String parameters_ = "";
    
	public LinkColumn(String header, String fieldName, String fieldId) {
		super(header, fieldName) ;
    fieldId_ = fieldId ;
  }
  
	public LinkColumn addParameter(Parameter[] params) {
	  StringBuffer b = new StringBuffer() ;
	  for(int i = 0; i < params.length; i++) {
	    b.append(Constants.AMPERSAND);
	    b.append(params[i].getName()).append('=').append(params[i].getValue());
	  }
	  parameters_ = b.toString() ;
    return this ;
	}
	
  public void render(ResponseWriter w, ResourceBundle res, 
                     UIGrid uiParent, DataHandler dhandler) throws IOException {
    if(clazz_ == null) {
      w.write("<td>");
    } else {
      w.write("<td class='"); w.write(clazz_); w.write("'>"); 
    }
    String baseURL =  uiParent.getBaseURL() ; 
    w.write("<a");
    if (clazz_ != null) {
      w.write(" class='"); w.write(clazz_); w.write("'");
    }
    w.write(" href='"); w.write(baseURL); w.write(parameters_) ;
    w.write(Constants.AMPERSAND + "objectId="); w.write(dhandler.getData(fieldId_)) ;
    w.write("'>");
    w.write(dhandler.getData(fieldName_));
    w.write("</a>");
    w.write("</td>") ;
  }
}