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
public class Parameters {
  final static public Parameter[] EMPTY = new Parameter[0] ; 
  Parameter[] parameter_ ;
  
  public Parameters(String s) {
    s = s.trim() ;
    String[] temp = s.split("|") ;
    parameter_ = new Parameter[temp.length] ;
    for(int i = 0 ; i < temp.length; i++) {
      temp[i] = temp[i].trim() ;
      parameter_[i] = toParameter(temp[i]) ;
    }
  }
  
  private Parameter toParameter(String s) {
    String[] temp = s.split("=") ;
    return  new Parameter(temp[0], temp[1]) ;
  }
}