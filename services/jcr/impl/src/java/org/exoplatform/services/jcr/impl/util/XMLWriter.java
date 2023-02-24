/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.util;

import java.util.Map;
import java.util.Iterator;
import java.util.Stack;
import java.util.Properties;
import java.util.HashMap;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: XMLWriter.java,v 1.4 2004/08/18 00:09:07 benjmestrallet Exp $
 */

public class XMLWriter {

  private boolean nsWritten;
  private Map namespaces;
  private StringBuffer buffer;
  private Stack nodes;

  public XMLWriter(Map namespaces) {
    this.buffer = new StringBuffer();
    this.namespaces = namespaces;
    this.nodes = new Stack();
    this.nsWritten = false;
  }

  public XMLWriter() {
    this(new HashMap());
  }

  public void startElement(String qname, Properties attrs) {
    Iterator keys;
    buffer.append("<" + qname);
    if (!nsWritten) {
      keys = namespaces.keySet().iterator();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        String value = (String) namespaces.get(key);
        writeAttribute("xmlns:" + key, value);
      }
      nsWritten = true;
    }
    if (attrs != null) {
      keys = attrs.keySet().iterator();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        String value = (String) attrs.get(key);
        writeAttribute(key, value);
      }
    }
    if (!nodes.empty()) {
      ((Context) nodes.peek()).isOpen = false;
    }
    nodes.push(new Context(qname));
    buffer.append(">");
  }

  public void endElement() {
    Context curNode;
    if (!nodes.empty())
      curNode = (Context) nodes.pop();
    else
      throw new RuntimeException("Unexpected Empty Stack at End element !!");

// TODO
//        if(curNode.isOpen)
//            buffer.append("/>");
//        else
    buffer.append("</" + curNode.nodeName + ">");
  }

  private void writeAttribute(String qname, String value) {
    buffer.append(" " + qname + "=\"" + value + "\"");
  }

  public void writeText(String text) {
    if (!nodes.empty()) {
      if (text.length() > 0)
        ((Context) nodes.peek()).isOpen = false;
    } else
      throw new RuntimeException("Unexpected Empty Stack at Text '" + text + "' !!!");
    buffer.append(text);
  }

  public byte[] getBytes() {
    return buffer.toString().getBytes();
  }

  public String toString() {
    return buffer.toString();
  }

  private class Context {
    private Context(String nodeName) {
      this.nodeName = nodeName;
      this.isOpen = true;
    }

    private String nodeName;
    private boolean isOpen;
  }
}
