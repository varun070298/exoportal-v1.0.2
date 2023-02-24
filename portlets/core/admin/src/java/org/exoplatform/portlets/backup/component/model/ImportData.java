/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.backup.component.model;

import java.util.* ;
import java.util.jar.*;
import java.io.File ;
/**
 * Jun 2, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class ImportData {
	private String name_ ;
	private Date createdDate_ ;
  private Date modifiedDate_ ;
	private String resourceURL_ ;
	private List dataEntries_ ;
	private JarFile jarFile_ ;
	
	public ImportData(File file) {
		name_ = file.getName() ;
		Date date =  new Date(file.lastModified()) ;
    createdDate_ =  date ;
    modifiedDate_ = date ;
		resourceURL_ = file.getAbsolutePath() ;
	}
	
	public String getName() { return name_ ; }
	public Date   getCreatedDate() { return createdDate_ ; }
	public Date   getModifiedDate() { return modifiedDate_ ; }
	
	public String getResourceURL() { return resourceURL_ ; }
	
	public List getDataEntries() {
		if(dataEntries_ == null) {
			
		}
		return dataEntries_ ;
	}
	
	public Enumeration getEntries() { 
		try {
			JarFile jar = getJarFile() ;
			return jar.entries() ;
		} catch (Exception ex) {
			ex.printStackTrace() ;
		}
		return null ;
	}
	
	private JarFile  getJarFile() throws Exception {
		if(jarFile_ == null) {
			jarFile_ = new JarFile(new File(resourceURL_)) ;
		}
		return jarFile_ ;
	}
}