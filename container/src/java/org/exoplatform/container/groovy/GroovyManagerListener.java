/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.groovy;

import java.util.List ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 9, 2004
 * @version $Id$
 */
public class GroovyManagerListener {
  
  public void load(GroovyObject gobject) throws Exception {  
    gobject.setObject(gobject.getType().newInstance()) ;
  }
  
  public void reload(GroovyObject gobject) throws Exception {  
    gobject.setObject(gobject.getType().newInstance()) ;
  }
  
  
  public void unload(GroovyObject gobject) throws Exception {  
    gobject.setObject(null) ;
  }
  
  static public void load(List listeners, GroovyObject object) throws Exception {
    for(int i = 0; i < listeners.size(); i++) {
      GroovyManagerListener listener = (GroovyManagerListener) listeners.get(i) ;
      listener.load(object) ;
    }
  }
  
  static public void reload(List listeners, GroovyObject object) throws Exception {
    for(int i = 0; i < listeners.size(); i++) {
      GroovyManagerListener listener = (GroovyManagerListener) listeners.get(i) ;
      listener.reload(object) ;
    }
  }
  
  static public void unload(List listeners, GroovyObject object) throws Exception {
    for(int i = 0; i < listeners.size(); i++) {
      GroovyManagerListener listener = (GroovyManagerListener) listeners.get(i) ;
      listener.unload(object) ;
    }
  }
}