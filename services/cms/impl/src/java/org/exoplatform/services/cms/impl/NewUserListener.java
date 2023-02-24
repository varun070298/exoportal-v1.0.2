 /**************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.cms.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.StringValue;
import javax.jcr.Ticket;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.LocaleConfigService;
import org.picocontainer.Startable;

/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class NewUserListener extends UserEventListener implements Startable {         
  private NewUserConfig config_;
  private ConfigurationManager cservice_;
  private Collection localeConfigs_;
  private RepositoryService jcrService_;
  private Log log_;   

  public NewUserListener(ConfigurationManager cservice, LocaleConfigService localeService, 
      OrganizationService orgService, RepositoryService jcrService, LogService logService) throws Exception {
    orgService.addUserEventListener(this) ;    
    cservice_ = cservice;
    jcrService_ = jcrService;
    log_ = logService.getLog(getClass().getName());
    ServiceConfiguration sconf = cservice.getServiceConfiguration(getClass()) ;
    config_ = (NewUserConfig) sconf.getObjectParam("new.user.configuration").getObject() ;        
    localeConfigs_ = localeService.getLocalConfigs();    
  }

  public void start() {
  }

  public void stop() {
  }
  
  public void preSave(User user, boolean isNew, XResources xresources) throws Exception {
    if(!isNew) return ;
    if(!isUserPredefined(user) && !config_.isCreateHome()) return;
    //use a anonymous connection for the configuration as the user is not authentified at that time
    Repository jcrRepository = jcrService_.getRepository();
    Ticket ticket = jcrRepository.login(null, config_.getWorkspace());      
    Node root = ticket.getRootNode();      
    Node home = null;
    boolean firstAdd = false;
    if(!root.hasNode(config_.getJcrPath())){           
      home = makePath(root, config_.getJcrPath());
      firstAdd = true;
    } else {
      home = root.getNode(config_.getJcrPath());
    }            
    String defaultLocale = config_.getDefaultLocale();
    Node userHome = home.addNode(user.getUserName(), "nt:folder");                  
    List users = config_.getUsers();
    boolean userFound = false;
    NewUserConfig.User templateConfig = null;      
    for (Iterator iter = users.iterator(); iter.hasNext();) {
      NewUserConfig.User userConfig = (NewUserConfig.User) iter.next();        
      String currentName = userConfig.getUserName();
      if(config_.getTemplate().equals(currentName))
        templateConfig = userConfig;        
      if(currentName.equals(user.getUserName())){          
        List files = userConfig.getReferencedFiles();
        importInJCR(files, localeConfigs_, userHome, user.getUserName());                    
        userFound = true;
        break;
      }                        
    }      
    if(!userFound){
      //use template conf
      if(!config_.isCreateHome()) {
        ticket.revert();
        return; 
      }
      List files = templateConfig.getReferencedFiles();        
      importInJCR(files, localeConfigs_, userHome, templateConfig.getUserName());        
    }            
    if(firstAdd == true){
      ticket.save();
    } else {
      userHome.save(false);
    }               
  }
  
  private boolean isUserPredefined(User user){
    List users = config_.getUsers();
    for (Iterator iter = users.iterator(); iter.hasNext();) {
      NewUserConfig.User userConfig = (NewUserConfig.User) iter.next();        
      String currentName = userConfig.getUserName();
      if(currentName.equals(user.getUserName())){
        return true;
      }                        
    }
    return false;
  }
  
  
  private void importInJCR(List files, Collection
      locales, Node userHome, String userName) 
       throws PathNotFoundException, RepositoryException{
    for (Iterator iterator = files.iterator(); iterator.hasNext();) {
      String file = (String) iterator.next();
      Node fileNode = makePath(userHome, file);        
      for (Iterator iterator2 = locales.iterator(); iterator2.hasNext();) {
        LocaleConfig localeConfig = (LocaleConfig) iterator2.next();
        String locale = localeConfig.getLanguage();
        String[] splittedName = StringUtils.split(file, "/");
        String realNameFile = splittedName[splittedName.length - 1];                    
        String warPath = config_.getContentLocation() + "/" + userName +
            file + "_" + locale + ".html";
        try {            
          String fileValue = IOUtil.getStreamContentAsString(cservice_.getInputStream(warPath)) ;
          Node realFileNode = fileNode.addNode(realNameFile + "_" + locale + ".html", "nt:file");            
          Node contentNode = realFileNode.getNode("jcr:content");
          contentNode.setProperty("exo:content", new StringValue(fileValue));            
        } catch (Exception e1) {
          //means the version of the file for that locale does not exist          
        }          
      }
    }    
  }
  
  public Node makePath(Node rootNode, String path) 
      throws PathNotFoundException, RepositoryException {
    String[] tokens = StringUtils.split(path, '/');        
    Node node = rootNode;
    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i];                  
      if (!node.hasNode(token)) {                
        node = node.addNode(token, "nt:folder");
      } else {                
        node = node.getNode(token);
      }
    }
    return node;
  }    

  public void postSave(User user, boolean isNew, XResources xresources) {    
  }

  public void preDelete(User user,  XResources xresources)  {
    Ticket ticket;
    try {                  
      //use a anonymous connection for the configuration as the user is not authentified at that time
      Repository jcrRepository = jcrService_.getRepository();
      ticket = jcrRepository.login(null, config_.getWorkspace());
      Node root = ticket.getRootNode();
      Node home =  ticket.getNodeByAbsPath(config_.getJcrPath());
      Node userNode = home.getNode(user.getUserName()); 
      home.remove(user.getUserName());    
      userNode.save(false);
    } catch (PathNotFoundException ex){
      log_.info("Can not delete home dir of user "+ user.getUserName());
    } catch (RepositoryException e) {     
      log_.error("RepositoryException while trying to delete a user home dir", e);
    }         
  }

  public void postDelete(User user,  XResources xresources)  {    
  }  
}
