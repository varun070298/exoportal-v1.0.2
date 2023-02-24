/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.inmemory;

import java.util.Map;
import java.util.HashMap;
import javax.jcr.Node;
import org.exoplatform.services.jcr.impl.core.NodeImpl;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: WorkspaceContainerRegistry.java,v 1.1 2004/08/23 10:31:39 geaz Exp $
 */
public class WorkspaceContainerRegistry {

    /*# private WorkspaceContainerRegistry _workspaceRegistry; */
    private static WorkspaceContainerRegistry instance = null;

  //map that contain a map of items
    private Map workspaces;
    private String defaultWorkspace;

    protected WorkspaceContainerRegistry() {

       workspaces = new HashMap();
    }

    public static WorkspaceContainerRegistry getInstance() {
        if (instance == null) {
            instance = new WorkspaceContainerRegistry ();
        }
        return instance;
    }


    public Map getWorkspaceContainer(String name, String rootNodeType) {

        if(workspaces.get(name) == null)
            initWorkspaceContainer(name, rootNodeType);

        return (Map)workspaces.get(name);
    }

     private void initWorkspaceContainer(String name, String rootNodeType) {

        Map workspace = new HashMap();
        try {
           workspace.put("/", new NodeImpl("/",rootNodeType));
        } catch (Exception e) {
            // unreal
        }
        workspaces.put(name, workspace);

//         if(isBase)
//           defaultWorkspace = name;
    }

}
