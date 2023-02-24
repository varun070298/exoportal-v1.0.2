/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.filesystem.nodedata;


import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.util.PathUtil;
import java.io.File;
import java.io.IOException;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeContainerResolver.java,v 1.13 2004/11/02 18:34:16 geaz Exp $
 */

public class NodeContainerResolver {

  // Repository Doc View
  public static final String DOCVIEW_EXT = ".rdv";

  public NodeContainer resolve(File rootFile, String jcrPath) throws PathNotFoundException,
      RepositoryException, IOException {
//      File rootFile = (File) rootContext;

    int depth = PathUtil.getDepth(jcrPath);
    int i = 0;
    File container = null;
//      String filePath = null;
    String parentType = "nt:folder";
    String ancestor = null;


    for (; i <= depth; i++) {
      ancestor = PathUtil.getAncestorPath(jcrPath, i);
      container = new File(rootFile.getCanonicalPath() + ancestor);

      if (container.exists()) {
        if (container.isDirectory()) {
          parentType = "nt:folder";
        } else {
          if (container.getName().toLowerCase().endsWith(DOCVIEW_EXT))
            parentType = "exo:jcrdocfile";
          else
            parentType = "nt:file";
        }
        break;
      }
    }

//    System.out.println(">>>.> Resolve container "+container+" "+ancestor+" "+parentType);

    if (parentType == null)
      throw new PathNotFoundException("File for the <" + jcrPath + "> not found!");

    if (parentType.equals("nt:file"))
      return new FileNodeContainer(ancestor, container);
    else if (parentType.equals("nt:folder"))
      return new FolderNodeContainer(ancestor, container);
    else if (parentType.equals("exo:jcrdocfile"))
      return new DocViewNodeContainer(ancestor, container.getCanonicalPath());

    throw new RepositoryException("Unresolved node <" + jcrPath + ">!");

  }

  public NodeContainer create(File rootFile, NodeData node) throws PathNotFoundException,
      RepositoryException, IOException, ItemExistsException {

    // File should exist
    String nodeType = node.getPrimaryNodeType().getName();
    String jcrPath = node.getPath();

    System.out.println("Resolver create ---" + jcrPath + " " + nodeType + " " + rootFile.getCanonicalPath() + jcrPath);

    if (isFile(nodeType)) {
      File file = new File(rootFile.getCanonicalPath() + jcrPath);
      if (file.exists())
        throw new ItemExistsException("File for <" + jcrPath + "> already exists!");

      if ("nt:folder".equals(nodeType)) {        
        file.mkdir();
      } else {
        file.createNewFile();
      }
    } 
    else if(!"nt:content".equals(nodeType))
        throw new RepositoryException("FS container can't create node of type <" + nodeType + ">!");

    return resolve(rootFile, jcrPath);
  }

  private boolean isFile(String type) {

    return type.equals("nt:file") ||
        type.equals("nt:folder") ||
        type.equals("exo:jcrdocfile");
  }


}
