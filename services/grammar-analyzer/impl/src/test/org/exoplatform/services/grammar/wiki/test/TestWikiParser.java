/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.grammar.wiki.test;

import java.util.HashMap;
import net.sf.cglib.util.StringSwitcher;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.grammar.wiki.WikiEngineService;
import org.exoplatform.test.BasicTestCase;
/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestBBCodeParser.java,v 1.1 2004/10/20 14:19:00 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestWikiParser extends BasicTestCase {
  private WikiEngineService  service_ ;
  public TestWikiParser(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    service_ = (WikiEngineService) pcontainer.getComponentInstanceOfType(WikiEngineService.class) ;
  }

  public void tearDown() throws Exception {
  }
  
  public void testParsingContext() throws Exception {
    String text = "this is a ssssaa--test strike-- , **bold**  ~~test italic~~  ---this is test--~~--" +
                  "\n 1.1 title **bold** *cdsncjhjdsvjfdvjkj vfdnvfdvj --* **tes bold --strike**" +
                  "\n 1. this is a test"  +
                  "\n a. this is an __underline__ test"  +
                  "\n i. title ~~italic~~ abd fdnvbjk"  +
                  "\n i. *lllllllllllllllllllllllllv bhdfhvfhdbv"  ;
    System.out.println(service_.toXHTML(text)) ;
  }
  
  public void testSmiley() throws Exception {
    String text = "smiley :-) :-( :-D  :-)))" ;
    System.out.println(service_.toXHTML(text)) ;
  }
  
  public void testList() throws Exception {
    String text = "\n * this is a test"  +
                  "\n ** this is a test 2 stars "  +
                  "\n ** this is a test 2 stars"  +
                  "\n *** this is a test 3 stars" +
                  "\n * this is a test 1 stars "  +
                  "\n ** this is a test 2 stars"  +
                  "\n\n------------------------------------------------" +
                  "\n ** this is a test 2 stars "  +
                  "\n ** this is a **bold** test 2 stars"  +
                  "\n *** this is a test 3 stars" ;
    System.out.println(service_.toXHTML(text)) ;
  }
  
  public void testEnumerated() throws Exception {
    String text = "Test Enumerated"  +
                  "\n 1. this is a test 1 stars "  +
                  "\n 1. this is a test 2 stars"  +
                  "\n 1. this is a test 3 stars"  ;
    System.out.println(service_.toXHTML(text)) ;
  }
  
  public void testAlphabeticalEnumerated() throws Exception {
    String text = "Test Roman Enumerated"  +
                  "\n a. this is a test 1 stars "  +
                  "\n a. this is a test 2 stars"  +
                  "\n a. this is a test 3 stars"  ;
    System.out.println(service_.toXHTML(text)) ;
  }
  
  public void testRomanEnumerated() throws Exception {
    String text = "Test Roman Enumerated"  +
                  "\n i. this is a test i. "  +
                  "\n i. this is a test ii. "  +
                  "\n i. this is a test iii."  ;
    System.out.println(service_.toXHTML(text)) ;
  }
  
  public void testTextHandler() throws Exception {
    String text = "text: {text}this is text{text} end" ;
    System.out.println(service_.toXHTML(text)) ;
  }
  
  public void testJavaHandler() throws Exception {
    String text = "text: {java}this is text{java} end" ;
    System.out.println(service_.toXHTML(text)) ;
  } 
  
  public void testLink() throws Exception {
    String text = "test link [www.yahoo.com] , [yahoo>www.yahoo.com]" ;
    System.out.println(service_.toXHTML(text)) ;
  }
  
  public void testTitle() throws Exception {
    String text = "Test Title"  +
                  "\n 1  this is a title 1 "  +
                  "\n 1.1 this is a title 1.1 " ;
    System.out.println(service_.toXHTML(text)) ;
  }
  
  public void testStwitcher() throws Exception {
    String[] keys = {"abc", "def" , "xgy"  , "agf", "test1", "test2", "test3", "verylonglongtext"} ;
    int[]    nums = {1, 2, 3, 4, 5, 6, 7, 8} ; 
    StringSwitcher switcher = StringSwitcher.create(keys, nums, false) ;
    assertEquals("abc , expect 1: ", 1, switcher.intValue("abc") ) ;
    assertEquals("def , expect 2: ", 2, switcher.intValue("def")) ;
    assertEquals("xyg , expect 3: ", 3, switcher.intValue("xgy")) ;
    assertEquals("agf , expect 4: ", 4, switcher.intValue("agf")) ;
    assertEquals("aaaf , expect -1: ", -1,  switcher.intValue("aaaf")) ;
    
    int ENTRY = 50 ;
    keys = new String[ENTRY] ;
    nums = new int[ENTRY] ;
    HashMap map = new HashMap(100) ;
    for(int i = 0; i < ENTRY; i++) {
      keys[i] = "longmethod" + i ;
      nums[i] = i ;
      map.put(keys[i], keys[i]) ;
    }
    switcher = StringSwitcher.create(keys, nums, false) ;
    int LOOP = 20000 ;
    long start = System.currentTimeMillis() ;
    for (int i = 0; i < LOOP; i++ ) map.get("longmethod21") ;
    pintMessage("MAP ", start , System.currentTimeMillis()) ;
    start = System.currentTimeMillis() ;
    for (int i = 0; i < LOOP; i++ ) switcher.intValue("longmethod21")  ;
    pintMessage("SWITCHER ", start , System.currentTimeMillis()) ;
  }

  private void pintMessage(String message, long start , long end) {
    System.out.println(message + " in " + (end -start) + "ms") ;
  }
}