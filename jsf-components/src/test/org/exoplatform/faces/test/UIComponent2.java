/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.test;

import java.util.ResourceBundle ;
import javax.faces.component.UIComponentBase ;
import org.exoplatform.services.log.LogService;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIComponent2.java,v 1.3 2004/06/03 22:51:36 tuan08 Exp $
 */
public class UIComponent2 extends UIComponentBase {
	
	public UIComponent2() {
		
	}
	
	public UIComponent2(LogService service, ResourceBundle res) {
		System.out.println("Call constructor  UIComponent1(LogService)") ;
		System.out.println("LogService : " + service) ;
		System.out.println("ResourceBundle : " + res) ;
	}
	
	public String getFamily() { return "" ; }
}