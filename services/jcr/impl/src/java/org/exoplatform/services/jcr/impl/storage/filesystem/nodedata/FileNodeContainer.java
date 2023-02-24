/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.filesystem.nodedata;


import javax.jcr.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.exoplatform.services.jcr.core.NodeChange;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.impl.core.PropertyImpl;
import org.exoplatform.services.jcr.storage.Container;


/**
 * Created by The eXo Platform SARL        
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: FileNodeContainer.java,v 1.14 2004/11/02 18:34:16 geaz Exp $
 */

public final class FileNodeContainer extends BaseNodeContainer {

  FileNodeContainer(String jcrPath, File storage) {
    super(jcrPath, "nt:file", storage);
  }

  public NodeData getNodeByPath(String relPath) {
    if (relPath.length() == 0)
      return getRootNode();
    else
      return getContentNode(relPath);

  }

  public List getChildren(String relPath) {
    ArrayList list = new ArrayList();
    if (relPath.length() == 0)
       list.add(getJcrPath("/jcr:content"));
//      list.add(getContentNode("/jcr:content"));
    return list;
  }

  public void add(Node node) throws ItemExistsException, RepositoryException {
    try {
      String relPath = parseRelPath(node.getPath());
      // file is already created by resolver.create
//      System.out.println("EXO:CONTENT ----  "+node);
      if (relPath.equals("/jcr:content")) {
        updateContent((NodeData)node);
      }
    } catch (Exception e) {
      throw new RepositoryException("Adding node failed. Reason:" + e.getMessage());
    }
  }

  public final void update(Node node) throws RepositoryException {
    String relPath;
    try {
      relPath = parseRelPath(node.getPath());
    } catch (Exception e) {
      return;
    }

    if (relPath.equals("/jcr:content")) // ; ?????
       updateContent(node);
  }

  public final void delete(String absPath) {
    try {
      String relPath = parseRelPath(absPath);
      log.debug("FileNode Container delete node:" + absPath + "at "+relPath);
      if (relPath.length() == 0)
        storage.delete();
      else if (relPath.equals("/jcr:content"))
        cleanContent();
    } catch (Exception e) {
      return;
    }
  }


///////////////////////////////////////////////////////

  protected NodeImpl getContentNode(String relPath) {
    if (!relPath.equals("/jcr:content"))
      return null;

    NodeImpl node;
    try {
      node = new NodeImpl(getJcrPath(relPath), getContentProps());
    } catch (Exception e) {
      return null;
    }

    return node;
  }

  protected List getContentProps() {

    ArrayList props = new ArrayList();
    props.add(getContentProperty("/jcr:content/jcr:primaryType"));
    PropertyImpl contentProp = getContentProperty("/jcr:content/exo:content");
    if(contentProp != null)
       props.add(contentProp);
    props.add(getContentProperty("/jcr:content/jcr:uuid"));

    return props;

  }


  protected PropertyImpl getContentProperty(String relPath) {

    if (!relPath.startsWith("/jcr:content"))
      return null;

    try {
      if (relPath.equals("/jcr:content/jcr:primaryType"))
        return new PropertyImpl(getJcrPath("/jcr:content/jcr:primaryType"),
            new StringValue("nt:content"), PropertyType.STRING);

      if (relPath.equals("/jcr:content/jcr:uuid"))
        return new PropertyImpl(getJcrPath("/jcr:content/jcr:uuid"),
            new StringValue(new String(Base64.encodeBase64( (containerPath/*+"/jcr:content"*/).getBytes()))), PropertyType.STRING) ;

      ////////////////////////
      // relPath = "/jcr:content/exo:content";
      ///////////////////////

      if (relPath.equals("/jcr:content/exo:content")) {
        FileInputStream fis = new FileInputStream(storage);
        if(fis.available() == 0)
            return null;
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        return new PropertyImpl(getJcrPath("/jcr:content/exo:content"),
            new StringValue(new String(buffer)), PropertyType.STRING);
      }

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    return null;
  }

  private void cleanContent() {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(storage);
      fos.write(new byte[0]);
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void updateContent(Node node) throws RepositoryException {
    try {
      FileOutputStream fos = new FileOutputStream(storage);
      System.out.println("EXO:CONTENT ----  "+node);
      Property prop = ((NodeImpl)node).getPermanentProperty("exo:content");
      if(prop == null)
        return;
//        throw new RepositoryException("FileNodeContainer.updateProperty() failed. Property exo:content not found in "+node.getNode().getPath());
//      log.debug("EXO:Content --" + prop + "= " + prop.getString());
      byte[] buffer = prop.getString().getBytes();
      fos.write(buffer);
      fos.close();
//      log.debug("FileNodeContainer.updateContent():  EXO:Content length =" + storage.length());
    } catch (Exception e) {
        e.printStackTrace();
        throw new RepositoryException("FileNodeContainer.updateProperty() failed. Reason:"+e);
    }
  }

}
