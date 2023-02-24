/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.util.List ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 17, 2004
 * @version $Id$
 */
public class ComponentVisitor {
  public  void traverse(UIExoComponent component)  {
    visit(component) ;
    List children = component.getChildren() ;
    for(int i = 0; i < children.size() ; i++) {
      UIExoComponent child = (UIExoComponent) children.get(i);
      traverse(child) ;
    }
  }
  
  protected  void visit(UIExoComponent component)  {
    
  }
}
