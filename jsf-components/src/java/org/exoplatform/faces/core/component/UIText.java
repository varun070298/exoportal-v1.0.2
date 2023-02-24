/*******************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.      *
 * Please look at license.txt in info directory for more license detail.       *
 *******************************************************************************/
package org.exoplatform.faces.core.component;

import org.exoplatform.commons.utils.HtmlUtil;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UITextArea;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.FormButton;
import org.exoplatform.faces.core.component.model.ListComponentCell;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
/**
 * Wed, March 22, 2004 @ 23:14 
 * @author: Tuan Nguyen	
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPageConfiguration.java,v 1.3 2004/08/07 18:11:24 tuan08 Exp $
 */
public class UIText extends UISimpleForm {
  final static public String PREVIEW_MODE_ACTION = "previewMode" ;
  final static public String EDIT_MODE_ACTION = "editMode" ;
  
  private UITextArea textInput_ ;
  private ListComponentCell actions_ ;
  
  public UIText() {
    super("textForm", "post", null) ;
    setId("UIText");
    textInput_ = new UITextArea("text" , "") ;
    add(new Row().
        add(new ComponentCell(this, textInput_))) ;
    actions_ = new ListComponentCell() ;
    add(new Row().
        add(actions_.addColspan("2").addAlign("center"))) ;
  }
  
  public void addEditButton(String label, String action, Class listener) {
    actions_.add(new FormButton(label,  action, EDIT_MODE)) ;
    addActionListener(listener, action) ;
  }
  
  public void addViewButton(String label, String action, Class listener) {
    actions_.add(new FormButton(label,  action, VIEW_MODE)) ;
    addActionListener(listener, action) ;
  }
  
  public String getText() { return textInput_.getValue(); }
  public void setText(String  page) {
    if (page != null) 	textInput_.setValue(page) ;   
    else  	textInput_.setValue("") ;   
  }
  
  public void setXMLText(String  text) {
    if (text != null)   {
      text = HtmlUtil.showXmlTags(text) ;
      textInput_.setValue(text) ;   
    }  else {
      textInput_.setValue("") ;   
    }
  }
  
  public class  PreviewModeActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
		  UIText uiText = (UIText) event.getSource();
      uiText.setMode(VIEW_MODE) ;
		}
  }
  
  public class  EditModeActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
      UIText uiText = (UIText) event.getSource();
      uiText.setMode(VIEW_MODE) ; 
		}
  }
}