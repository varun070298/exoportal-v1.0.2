/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor.jvm.v14;


import java.net.URL;
import org.exoplatform.container.monitor.jvm.OperatingSystemInfo;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 8, 2004
 * @version $Id$
 */
public class OperatingSystemInfoImpl implements OperatingSystemInfo {
  
  public OperatingSystemInfoImpl() {
  }
  
  public String getArch() { return "N/A" ; }

  public String getName() { return "N/A" ; } 
  
  public String getVersion() { return "N/A" ;}
  
  public int    getAvailableProcessors()  { return -1 ; }
  
  public URL createURL(String file)  throws Exception { 
    return new URL("file:" + file) ; 
  }
  
  public String toString() {
    StringBuffer b = new StringBuffer() ;
    b.append("Operating System: ").append(getName()).append("\n") ;
    b.append("Operating  System Version : ").append(getVersion()).append("\n") ;
    b.append("CPU Achitechure : ").append(getArch()).append("\n") ;
    b.append("Number of Processors : ").append(getAvailableProcessors()).append("\n") ;
    return b.toString() ;
  }
}