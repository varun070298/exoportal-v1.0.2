/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component;

import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIHtmlTextArea;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.core.validator.EmptyFieldValidator;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portlets.content.ContentUtil;
import org.exoplatform.portlets.content.display.component.model.ContentConfig;
import org.exoplatform.services.portal.model.Node;
/**
 * Sat, Jan 03, 2004 @ 11:16
 * @author: Ove Ranheim
 * @email: oranheim@users.sourceforge.netx
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: UIContentHtmlEditorForm.java,v 1.2 2004/06/15 23:09:18
 *           oranheim Exp $
 */
public class UIContentEditor extends UISimpleForm {
	
	private UIStringInput nameInput_;
	private UIStringInput titleInput_;
	private UIHtmlTextArea editorInput_;
	private ContentConfig config_;
	
	public UIContentEditor()	{
		super("editorForm", "post", null);
		setId("UIContentEditor");
		setClazz("UIContentEditor");
		nameInput_ = new UIStringInput("name", "").
		             addValidator(EmptyFieldValidator.class) ;
		titleInput_ = new UIStringInput("title", "").
		              addValidator(EmptyFieldValidator.class) ;
		editorInput_ = new UIHtmlTextArea("htmlEditor", "", "100%", "500px") ;		
		add(new HeaderRow().
				add(new Cell("#{UIContentEditor.header.content-html-editor-form}").
						addColspan("2")));
		add(new Row().
				add(new LabelCell("#{UIContentEditor.label.name}")).
				add(new ComponentCell(this, nameInput_)));
		add(new Row().
				add(new LabelCell("#{UIContentEditor.label.title}")).
				add(new ComponentCell(this, titleInput_)));
		add(new Row().
				add(new ComponentCell(this, editorInput_).addColspan("2")));
		add(new Row().
				add(new ListComponentCell().
						add(new FormButton("#{UIContentEditor.button.save}", SAVE_ACTION)).
						add(new FormButton("#{UIContentEditor.button.cancel}", CANCEL_ACTION)).
						addColspan("2").addAlign("center")));
		 addActionListener(CancelActionListener.class, CANCEL_ACTION)  ;
		 addActionListener(SaveActionListener.class, SAVE_ACTION)  ;
	}
	
	public ContentConfig getContentConfig() { return config_; }
	
	public void setContentConfig(ContentConfig config) {
		config_ = config;
		if (config == null) {
			nameInput_.setText("");
			titleInput_.setText("");
			editorInput_.setValue("");
		} else {
			nameInput_.setText(config.getName());
			titleInput_.setText(config.getTitle());
      ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
      Node selectedNode = portal.getSelectedNode();
			String content = ContentUtil.resolveContent(config, selectedNode);
      if(content == null){
        content = "No content found";
      }
			content = content.replaceAll("<", "&lt;");
			content = content.replaceAll(">", "&gt;");            
			editorInput_.setValue(content);
		}
	}
	
	
	static public class CancelActionListener extends ExoActionListener {
	  public void execute(ExoActionEvent event) throws Exception {
	    UIContentEditor uiEditor = (UIContentEditor) event.getSource() ;
	    uiEditor.setRenderedSibling(UIContentConfig.class) ;
	  }
	}
	
	static public class SaveActionListener extends ExoActionListener {
	  public void execute(ExoActionEvent event) throws Exception {
	    UIContentEditor uiEditor = (UIContentEditor) event.getSource() ;
	    if (uiEditor.config_ == null) {
	      uiEditor.config_ = new ContentConfig();
	    }
	    uiEditor.config_.setName(uiEditor.nameInput_.getValue());
	    uiEditor.config_.setTitle(uiEditor.titleInput_.getValue());
	    
	    uiEditor.config_.setContent(uiEditor.editorInput_.getValue());
	    UIContentConfig uiConfig = 
	      (UIContentConfig)uiEditor.getSibling(UIContentConfig.class);
	    uiConfig.saveContentConfig(ContentUtil.MODIFY_STATE, uiEditor.config_);
	    uiEditor.setRenderedSibling(UIContentConfig.class) ;
	  }
	}
}