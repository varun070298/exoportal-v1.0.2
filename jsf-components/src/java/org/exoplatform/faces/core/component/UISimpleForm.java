/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.ValidatorManager;
import org.exoplatform.faces.core.Util;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UISimpleForm.java,v 1.23 2004/08/19 14:53:44 tuan08 Exp $
 */
public class UISimpleForm extends UIGrid {
  public static final String  COMPONENT_FAMILY = "org.exoplatform.faces.core.component.UISimpleForm" ;
  final public static String  SIMPLE_FORM_RENDERER  = "SimpleFormRenderer";
  final static public short ALL_MODE = 0 ;
  final static public short VIEW_MODE = 1 ;
  final static public short EDIT_MODE = 2 ;
  
  private List                hiddenInputs_;
  private boolean              error_;
  private String              formName_;
  private String              method_;
  private String              formId_;
  private String              userAction_;
  protected List              validators_ ;
  private String              script_;
  private short               mode_ ;

  public UISimpleForm(String name, String method, String formId) {
    hiddenInputs_ = new ArrayList(3);
    formName_ = name;
    method_ = method;
    formId_ = formId;
    if (formId_ == null) formId_ = name ;
    setRendererType(SIMPLE_FORM_RENDERER);
    setId(name) ;
    mode_ = EDIT_MODE ;
  }

  public boolean hasError() { return error_; }
  public void setError(boolean b) {  error_ = b; }

  public String getFormName() {  return formName_; }

  public String getMethod() {  return method_; }

  public String getFormId() {  return formId_; }

  public String getUserAction() { return userAction_; }

  public void setUserAction(String userAction) { userAction_ = userAction; }

  public List getHiddenInputs() { return hiddenInputs_;}

  public String getScript() {
    if (script_ == null) script_ = getNormalScript();
    return script_;
  }

  public UISimpleForm add(UIHiddenInput input) {
    getChildren().add(input);
    hiddenInputs_.add(input);
    return this;
  }
  
  public void addValidator(Validator validator) {
    if(validators_ == null ) validators_ = new ArrayList(3) ;
    validators_.add(validator) ;
  }
  
  public void addValidator(Class clazz) {
    if(validators_ == null ) validators_ = new ArrayList(3) ;
    ComponentUtil.addValidator(validators_, clazz) ;
  }
  
  public short getMode() { return mode_ ; }
  public void setMode(short mode) {
    List children = getChildren() ;
    for(int i = 0; i < children.size(); i++) {
      Object child = children.get(i) ;
      if(child instanceof UIInput) {
        UIInput input = (UIInput) child ;
        if(VIEW_MODE == mode)  { 
          input.setReadonly(true) ;
          mode_ = VIEW_MODE ;
        }  else { 
          input.setReadonly(false) ;
          mode_ = EDIT_MODE ;
        }
      }
    }
  }
  
  public void decode(FacesContext context) {
  	getRenderer(context).decode(context, this) ;
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

  final public  void processValidators(FacesContext context) {
    if(!isRendered()) return ;
    List children = getChildren() ;
    for(int i = 0; i < children.size(); i++) {
      UIComponent uiChild = (UIComponent) children.get(i) ;
      if(uiChild.isRendered()) {
        uiChild.processValidators(context) ;
      }
    }
    if(context.getRenderResponse()) return ;
    error_ = false ;
    if(validators_ != null) {
      Validator validator = null ;
      try {
        for(int i = 0; i < validators_.size(); i++) {
          validator = (Validator) validators_.get(i) ;
          validator.validate(context, this, null) ;
        }
      } catch(ValidatorException ex) {
        error_ = true ;
        FacesMessage message = ex.getFacesMessage() ;
        InformationProvider iprovider = Util.findInformationProvider(this) ;
        iprovider.addMessage(message) ;
        context.renderResponse() ;
      }
    }
  }
  
  private String getNormalScript() {
    String script =
    "<script type='text/javascript'>\n" +
    "  //ie bug  you cannot have more than one button tag\n" +
    "  function submit_" + formName_ + "(action) {\n" +
    "    document." + formName_ + ".elements['" + FacesConstants.ACTION + "'].value = action ;\n" +
    "    document." + formName_ + ".submit();\n" +
    "  }\n" +
    "</script>\n";
    return script;
  }
  
  public void setNoScript() {
      script_ = "";
  }
  
  public void setEnhancedScript(String taId, String width, String height) {
    String script =
    "<script type='text/javascript'>\n" +
		"  _editor_url = '/htmlarea';\n" +
		"  _editor_lang = 'en'; \n" +
		"</script>" +
    "<script type='text/javascript' src='/htmlarea/htmlarea.js'></script>\n" +
    "<script type='text/javascript'>\n" +
    "  var editor = null;\n" +
    "  function initEditor() {\n" +
    "    editor = new HTMLArea('" + taId + "');\n" +
    "    var cfg = editor.config; // this is the default configuration\n" +
    //"    cfg.height = '" + height + "';\n" +
    //"    cfg.width = '" + width + "';\n" +
    "    editor.generate();\n" +
    "    editor._iframe.style.width = \"" + width + "\";" +
    "    editor._textArea.style.width = \"" + width + "\";" +
    "    editor._iframe.style.height = \"" + height + "\";" +
    "    editor._textArea.style.height = \"" + height + "\";" +
    "  }\n" +
		
    "  //ie bug  you cannot have more than one button tag\n" +
    "  function submit_" + formName_ + "(action) {\n" +
    "    document." + formName_ + ".elements['" + ACTION + "'].value = action ;\n" +
    "    document." + formName_ + ".onsubmit();\n" +
    "    document." + formName_ + ".submit();\n" +
    "  }\n" +
    "  initEditor() ;\n" +
    "</script>\n";
    script_ = script;
  }
  
  public String getFamily() {  return COMPONENT_FAMILY ; }
  
}