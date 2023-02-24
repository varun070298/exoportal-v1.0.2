/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.commons.xhtml;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 7, 2004
 * @version $Id$
 */
public class Attributes {
  StringBuffer b = null ;
  
  public Attributes() {
    b = new StringBuffer(50) ;
  }
  
  public Attributes(String s) {
    b = new StringBuffer(s.length()) ;
    String[] temp = s.split("\\|") ;
    for(int i = 0 ; i < temp.length; i++) {
      temp[i] = temp[i].trim() ;
      appendAttribute(temp[i]) ;
    }
  }
  
  public void addAttribute(String name, String value) {
    b.append(" ").append(name).append("='").append(value).append("'") ;
  }
  
  private void appendAttribute(String s) {
    String[] temp = s.split(":") ;
    if(temp.length != 2) return ;
    b.append(" ").append(temp[0].trim()).append("='").append(temp[1].trim()).append("'" );
  }
  
  public String toString() { return b.toString() ; }
}