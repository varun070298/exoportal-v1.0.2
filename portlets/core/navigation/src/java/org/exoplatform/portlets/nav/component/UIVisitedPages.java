/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.nav.component;

import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 31 aug 2004
 */
public class UIVisitedPages extends UIExoCommand {
  final static public int  DEFAULT_NUMBER = 5;
  final static public String MORE_ACTION = "more" ;
  final static public String LESS_ACTION = "less" ;
  final static public Parameter[] moreParams = {new Parameter(ACTION, MORE_ACTION) };
  final static public Parameter[] lessParams = {new Parameter(ACTION, LESS_ACTION) };
  
  private int  numberOfPages_ = DEFAULT_NUMBER  ;
  
	public UIVisitedPages() throws Exception {
		setId("UIVisitedPages");
		setRendererType("VisitedPagesRenderer");
    addActionListener(MoreActionListener.class, MORE_ACTION) ;
    addActionListener(LessActionListener.class, LESS_ACTION) ;
	}

  public String getFamily( ) { return "org.exoplatform.portlets.nav.component.UIVisitedPages" ; }

  public int getNumberOfPages() { return numberOfPages_ ; }
  
  
  static public class MoreActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIVisitedPages uiComponent =(UIVisitedPages)event.getSource() ;
      uiComponent.numberOfPages_ = -1 ;
    }
  }
  
  static public class LessActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIVisitedPages uiComponent =(UIVisitedPages)event.getSource() ;
      uiComponent.numberOfPages_ = DEFAULT_NUMBER;
    }
  }
}
