/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

import java.util.* ;

/**
 * Jul 19, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ValuesParam.java,v 1.1 2004/10/20 20:54:13 tuan08 Exp $
 */
public class ValuesParam extends  Parameter {
  
	private List		values = new ArrayList(2);

	public List getValues() {	return values; }
	public void setValues(List values) { this.values = values; }
  
  public String getValue() {
   if(values.size() == 0) return null ;
   return (String) values.get(0) ;
  }
  
  public void addValue(String value) { values.add(value) ;}
}