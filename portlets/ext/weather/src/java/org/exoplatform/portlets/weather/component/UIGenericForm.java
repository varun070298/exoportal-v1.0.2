/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.weather.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.event.ExoActionEvent;


/**
 * Jul 16, 2004, 11:36:22 AM
 * @author: F MORON
 * @email:   francois.moron@rd.francetelecom.com
 **/

public class UIGenericForm extends UIGrid {
	final public static String  GENERIC_FORM_RENDERER  = "GenericFormRenderer";

	private ArrayList		commandButtons_;
	private boolean      error_;
	private String       formName_;
	private String       method_;
	private String       formId_;

	public UIGenericForm(String name, String method, String formId) {
		commandButtons_ = new ArrayList(5);
		formName_ = name;
		method_ = method;
		formId_ = formId;
	   if (formId_ == null) formId_ = name ;
		setRendererType(GENERIC_FORM_RENDERER);
		setId(name) ;
	}

	public UIGenericForm register(UICommandButton button) {
		for (int i=0; i<commandButtons_.size(); i++) {
			if (( (UICommandButton) commandButtons_.get(i) ).getName().equalsIgnoreCase(button.getName())) return this;
		}
		commandButtons_.add(button);
		return this;
	}

	public boolean hasError() { return error_; }

	public void setError(boolean b) {  error_ = b; }

	public String getFormName() {  return formName_; }

	public String getMethod() {  return method_; }

	public String getFormId() {  return formId_; }

	public void decode(FacesContext context) {
		Map paramMap = context.getExternalContext().getRequestParameterMap() ;
		for (int i=0; i < commandButtons_.size(); i++) {
			String buttonName = ((UICommandButton)commandButtons_.get(i)).getName();
		  if(paramMap.containsKey(buttonName)) {
		   	broadcast(new ExoActionEvent(this, buttonName, paramMap)) ;
				return;
		   }
		}
	}
  
	public void processDecodes(FacesContext context) {
		error_ = false;
		Map paramMap = context.getExternalContext().getRequestParameterMap();
		String uicomponent = (String) paramMap.get(UICOMPONENT);
		if (!getId().equals(uicomponent)) return;
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
		  UIComponent child = (UIComponent) children.get(i);
		  child.processDecodes(context);
		}
		decode(context);
	}
}