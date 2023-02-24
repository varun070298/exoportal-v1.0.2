/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.indexing.component;

import java.util.ResourceBundle;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.indexing.IndexerPlugin;
import org.exoplatform.services.indexing.IndexingService;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIListIndexer.java,v 1.2 2004/09/16 20:17:02 tuan08 Exp $
 */
public class UIListIndexer extends UIGrid {
  final static public String REINDEX_ACTION = "reindex" ;
  final static public String REMOVE_INDEX_ACTION = "removeIndex" ;
  final static public String MORE_ACTION = "more" ;
	static Parameter[] REMOVE_INDEX_PARAMS =  { new Parameter(ACTION , REMOVE_INDEX_ACTION) } ;
  static Parameter[] REINDEX_PARAMS =  { new Parameter(ACTION , REINDEX_ACTION) } ;
  static Parameter[] MORE_PARAMS =  { new Parameter(ACTION , MORE_ACTION) } ;
	
	private IndexerPluginDataHandler dataHandler_ ;
  private IndexingService iservice_ ; 
  private ResourceBundle res_ ;
	
	public UIListIndexer(IndexingService iservice, ResourceBundle res) throws Exception {
		setId("UIListIndexer") ;		
    iservice_ = iservice ;
    res_ = res ;  
		dataHandler_ = new IndexerPluginDataHandler() ;
    dataHandler_.setDatas(iservice_.getIndexerPlugins()) ;
		add(new Rows(dataHandler_, "even", "odd").
				add(new Column("#{UIListIndexer.header.name}", "name")).
				add(new Column("#{UIListIndexer.header.description}", "description")).
				add(new ActionColumn("#{UIListIndexer.header.action}", "identifier").
						add(true, new Button("#{UIListIndexer.button.reindex}", REINDEX_PARAMS)).
            add(true, new Button("#{UIListIndexer.button.remove-index}", REMOVE_INDEX_PARAMS)).
            add(true, new Button("#{UIListIndexer.button.more}", MORE_PARAMS)))) ;
		
    addActionListener(ReindexActionListener.class, REINDEX_ACTION ) ;
    addActionListener(RemoveIndexActionListener.class, REMOVE_INDEX_ACTION ) ;
		addActionListener(MoreActionListener.class, MORE_ACTION) ;
	}
	
	
	public class IndexerPluginDataHandler extends CollectionDataHandler {
		private IndexerPlugin plugin_   ;
		
		public String  getData(String fieldName)   {
			if("name".equals(fieldName)) 
        return  res_.getString("UIListIndexer." + plugin_.getPluginIdentifier() + ".name");
			if("description".equals(fieldName)) 
        return  res_.getString("UIListIndexer." + plugin_.getPluginIdentifier() + ".description");
			if("identifier".equals(fieldName)) 
        return plugin_.getPluginIdentifier() ;
			return null ;
		}
		
		public void setCurrentObject(Object o) { plugin_ = (IndexerPlugin) o; }
	}
	
	
  static public class MoreActionListener extends ExoActionListener  {
    public MoreActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIListIndexer uiList= (UIListIndexer) event.getComponent() ;
      String objectId = event.getParameter(OBJECTID) ;
      UIPortlet uiPortlet = (UIPortlet)  uiList.getParent() ;
      if(uiPortlet.findComponentById(objectId) == null) {
        InformationProvider iprovider = findInformationProvider(uiList) ;
        iprovider.addMessage(new ExoFacesMessage("#{UIListIndexer.msg.not-support-extension}")) ;
      } else {
        uiPortlet.setRenderedComponent(objectId) ;
      }
    }
  }
  
	static public class ReindexActionListener extends ExoActionListener  {
    public ReindexActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
		public void execute(ExoActionEvent event) throws Exception {
      UIListIndexer uiList = (UIListIndexer) event.getComponent() ;
      String objectId = event.getParameter(OBJECTID) ;
      IndexerPlugin plugin = uiList.iservice_.getIndexerPlugin(objectId) ;
      plugin.reindex() ;
		}
	}
  
  static public class RemoveIndexActionListener extends ExoActionListener  {
    public RemoveIndexActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIListIndexer uiList = (UIListIndexer) event.getComponent() ;
      String objectId = event.getParameter(OBJECTID) ;
      IndexerPlugin plugin = uiList.iservice_.getIndexerPlugin(objectId) ;
      plugin.removeIndex() ;
    }
  }
}