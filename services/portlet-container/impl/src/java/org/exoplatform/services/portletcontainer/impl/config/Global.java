/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.config;

/**
 * Jul 7, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Global.java,v 1.1 2004/07/08 19:11:45 tuan08 Exp $
 */
public class Global {
	private String name ;
	private String description ;
	private int majorVersion ;
	private int minorVersion ;
    
	public String getDescription() {
		return description;
	}
    
	public void setDescription(String description) {
		this.description = description;
	}
    
	public int getMajorVersion() {
		return majorVersion;
	}
    
	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}
    
	public int getMinorVersion() {
		return minorVersion;
	}
    
	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
