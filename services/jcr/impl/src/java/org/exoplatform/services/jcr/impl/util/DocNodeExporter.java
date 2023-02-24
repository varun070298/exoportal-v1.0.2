/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.util;

import javax.jcr.RepositoryException;


import java.util.Properties;
import java.util.List;


import javax.jcr.PropertyType;

import org.apache.commons.codec.binary.Base64;
import org.exoplatform.services.jcr.impl.Constants;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.impl.core.PropertyImpl;
import org.exoplatform.services.jcr.impl.core.WorkspaceImpl;
import org.exoplatform.services.jcr.storage.Container;

import javax.jcr.PropertyIterator;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: DocNodeExporter.java,v 1.5 2004/08/23 10:31:39 geaz Exp $
 */

public class DocNodeExporter {


  public static void export(Container container, NodeImpl node, XMLWriter writer,
                            boolean binaryAsLink, boolean noRecurse) throws RepositoryException {

    String name = node.getName();

    if (name.length() == 0) // root node
      name = "root";

//        buffer.append("<"+name);
    Properties attrs = new Properties();
//        List props = container.getChildren(node.getPath(), Constants.PROPS);
//        for(int i = 0; i<props.size(); i++) {
//            PropertyImpl prop = (PropertyImpl)props.get(i);

    PropertyIterator props = node.getProperties();
    while (props.hasNext()) {
      PropertyImpl prop = (PropertyImpl) props.next();
      String strPropVal = getStrPropValue(prop, binaryAsLink);
//            buffer.append(" "+prop.getName()+"=\""+strPropVal+"\"");
      attrs.setProperty(prop.getName(), strPropVal);
    }
    writer.startElement(name, attrs);

    List nodes = container.getChildren(node.getPath());
    for (int i = 0; i < nodes.size(); i++) {
      NodeImpl child = (NodeImpl) nodes.get(i);
//            child.setWorkspace((WorkspaceImpl)node.getWorkspace());
      if (!noRecurse)
        export(container, child, writer, binaryAsLink, noRecurse);
    }

    writer.endElement();

  }
/*
    public static void export( Container container, String path, XMLWriter writer,
                          boolean binaryAsLink, boolean noRecurse) throws RepositoryException {

        String name = PathUtil.getName(path);
        System.out.println("Name---"+name);

        if(name.length() == 0) // root node
             name = "root";

//        buffer.append("<"+name);
        Properties attrs = new Properties();

        PropertyIterator props = container.getRootNode().getProperties();
        System.out.println("Props---"+props);
        while(props.hasNext()) {
            PropertyImpl prop = (PropertyImpl)props.next();
            String strPropVal = getStrPropValue(prop, binaryAsLink);
            attrs.setProperty(prop.getName(), strPropVal);
        }
        writer.startElement(name, attrs);

        List nodes = container.getChildren(path);
        System.out.println("Nodes---"+nodes);
        if(nodes.size() > 0) {
           for(int i = 0; i<nodes.size(); i++) {
               NodeImpl child = (NodeImpl)nodes.get(i);
               if(!noRecurse)
                   export(container, child, writer, binaryAsLink, noRecurse);
           }
        }

        writer.endElement();

    }
*/
  public static String getStrPropValue(PropertyImpl prop, boolean binaryAsLink) {

    if (prop.getType() == PropertyType.BINARY) {
      if (binaryAsLink)
        return prop.getPath();
      else {
        String str = new String(Base64.encodeBase64(prop.toString().getBytes()));
        return str;
      }

    } else
      return StringConverter.normalizeString(prop.toString(), false);
  }

}
