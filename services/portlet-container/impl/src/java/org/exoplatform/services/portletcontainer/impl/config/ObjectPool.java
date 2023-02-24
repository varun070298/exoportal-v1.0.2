/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.config;

/**
 * Jul 7, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ObjectPool.java,v 1.1 2004/07/08 19:11:45 tuan08 Exp $
 */
public class ObjectPool {
	private int instancesInPool ;
    
	public int getInstancesInPool() {
		return instancesInPool;
	}
	public void setInstancesInPool(int instanceInPool) {
		this.instancesInPool = instanceInPool;
	}
}
