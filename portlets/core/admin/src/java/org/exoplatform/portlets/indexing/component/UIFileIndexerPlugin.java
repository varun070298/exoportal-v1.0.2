/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.indexing.component;

import org.exoplatform.faces.core.component.UICheckBox;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.indexing.FileIndexerPlugin;
import org.exoplatform.services.indexing.IndexingService;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIFileIndexerPlugin.java,v 1.2 2004/09/17 16:59:12 tuan08 Exp $
 */
public class UIFileIndexerPlugin extends  UISimpleForm {
  final static public String INDEX_DIRECTORY = "index" ;
  final static public String REINDEX_DIRECTORY = "reindex" ;
  
  private UIStringInput directoryInput_ ;
  private UIStringInput acceptExtInput_ ;
  private UICheckBox recursiveInput_ ;
  private FileIndexerPlugin indexer_ ;
  
  public UIFileIndexerPlugin(IndexingService iservice) throws Exception {
    super("fileIndexerPlugin", "post", null) ;
  	setId(FileIndexerPlugin.IDENTIFIER) ;    
    indexer_ = (FileIndexerPlugin)iservice.getIndexerPlugin(FileIndexerPlugin.IDENTIFIER) ;
    
    directoryInput_ = new UIStringInput("directory", "") ;
    acceptExtInput_ = new UIStringInput("acceptExt", "html, txt, xml, java") ;
    recursiveInput_ = new UICheckBox("recursive", "true") ;
    recursiveInput_.setSelect(true) ;
   
    add(new HeaderRow().
        add(new Cell("#{UIFileIndexerPlugin.header.file-indexer}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIFileIndexerPlugin.label.directory}")).
        add(new ComponentCell(this, directoryInput_)));
    add(new Row().
        add(new LabelCell("#{UIFileIndexerPlugin.label.accept-extension}")).
        add(new ComponentCell(this, acceptExtInput_)));
    add(new Row().
        add(new LabelCell("#{UIFileIndexerPlugin.label.recursive-index}")).
        add(new ComponentCell(this, recursiveInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UIFileIndexerPlugin.button.index-directory}", INDEX_DIRECTORY)).
            add(new FormButton("#{UIFileIndexerPlugin.button.reindex-directory}", REINDEX_DIRECTORY)).
            add(new FormButton("#{UIFileIndexerPlugin.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    
    addActionListener(ShowListIndexerPluginActionListener.class, CANCEL_ACTION) ;
    addActionListener(IndexActionListener.class, INDEX_DIRECTORY) ;
    addActionListener(ReindexActionListener.class, REINDEX_DIRECTORY) ;
  }
  
  static public class IndexActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIFileIndexerPlugin uiComponent = (UIFileIndexerPlugin) event.getComponent() ;
      String directory = uiComponent.directoryInput_.getValue() ;
      String acceptExt =  uiComponent.acceptExtInput_.getValue() ;
      String[] ext = acceptExt.split(",") ;
      boolean recursive = "true".equals( uiComponent.recursiveInput_.getValue());
      uiComponent.indexer_.indexDirectory(directory, null ,ext, recursive) ;
    }
  }
  
  static public class ReindexActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIFileIndexerPlugin uiComponent = (UIFileIndexerPlugin) event.getComponent() ;
      String directory = uiComponent.directoryInput_.getValue() ;
      String acceptExt =  uiComponent.acceptExtInput_.getValue() ;
      String[] ext = acceptExt.split(",") ;
      boolean recursive = "true".equals( uiComponent.recursiveInput_.getValue());
      uiComponent.indexer_.reindexDirectory(directory, null, ext, recursive) ;
    }
  }
}