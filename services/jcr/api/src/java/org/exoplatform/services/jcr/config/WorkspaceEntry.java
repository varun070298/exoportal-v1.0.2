/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.config;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 23 juil. 2004
 */
public class WorkspaceEntry {

  private String name;
  private boolean base;
  private String repositoryName;
  private String containerName;

  public WorkspaceEntry(String name, String repositoryName, String containerName) {
    this(name, repositoryName, false, containerName);
  }

  public WorkspaceEntry(String name, String repositoryName, boolean base, String containerName) {
    this.name = name;
    this.base = base;
    this.repositoryName = repositoryName;
    this.containerName = containerName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isBase() {
    return base;
  }

  public void setBase(boolean base) {
    this.base = base;
  }

  public String getRepositoryName() {
    return repositoryName;
  }

  public void setRepositoryName(String repositoryName) {
    this.repositoryName = repositoryName;
  }

  public String getContainerName() {
    return containerName;
  }

  public void setContainerName(String containerName) {
    this.containerName = containerName;
  }

}
