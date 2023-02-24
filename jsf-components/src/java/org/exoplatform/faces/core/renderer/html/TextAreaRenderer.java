/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.faces.core.renderer.html;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.FacesUtil;
import org.exoplatform.faces.core.component.UITextArea;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 15, 2004 5:05:45 PM
 */
public class TextAreaRenderer extends HtmlBasicRenderer {
	
	public void decode(FacesContext context, UIComponent component) {
		UITextArea uiInput = (UITextArea) component;
    if(!uiInput.isEditable() || uiInput.isReadonly()) return ;
		Map paramMap = context.getExternalContext().getRequestParameterMap();
		String value = (String) paramMap.get(uiInput.getName());
		if (value == null) return  ;
		// Update managed bean (model object)		
		if (!FacesUtil.updateBoundValueBinding(context, component, "text", value)) {		
			uiInput.setText(value);
		}
	}
	
  public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    UITextArea uiTextArea = (UITextArea) component;
    ResponseWriter w = context.getResponseWriter() ;
    String value = uiTextArea.getText() ;
    if (value == null) value = "" ;
    w.write("<textarea ");
    w.write(" id='"); w.write(uiTextArea.getId()); w.write("'") ;
    w.write(" name='"); w.write(uiTextArea.getName()); w.write("'") ;
    if (uiTextArea.getCols() != null) {
    	w.write(" cols='"+uiTextArea.getCols()+"'") ;
    }
    if (uiTextArea.getRows() != null) {
    	w.write(" rows='"+uiTextArea.getRows()+"'") ;
    }
    if (uiTextArea.getClazz() != null) {
    	w.write(" class='"); w.write(uiTextArea.getClazz()); w.write("'") ;
    }
    if (uiTextArea.getKeyDown() != null) {
    	w.write(" onKeyDown='"); w.write(uiTextArea.getKeyDown()); w.write("'") ;
    }
    if (uiTextArea.getKeyUp() != null) {
    	w.write(" onKeyUp='"); w.write(uiTextArea.getKeyUp()); w.write("'") ;
    }
    if(!uiTextArea.isEditable() || uiTextArea.isReadonly()) {
      w.write(" readonly='readonly' ");
    }
    w.write(">") ;
    w.write(value) ;
    w.write("</textarea>") ;
  }
}