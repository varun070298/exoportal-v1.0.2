/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.groovy;

import org.exoplatform.container.ExoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.defaults.AbstractComponentAdapter;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 9, 2004
 * @version $Id$
 */
public class ExoGroovyComponentAdapter extends AbstractComponentAdapter {
  private GroovyObject gobject_ ;
  
  public ExoGroovyComponentAdapter(GroovyObject gobject)  {
    super(gobject.getType(), gobject.getType()) ;
    gobject_ = gobject ;
  }
  
  public Object getComponentInstance(PicoContainer container) {
    Object obj = gobject_.getObject() ;
    if(obj == null) {
      try {
        synchronized(container) {
          ExoContainer exoContainer = (ExoContainer) container ;
          obj = exoContainer.createComponent(gobject_.getType());
          gobject_.setObject(obj) ;
        }
      } catch (Exception ex) {
        throw new PicoInitializationException("Cannot instantiate script: " + gobject_.getGroovyResource(), ex) ;
      }
    }
    return obj ;
  }
  
  public void verify(PicoContainer container)  {
    
  }
}
