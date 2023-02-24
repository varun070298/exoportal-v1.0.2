/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import java.io.FileOutputStream;
import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.content.explorer.component.model.FileNodeDescriptor;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;

/**
 * Jun 22, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIFileContentEditor.java,v 1.3 2004/08/17 13:07:02 tuan08 Exp $
 */
public class UIFileContentEditor extends UIContentEditor {
  
	public UIFileContentEditor() {
		addActionListener(SaveContentActionListener.class, SAVE_CONTENT_ACTION)  ;
	}
	
	public void  onChange(UIExplorer uiExplorer, NodeDescriptor node)  {
    String mimeType = node.getNodeType() ;
    if (!mimeType.startsWith("text")) {
      setRendered(false) ;
      return ;
    }
    FileNodeDescriptor fnode = (FileNodeDescriptor) node ;
    String content = null ;
    if(fnode.getCacheContent() == null) {
      UIFileExplorer uiFileExplorer = (UIFileExplorer)uiExplorer;
      String path = uiFileExplorer.getRealPathBaseDir() + uiExplorer.getSelectNode().getUri() ;
      try {
        content = IOUtil.getFileContenntAsString(path) ;
        fnode.setCacheContent(content) ;
      } catch (Exception ex) {
        content = ExceptionUtil.getExoStackTrace(ex) ;
      }
    } else {
      content = fnode.getCacheContent();
    }
    setContent(content) ;
    setRendered(true) ;
    setContentType(mimeType) ;
    setRendererType(CONTENT_FORM_RENDERER) ;
  }
	
	static public class SaveContentActionListener extends ExoActionListener {
	  public void execute(ExoActionEvent event) throws Exception {
	    UIFileContentEditor uiEditor = (UIFileContentEditor) event.getComponent() ;
      UIFileExplorer uiExplorer = 
        (UIFileExplorer) uiEditor.getAncestorOfType(UIFileExplorer.class) ;
	    InformationProvider iprovider = findInformationProvider(uiEditor) ;
	    String content =  uiEditor.content_.getValue() ; 
      FileNodeDescriptor fnode =(FileNodeDescriptor) uiExplorer.getSelectNode() ;
	    String uri = fnode.getUri() ;
	    String realPath = uiExplorer.getRealPathBaseDir() + uri  ;
	    FileOutputStream os= new FileOutputStream(realPath) ;
	    os.write(content.getBytes()) ;
	    os.close() ;
      fnode.setCacheContent(content) ;
	    uiExplorer.broacastOnModify(fnode) ;
	    iprovider.addMessage(new ExoFacesMessage("#{UIFileContentEditor.msg.save-file-success}")) ;
	  }
	}
}