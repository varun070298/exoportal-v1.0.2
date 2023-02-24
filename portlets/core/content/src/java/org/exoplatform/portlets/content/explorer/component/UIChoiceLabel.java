/*
 * Created on Jan 25, 2005
 */
package org.exoplatform.portlets.content.explorer.component;

import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.LabelCell;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;

/**
 * @author benjaminmestrallet
 */
public class UIChoiceLabel extends UIGrid implements ExplorerListener{

  public UIChoiceLabel(){
    setId("UIChoiceLabel");
  
    add(new Row().
        add(new LabelCell("#{UIChoiceLabel.label.choice}")));        
  }
  
  
  public void  onChange(UIExplorer uiExplorer, NodeDescriptor node)  {
    if(node.isLeafNode()) setRendered(false) ;
    else setRendered(true) ;
  }
  
  public void  onModify(UIExplorer uiExplorer, NodeDescriptor node)  {}
  
  public void  onAddChild(UIExplorer uiExplorer, NodeDescriptor node)  { }
  
  public void  onRemove(UIExplorer uiExplorer, NodeDescriptor node)  {  }  
  
  
}
