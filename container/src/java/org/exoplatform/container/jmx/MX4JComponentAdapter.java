/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by James Strachan and Mauro Talevi                          *
 *****************************************************************************/
package org.exoplatform.container.jmx;

import org.exoplatform.container.ExoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.AbstractComponentAdapter;
/**
 * @author James Strachan
 * @author Mauro Talevi
 * @author Jeppe Cramon
 * @author Benjamin Mestrallet 
 * @version $Revision: 1.5 $
 */
public class MX4JComponentAdapter extends AbstractComponentAdapter {
	
  private Object instance_ ;
	
	public MX4JComponentAdapter(Object key, Class implementation) {
    super(key, implementation) ;
	}
	
	public Object getComponentInstance(PicoContainer container) {
    if(instance_ != null ) return instance_ ;
    ExoContainer exocontainer = (ExoContainer) container ;
    try {
      synchronized(container) {
        instance_ = exocontainer.createComponent(getComponentImplementation()) ;
        exocontainer.manageMBean(this.getComponentKey(), instance_) ;
      }
    } catch (Exception ex) {
      throw new RuntimeException("Cannot instantiate component " + getComponentImplementation(), ex) ;
    }
		return instance_ ;
	}
  
   public void verify(PicoContainer container)  {  }
}