/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.content;

import java.util.Locale;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import javax.jcr.StringValue;
import javax.jcr.Ticket;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.session.PortalResources;
import org.exoplatform.portlets.content.display.component.model.ContentConfig;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jul 5, 2004 1:35:46 PM
 */
public class ContentUtil {

  public static final String EDIT_STATE = "ACTION_EDIT";

  public static final String MODIFY_STATE = "ACTION_MODIFY";

  private static final String JCR_ROOT_PATH = "/cms/home/";

  private static final String WORSPACE = "ws";

  private static final String DEFAULT_LOCALE = "en";

  private static final String ROOT_FILE_NAME = "home";

  public static Ticket getCurrentTicket(String userName) {
    try {
      PortalContainer manager = PortalContainer.getInstance();
      RepositoryService repositoryService = (RepositoryService) manager
          .getComponentInstanceOfType(RepositoryService.class);
      Repository repository = repositoryService.getRepository();
      return repository.login(null, WORSPACE);
    } catch (RepositoryException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String resolveContent(ContentConfig config,
      org.exoplatform.services.portal.model.Node currentNode) {
    try {
      String uri = config.getUri();
      String pageRef = currentNode.getPageReference("text/xhtml").getPageReference();
      String[] splitedPageRef = StringUtils.split(pageRef, ":");
      String owner = splitedPageRef[0];            
      if (uri == null || "".equals(uri)) {
        //use the currentNodePath to lookup the content        
        uri = splitedPageRef[1];
      }
      Node node = lookupNode(getCurrentTicket(getUserName()), owner, uri, true);
      Property prop = node.getNode("jcr:content").getProperty("exo:content");
      if (prop != null)
        return prop.getString();
    } catch (PathNotFoundException e) {
      return null;
    } catch (RepositoryException e) {
      //log_.error(e.getMessage());
      throw new RuntimeException(e);
    }
    return null;
  }

  private static Node lookupNode(Ticket ticket, String owner, String uri, boolean useDefault)
      throws PathNotFoundException, RepositoryException {
    String[] splittedName = StringUtils.split(uri, "/");
    String realNameFile = null;
    if (splittedName.length == 0) {
      realNameFile = ROOT_FILE_NAME;
    } else {
      realNameFile = splittedName[splittedName.length - 1];
    }
    uri = JCR_ROOT_PATH + owner + uri;
    Node node = ticket.getNodeByAbsPath(uri);
    try {
      node = node.getNode(realNameFile + "_" + getLocale().getLanguage()
          + ".html");
    } catch (PathNotFoundException e) {
      if(useDefault)
        node = node.getNode(realNameFile + "_" + DEFAULT_LOCALE + ".html");
      else
        throw e;
    }
    return node;
  }

  public static Node createNode(Ticket ticket, String uri)
    throws RepositoryException {    
    Node node = getHomeNode(ticket);
    return createNodeFromNode(node, uri);
  }  
  
  public static Node createContentNode(Ticket ticket, String uri)
      throws RepositoryException {    
    Node node = getHomeNode(ticket);    
    node = createNodeFromNode(node, uri);
    String[] splittedName = StringUtils.split(uri, "/");
    node = node.addNode(splittedName[splittedName.length - 1] + "_"
        + getLocale().getLanguage() + ".html", "nt:file");
    return node;
  }
  
  private static Node createNodeFromNode(Node node, String uri) throws RepositoryException{
    String[] splittedName = StringUtils.split(uri, "/");
    for (int i = 0; i < splittedName.length; i++) {
      try {
        node = node.getNode(splittedName[i]);
      } catch (PathNotFoundException exc) {
        node = node.addNode(splittedName[i], "nt:folder");
      }
    }
    return node;
    
  }

  private static Node getHomeNode(Ticket ticket) throws PathNotFoundException,
      RepositoryException {
    String homeDir = JCR_ROOT_PATH + getPortalOwner();
    Node node = null;
    try {
      node = ticket.getNodeByAbsPath(homeDir);
    } catch(PathNotFoundException ex) {
      node = createNodeFromNode(ticket.getRootNode(), homeDir);
      ticket.save();
    }    
    return node;
  }

  private static Locale getLocale() {
    PortalResources appres = 
      (PortalResources)SessionContainer.getComponent(PortalResources.class);
    return appres.getLocale();
  }

  private static String getUserName() {
    ExternalContext econtext = FacesContext.getCurrentInstance()
        .getExternalContext();
    return econtext.getRemoteUser();
  }

  private static String getPortalOwner() {
    return SessionContainer.getInstance().getOwner() ;
  }

  public static void storeContent(String uri, String content)
      throws RepositoryException {
    String[] splittedName = StringUtils.split(uri, "/");    
    ExternalContext econtext = FacesContext.getCurrentInstance()
        .getExternalContext();
    String username = econtext.getRemoteUser();
    Ticket ticket = getCurrentTicket(username);
    Node node = null;
    try {
      node = lookupNode(ticket, getPortalOwner(), uri, false);
    } catch (PathNotFoundException e) {      
      node = createContentNode(ticket, uri);
    }
    node = node.getNode("jcr:content");
    node.setProperty("exo:content", new StringValue(content));
    getHomeNode(ticket).getNode(splittedName[0]).save(false);
  }
}