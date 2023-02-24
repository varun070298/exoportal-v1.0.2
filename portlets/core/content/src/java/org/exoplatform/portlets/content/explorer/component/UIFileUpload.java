/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import java.io.FileOutputStream;
import java.util.Iterator;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 20, 2005
 * @version $Id$
 */
public class UIFileUpload extends org.exoplatform.faces.core.component.UIFileUpload 
  implements ExplorerListener {
  
  public UIFileUpload() {
    setShowCancel(false) ;
    setShowHeader(false) ;
  }
  
  public void addFileSystemActionListener()  {
    addActionListener(FileSystemUploadActionListener.class, UPLOAD_ACTION) ;
  }
  
  public void  onChange(UIExplorer uiExplorer, NodeDescriptor node)  {
    if(node.isLeafNode()) setRendered(false) ;
    else setRendered(true) ;
  }
  
  public void  onModify(UIExplorer uiExplorer, NodeDescriptor node)  {}
  
  public void  onAddChild(UIExplorer uiExplorer, NodeDescriptor node)  { }
  
  public void  onRemove(UIExplorer uiExplorer, NodeDescriptor node)  {  }
  
  
  static public class FileSystemUploadActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIFileUpload uiFileUpload = (UIFileUpload) event.getComponent() ;
      UIFileExplorer uiExplorer = 
        (UIFileExplorer) uiFileUpload.getAncestorOfType(UIFileExplorer.class) ;
      NodeDescriptor node = uiExplorer.getSelectNode() ;
      InformationProvider iprovider = findInformationProvider(uiFileUpload)  ;
      String dir = uiExplorer.getRealPathBaseDir() + node.getUri() ;
      Iterator i = uiFileUpload.getUserInputs().iterator() ;
      while(i.hasNext()) {
        UIFileUpload.UserInput input = (UIFileUpload.UserInput) i.next() ;
        String fileName = input.getName() ;
        if (fileName == null || fileName.length() == 0) fileName = input.getFileName() ;
        String realPath = dir + "/" + fileName ;
        FileOutputStream os = new FileOutputStream(realPath) ;
        os.write(input.getStream()) ;
        os.close() ;
      }
      uiExplorer.broadcastOnAddChild(node) ;
      iprovider.addMessage(new ExoFacesMessage("#{UIFilePortlet.msg.file-upload-success}")) ;
    }
  }
}
