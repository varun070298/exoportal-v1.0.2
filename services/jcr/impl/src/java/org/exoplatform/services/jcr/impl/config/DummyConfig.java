/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.config;


import java.util.Properties;
import org.exoplatform.services.jcr.config.*;
import org.exoplatform.services.jcr.impl.storage.filesystem.SimpleFsContainerImpl;
import org.exoplatform.services.jcr.impl.storage.inmemory.ContainerImpl;
import org.exoplatform.services.jcr.impl.storage.rdb.container.RDBWorkspaceContainerImpl;

/**
 * Created by The eXo Platform SARL
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: DummyConfig.java,v 1.12 2004/11/02 18:36:28 geaz Exp $
 */

public class DummyConfig implements RepositoryServiceConfig {

  private WorkspaceEntry[] wes = new WorkspaceEntry[9];
  private ContainerEntry[] ces = new ContainerEntry[5];
  private RepositoryEntry[] res = new RepositoryEntry[3];
  private RepositoryManagerEntry[] mes = new RepositoryManagerEntry[3];

  public DummyConfig() {
    wes[0] = new WorkspaceEntry("ws", "mock", true, "c.inmemory");
    wes[1] = new WorkspaceEntry("ws2", "mock", "c.inmemory2");
    wes[2] = new WorkspaceEntry("ws3", "mock", "c.inmemory");
    wes[3] = new WorkspaceEntry("ws", "fs1", true, "c.fs1");
    wes[4] = new WorkspaceEntry("ws2", "fs1", "c.fs1");
    wes[5] = new WorkspaceEntry("ws3", "fs1", "c.fs1");
    wes[6] = new WorkspaceEntry("ws", "db1", true, "c.db1");
    wes[7] = new WorkspaceEntry("ws2", "db1", "c.db2");
    wes[8] = new WorkspaceEntry("ws3", "db1", "c.db1");

    Properties props = new Properties();
    props.setProperty("rootNodeType", "nt:default");
    ces[0] = new ContainerEntry("c.inmemory", ContainerImpl.class, props);
    ces[1] = new ContainerEntry("c.inmemory2", ContainerImpl.class, props);

    props = new Properties();
    props.setProperty("rootDir", "./temp/fsroot");
    ces[2] = new ContainerEntry("c.fs1", SimpleFsContainerImpl.class, props);

    props = new Properties();
    props.setProperty("rootNodeType", "nt:default");
    props.setProperty("sourceName", "exo.hibernate.service1");

//    props.setProperty("sourceName", "jdbc:hsqldb:mem:exo");
//    props.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.HSQLDialect");

    ces[3] = new ContainerEntry("c.db1", RDBWorkspaceContainerImpl.class, props);
    props = new Properties();
//    props.setProperty("rootNodeType", "nt:default");
    props.setProperty("sourceName", "exo.hibernate.service2");

//    props.setProperty("sourceName", "jdbc:hsqldb:mem:exo2");
//    props.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.HSQLDialect");

    ces[4] = new ContainerEntry("c.db2", RDBWorkspaceContainerImpl.class, props);

    props = new Properties();
    mes[0] = new RepositoryManagerEntry("inmemory", org.exoplatform.services.jcr.impl.storage.inmemory.RepositoryManagerImpl.class, props);
    mes[1] = new RepositoryManagerEntry("sfs", org.exoplatform.services.jcr.impl.storage.filesystem.SimpleFsRepositoryManagerImpl.class, props);
    props = new Properties();
    props.setProperty("sourceName", "exo.hibernate.service1");
//    props.setProperty("sourceName", "jdbc:hsqldb:mem:exo");
//    props.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.HSQLDialect");
    mes[2] = new RepositoryManagerEntry("db", org.exoplatform.services.jcr.impl.storage.rdb.repository.RDBRepositoryManagerImpl.class, props);


    res[0] = new RepositoryEntry("mock", "inmemory");
    res[1] = new RepositoryEntry("fs1", "sfs");
    res[2] = new RepositoryEntry("db1", "db");

  }

  public String getDefaultRepositoryName() {
    return "mock";
  }

  public RepositoryEntry[] getRepositoryEntries() {
    return res;
  }

  public WorkspaceEntry[] getWorkspaceEntries() {
    return wes;
  }

  public WorkspaceEntry getWorkspaceEntry(String repositoryName, String name) {
    for (int i = 0; i < wes.length; i++) {
      if (wes[i].getRepositoryName().equals(repositoryName) && wes[i].getName().equals(name))
        return wes[i];
    }
    return null;
  }

  public ContainerEntry[] getSupportedContainerEntries() {
    return ces;
  }

  public ContainerEntry getContainerEntry(String name) {
    for (int i = 0; i < ces.length; i++) {
      if (ces[i].getName().equals(name))
        return ces[i];
    }
    return null;
  }

  public RepositoryManagerEntry[] getSupportedRepositoryManagerEntries() {
    return mes;
  }

  public RepositoryManagerEntry getRepositoryManagerEntry(String name) {
    for (int i = 0; i < mes.length; i++) {
      if (mes[i].getName().equals(name))
        return mes[i];
    }
    return null;
  }

  public RepositoryEntry getRepositoryEntry(String name) {
    for (int i = 0; i < res.length; i++) {
      if (res[i].getName().equals(name))
        return res[i];
    }
    return null;
  }

}
