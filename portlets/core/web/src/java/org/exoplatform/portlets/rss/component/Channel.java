/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.rss.component;

import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/*
 * Fri, May 30, 2003 @  
 * @author: Tuan Nguyen
 * @version: $Id: Channel.java,v 1.2 2004/07/29 14:09:43 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class Channel {
  private String title_ ;
  private String description_ ;
  private String link_ ;
  private String imageSrc_ ;
  private Item[] items_ ;
  private long updateTime_ ;
  
  public Channel() {
  }

  public String getTitle() { return title_ ; }
  public void   setTitle(String title) { title_ = title ; }

  public String getLink() { return link_ ; }
  public void   setLink(String link) { link_ = link ; }

  public String getDescription() { return description_ ; }
  public void   setDescription(String desc) {description_ = desc ; }

  public String getImageSrc() { return imageSrc_ ; }
  public void   setImageSrc(String src) { imageSrc_ =  src ; }
 
  public Item[] getItems() { return items_ ; }  
  public void   setItems(Item[] items) { items_ = items ; }  

  public long getUpdateTime() { return updateTime_ ; }
  public void setUpdateTime(long time) { updateTime_ = time; }

  static public Channel parse(String url) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setIgnoringComments(true);
    factory.setCoalescing(true);
    factory.setNamespaceAware(false);
    factory.setValidating(false);
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.parse(url);
    Channel channel = new Channel() ;
    setChannelInfo(channel, document) ;
    setItems(channel, document) ;
    channel.setUpdateTime(System.currentTimeMillis()) ;
    return channel ;
  }


  static private void setChannelInfo(Channel channelObj, Document doc) throws Exception {
    NodeList list = doc.getElementsByTagName("channel");
    if (list.getLength() > 0) {
      Node channel = list.item(0);
      list = channel.getChildNodes();
      for (int i = 0; i < list.getLength(); ++i) {
        Node node = list.item(i);
        String nodeName = node.getNodeName() ; 
        if ("title".equals(nodeName)) {
          channelObj.setTitle(node.getFirstChild().getNodeValue());
        } else  if ("link".equals(nodeName)) {
          channelObj.setLink(node.getFirstChild().getNodeValue());
        } else  if ("description".equals(nodeName)) {
          channelObj.setDescription(node.getFirstChild().getNodeValue());
        }
      }
    }
  }

  static private void setItems(Channel channelObj, Document doc) throws Exception {
    String root = doc.getDocumentElement().getTagName();
    Item[] items = null ;
    if (root.equals("rdf:RDF")) {
      NodeList list = doc.getElementsByTagName("item");
      items = getItems(list);
    } else if (root.equals("rss")) {
      NodeList list = doc.getElementsByTagName("channel");
      if (list.getLength() != 1) {
        items =  new Item[0];
      } else {
        Node channel = list.item(0);
        items = getItems(channel.getChildNodes());
      }
    } else if (root.equals("xml")) {
      NodeList list = doc.getElementsByTagName("item");
      items =  getItems(list);
    } else {
      items = new  Item[0];
    }
    channelObj.setItems(items) ;
  }

  static private Item[] getItems(NodeList items) throws Exception {
    Vector v = new Vector();
    for (int i = 0; i < items.getLength(); ++i) {
      Node node = items.item(i);
      if (node.getNodeName().equals("item") == false) {
        continue;
      }
      NodeList itemChildren = node.getChildNodes();
      Item item = new Item() ;
      for (int j = 0; j < itemChildren.getLength(); ++j) {
        Node child = itemChildren.item(j);
        String nodeName = child.getNodeName() ;
        if (nodeName.equals("title")) {
          if (child.getFirstChild() != null)
            item.setTitle(child.getFirstChild().getNodeValue());
        }
        if (nodeName.equals("link")) {
          if (child.getFirstChild() != null)
            item.setLink(child.getFirstChild().getNodeValue());
        }
        if (nodeName.equals("description")) {
          if (child.getFirstChild() != null) {
            item.setDescription(child.getFirstChild().getNodeValue());
          }
        }
      }
      v.addElement(item);
    }
    Item[] foundItems = new Item[v.size()];
    v.copyInto(foundItems);
    return foundItems;
  }

  public String toString() {
    StringBuffer b = new StringBuffer() ;
    for (int i = 0; i < items_.length; ++i) {
      b.append(items_[i].getTitle()).append("\n");
    }
    return b.toString() ;
  }
}

