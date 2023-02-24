/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import java.io.IOException;
import java.util.List;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.Cell;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.services.communication.forum.Category;
import org.exoplatform.services.communication.forum.ForumService;

/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIViewCategories.java,v 1.8 2004/10/20 18:46:31 benjmestrallet Exp $
 */
public class UIViewCategories extends UIGrid {
  protected static Log log_ = getLog("org.exoplatform.portlets.communication.forum") ;
  private ForumService service_ ;
  private long lastModifiedTime_  ;
  
  public UIViewCategories(ForumService service) throws Exception {
    super() ;
    setId("UIViewCategories") ;
    service_ = service  ;
    lastModifiedTime_ = service.getLastModifiedTime() ;    
    buildTree() ;
  }
  
  private void buildTree() throws Exception {
    getChildren().clear() ;
    getRows().clear() ;
    List categories = service_.getCategories();
    if(categories.size() > 0) {
      for (int i = 0; i < categories.size(); i++) {
        Category category = (Category) categories.get(i) ;
        UICategory uiCategory = new UICategory(service_, category) ;
        add(new Row().
            add(new ComponentCell(this, uiCategory))) ;
      }
    } else {
      add(new Row().
          add(new Cell("#{UIViewCategories.msg.no-category}").addColspan("2"))) ;
    }
    lastModifiedTime_ = service_.getLastModifiedTime() ;
  }
  
  public void encodeBegin(FacesContext context) throws IOException {
    if(lastModifiedTime_ < service_.getLastModifiedTime()) {
      try {
        buildTree();
      } catch (Exception ex) {
        log_.error("Error: " , ex) ;
      }
    }
    super.encodeBegin(context) ;
  }   
  
}