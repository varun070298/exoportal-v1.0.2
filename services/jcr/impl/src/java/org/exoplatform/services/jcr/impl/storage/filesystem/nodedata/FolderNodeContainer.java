/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.filesystem.nodedata;


import javax.jcr.ItemExistsException;
import javax.jcr.RepositoryException;
import javax.jcr.Node;
import org.exoplatform.services.jcr.core.ItemLocation;
import org.exoplatform.services.jcr.core.NodeChange;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.storage.Container;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: FolderNodeContainer.java,v 1.15 2004/11/02 18:34:16 geaz Exp $
 */

public final class FolderNodeContainer extends BaseNodeContainer {


  FolderNodeContainer(String jcrPath, File storage) {
    super(jcrPath, "nt:folder", storage);
  }


  public NodeData getNodeByPath(String relPath) {

    if (relPath.length() != 0)
      return null;
    else
      return getRootNode();

  }

  public List getChildren(String relPath) {
//    System.out.println("Folder children '" + relPath + "' " + storage+" "+containerPath);
//    if (relPath.length() != 0)
//      return null;

    ArrayList list = new ArrayList();

    File[] cFiles = storage.listFiles();
    for (int i = 0; i < cFiles.length; i++){
      try {
         NodeContainer nodeContainer;
        String path = new ItemLocation(containerPath, cFiles[i].getName()).getPath();
/*
//        list.add(new NodeImpl(path));
         if(cFiles[i].isDirectory())
            nodeContainer = new FolderNodeContainer(path, cFiles[i]);
         else
            nodeContainer = new FileNodeContainer(path, cFiles[i]);

         list.add(nodeContainer.getRootNode());
*/
         list.add(path);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return list;
  }

  public void add(Node node) throws ItemExistsException, RepositoryException {
    // just create container (file or folder)
    // creates by resolver.create
  }


  public final void update(Node node) {
    // NOOP
  }

  public final void delete(String absPath) {

//    boolean result = storage.delete();
    log.debug("FolderNode Container delete node:" + absPath);
    deleteRecursively(storage);
  }

  final void deleteRecursively(File file) {

    File[] children = file.listFiles();
    if(children != null) {
      for(int i=0; i<children.length; i++) {
          log.debug("File :" + children[i]+" found for delete.");
          deleteRecursively(children[i]);
      }
    }
    System.out.println("File exists: " + file.exists());
    System.out.println("File is file: " + file.isFile());
    System.out.println("File deleted: " + file.delete());
/*
    boolean result = file.delete();
    log.debug("FolderNode Container deleted file:" + file + " result: "+result);
    */
//    file.deleteOnExit();

  }

}
