/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.test;

import org.exoplatform.faces.UIComponentFactory;
import org.exoplatform.faces.core.renderer.xhtmlmp.SimpleFormRenderer;
import org.exoplatform.test.BasicTestCase;
/**
 * Thu, May 15, 2004 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestUIComponentFactory.java,v 1.4 2004/08/05 14:58:42 tuan08 Exp $
 * @email: tuan08@yahoo.com
 */
public class TestUIComponentFactory extends BasicTestCase {
    
  public TestUIComponentFactory(String name) {
    super(name);
  }

  public void setUp() throws Exception {
  	
  }
  
  public void tearDown() throws Exception {

  }
  
  public void testFactory() throws Exception {
  	UIComponentFactory factory = new UIComponentFactory(null) ;
  	//factory.createUIComponent("org.exoplatform.faces.test.UIComponent1") ;
  	factory.createUIComponent("org.exoplatform.faces.test.UIComponent1$UIInnerComponent") ;
  	//Class clazz = Class.forName("org.exoplatform.faces.test.UIComponent1$UIInnerComponent") ;
  	//System.out.println(UIComponent1.UIInnerComponent.class.getName()) ;
  }
  
  public void testGetLabel() throws Exception {
    SimpleFormRenderer renderer = new SimpleFormRenderer() ;
    String s = renderer.getLabel("<img src='/skin/blank.gif'/>test") ;
    System.out.println("label = " + s) ;
  }
}