/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.test;

import java.util.ResourceBundle;

import javax.faces.component.UIComponentBase ;
import org.exoplatform.services.log.LogService;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIComponent1.java,v 1.2 2004/06/03 22:51:36 tuan08 Exp $
 */
public class UIComponent1 extends UIComponentBase {
	static int counter = 0 ;
	
	public UIComponent1(LogService service) {
		
	}
	
	public UIComponent1(UIComponent2 component2, LogService service) {
		System.out.println("Call constructor  UIComponent1(UIComponent2, LogService)") ;
		System.out.println("LogService : " + service) ;
		System.out.println("UIComponent2 : " + component2) ;
	}
	
	public String getFamily() { return "" ; }
	
	public class UIInnerComponent extends UIComponentBase {
		public UIInnerComponent() {
			
		}
		
		public UIInnerComponent(LogService service) {
			System.out.println("=== > Call constructor  UIInnerComponent(LogService)") ;
			System.out.println("LogService : " + service) ;
			counter++ ;
			if (counter > 5) System.exit(0) ;
		}
		
		public String getFamily() { return "" ; }
	}
}
