/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.backup.component;

import java.io.FileFilter;
import java.io.File ;

/**
 * Jun 2, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class ZipFileFilter implements FileFilter{
	public boolean accept(File file)  {
		if(file.getName().endsWith(".zip") && file.isFile()) return true ;
		return false ;
	}
}
