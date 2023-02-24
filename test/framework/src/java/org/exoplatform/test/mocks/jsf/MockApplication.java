/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.mocks.jsf;
import java.util.*;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.*;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import javax.faces.application.*;
/**
 * Apr 15, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MockApplication.java,v 1.1 2004/10/11 23:27:25 tuan08 Exp $
 **/
public class MockApplication extends Application {
	public MockApplication() {
	}
	
	public ActionListener getActionListener() {
		return null;
	}
	public void setActionListener(ActionListener actionlistener) {
	}
	public Locale getDefaultLocale() {
		return null;
	}
	public void setDefaultLocale(Locale locale) {
	}
	public String getDefaultRenderKitId() {
		return null;
	}
	public void setDefaultRenderKitId(String s) {
	}
	public String getMessageBundle() {
		return null;
	}
	public void setMessageBundle(String s) {
	}
	public NavigationHandler getNavigationHandler() {
		return null;
	}
	public void setNavigationHandler(NavigationHandler navigationhandler) {
	}
	public PropertyResolver getPropertyResolver() {
		return null;
	}
	public void setPropertyResolver(PropertyResolver propertyresolver) {
	}
	public VariableResolver getVariableResolver() {
		return null;
	}
	public void setVariableResolver(VariableResolver variableresolver) {
	}
	public ViewHandler getViewHandler() {
		return null;
	}
	public void setViewHandler(ViewHandler viewhandler) {
	}
	public StateManager getStateManager() {
		return null;
	}
	public void setStateManager(StateManager statemanager) {
	}
	public void addComponent(String s, String s1) {
	}
	public UIComponent createComponent(String s) throws FacesException {
		return null;
	}
	public UIComponent createComponent(ValueBinding valuebinding,
			FacesContext facescontext, String s) throws FacesException {
		return null;
	}
	public Iterator getComponentTypes() {
		return null;
	}
	public void addConverter(String s, String s1) {
	}
	public void addConverter(Class class1, String s) {
	}
	public Converter createConverter(String s) {
		return null;
	}
	public Converter createConverter(Class class1) {
		return null;
	}
	public Iterator getConverterIds() {
		return null;
	}
	public Iterator getConverterTypes() {
		return null;
	}
	public MethodBinding createMethodBinding(String s, Class aclass[])
			throws ReferenceSyntaxException {
		return null;
	}
	public Iterator getSupportedLocales() {
		return null;
	}
	public void setSupportedLocales(Collection collection) {
	}
	public void addValidator(String s, String s1) {
	}
	public Validator createValidator(String s) throws FacesException {
		return null;
	}
	public Iterator getValidatorIds() {
		return null;
	}
	public ValueBinding createValueBinding(String s)
			throws ReferenceSyntaxException {
		return null;
	}
}