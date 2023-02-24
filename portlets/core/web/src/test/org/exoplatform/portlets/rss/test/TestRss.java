/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.portlets.rss.test;

import java.util.*;
import java.io.InputStream ;
import java.net.URL ;
import org.exoplatform.portlets.rss.component.*;
import org.exoplatform.test.BasicTestCase;

//import org.apache.commons.digester.rss.RSSDigester;
//import org.apache.commons.digester.rss.Channel;
//import org.apache.commons.digester.rss.Item;
/*
 * Thu, Jun 05, 2003 @  
 * @author: Tuan Nguyen
 * @version: $Id: TestRss.java,v 1.2 2004/04/10 17:21:35 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestRss extends BasicTestCase {
  
  public TestRss(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  protected int getTestNumber() {
    return 1 ;
  }
  /*
  public void testRssChannel() throws Exception {
    Channel channel = null;
    try {
      RSSDigester digester = new RSSDigester();
      digester.setDebug(999) ;
      //channel = (Channel) digester.parse("file:./src/exo/portal/portlets/rss/test/Rss.xml");
      URL url = new URL("http://sourceforge.net/export/rss2_projsummary.php?group_id=62218");
      //URL url = new URL("http://headlines.internet.com/internetnews/fina-news/news.rss");
      System.out.println("size = " + url.openStream().available());
      channel = (Channel) digester.parse(url.openStream());
      System.out.println("Channel " + channel);
      System.out.println("Rating : " + channel.getRating());
      System.out.println("Publish Date : " + channel.getPubDate());
      System.out.println("Description : " + channel.getDescription());
      Item[] items = channel.getItems() ;
      for (int i = 0; i < items.length ; i++) {
        System.out.println("Title : " + items[i].getTitle());
        System.out.println("Description : " + items[i].getDescription());
        System.out.println("Link : " + items[i].getLink());
        
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  */

  public void testRssChannel() throws Exception {
    Channel channel = Channel.parse("http://headlines.internet.com/internetnews/fina-news/news.rss") ;
    System.out.println("title : " + channel.getTitle());
    System.out.println("link : " + channel.getLink());
    System.out.println("Description : " + channel.getDescription());
    Item[] items = channel.getItems() ;
    for (int i = 0; i < items.length ; i++) {
      System.out.println("Title : " + items[i].getTitle());
      System.out.println("Description : " + items[i].getDescription());
      System.out.println("Link : " + items[i].getLink());

    }
  }

  public String getDescription() {
    return "Test Rss" ;
  }
}

