/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal;

import org.exoplatform.Constants;
import org.exoplatform.faces.core.component.model.Parameter;
/**
 * Apr 18, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortalConstants.java,v 1.15 2004/09/27 20:44:58 tuan08 Exp $
 **/
public class PortalConstants extends Constants {
  final public static String NODE_URI = "nodeURI" ;
  
  final public static String ADD_NODE_ACTION = "addNode" ;
  final public static String DELETE_NODE_ACTION = "deleteNode" ;
  final public static String EDIT_NODE_ACTION = "editNode" ;
  final public static String SAVE_NODE_ACTION = "saveNode" ;
  final public static String MOVE_UP_NODE_ACTION = "moveUpNode" ;
  final public static String MOVE_DOWN_NODE_ACTION = "moveDownNode" ;
  final public static String CHANGE_NODE_ACTION = "changeNode" ;
  
  final public static String ADD_CONTAINER_ACTION = "addContainer" ;
  final public static String ADD_PORTLET_ACTION = "addPortlet" ;
  final public static String EDIT_ACTION = "editProperty" ;
  final public static String DELETE_ACTION = "delete" ;
  final public static String MOVE_UP_ACTION = "moveUp" ;
  final public static String MOVE_DOWN_ACTION = "moveDown" ;
  final public static String SAVE_ACTION = "save" ;
  final public static String BROWSE_PAGE_ACTION = "browsePage" ;
  final public static String SAVE_PAGE_ACTION = "savePage" ;
  final public static String PLACE_BODY_ACTION = "placePage" ;
  final public static String CHANGE_CONTAINER_TAB_ACTION = "changeTab" ;
  
  final public static String EDIT_PORTAL_ACTION    = "editPortal" ;
  final public static String SAVE_PORTAL_ACTION     = "savePortal" ;
  final public static String CHANGE_LANGUAGE_ACTION = "changeLanguage" ;
  final public static String LANGUAGE_PARAM       =  PARAMETER_ENCODER + "language" ;
  
  final public static Parameter ADD_NODE = new Parameter(PORTAL_ACTION,  ADD_NODE_ACTION);
  final public static Parameter ADD_CONTAINER =  new Parameter(PORTAL_ACTION, ADD_CONTAINER_ACTION);
  final public static Parameter ADD_PORTLET = new Parameter(PORTAL_ACTION, ADD_PORTLET_ACTION);
  final public static Parameter EDIT = new Parameter(PORTAL_ACTION, EDIT_ACTION);
  final public static Parameter EDIT_NODE = new Parameter(PORTAL_ACTION, EDIT_NODE_ACTION);
  final public static Parameter DELETE = new Parameter(PORTAL_ACTION, DELETE_ACTION);
  final public static Parameter DELETE_NODE = new Parameter(PORTAL_ACTION, DELETE_NODE_ACTION);
  final public static Parameter MOVE_UP = new Parameter(PORTAL_ACTION, MOVE_UP_ACTION);
  final public static Parameter MOVE_DOWN = new Parameter(PORTAL_ACTION, MOVE_DOWN_ACTION);
  final public static Parameter BROWSE_PAGE = new Parameter(PORTAL_ACTION, BROWSE_PAGE_ACTION);
  final public static Parameter PLACE_BODY = new Parameter(PORTAL_ACTION, PLACE_BODY_ACTION);
  final public static Parameter SAVE = new Parameter(PORTAL_ACTION, SAVE_ACTION);
  
  final public static Parameter[] addNodeParams = { new Parameter(PORTAL_ACTION,  ADD_NODE_ACTION) };
  final public static Parameter[] addContainerParams = { new Parameter(PORTAL_ACTION, ADD_CONTAINER_ACTION) };
  final public static Parameter[] addPortletParams = {new Parameter(PORTAL_ACTION, ADD_PORTLET_ACTION) };
  final public static Parameter[] editParams = {new Parameter(PORTAL_ACTION, EDIT_ACTION)};
  final public static Parameter[] editNodeParams = {new Parameter(PORTAL_ACTION, EDIT_NODE_ACTION)};
  final public static Parameter[] deleteParams = {new Parameter(PORTAL_ACTION, DELETE_ACTION)};
  final public static Parameter[] deleteNodeParams = { new Parameter(PORTAL_ACTION, DELETE_NODE_ACTION) };
  final public static Parameter[] moveUpParams = {new Parameter(PORTAL_ACTION, MOVE_UP_ACTION)};
  final public static Parameter[] moveDownParams = {new Parameter(PORTAL_ACTION, MOVE_DOWN_ACTION)};
  final public static Parameter[] browsePageParams = { new Parameter(PORTAL_ACTION, BROWSE_PAGE_ACTION) };
  final public static Parameter[] placeBodyParams ={new Parameter(PORTAL_ACTION, PLACE_BODY_ACTION)};
  final public static Parameter[] saveParams = {new Parameter(PORTAL_ACTION, SAVE_ACTION)};
  
  final public static String ADD_ICON = "<img class='add-icon' src='/skin/blank.gif'/>" ;
  final public static String ADD_CONTAINER_ICON = "<img class='add-container' src='/skin/blank.gif'/>" ;
  final public static String ADD_PORTLET_ICON = "<img class='add-portlet' src='/skin/blank.gif'/>" ;
  final public static String EDIT_ICON = "<img class='edit-icon' src='/skin/blank.gif'/>" ;
  final public static String EDIT_PAGE_NODE_ICON = "<img class='edit-node-icon' src='/skin/blank.gif'/>" ;
  final public static String DELETE_ICON = "<img class='delete-icon' src='/skin/blank.gif'/>" ;
  final public static String UP_ICON = "<img class='up-icon' src='/skin/blank.gif'/>" ;
  final public static String DOWN_ICON = "<img class='down-icon' src='/skin/blank.gif'/>" ;
  final public static String LEFT_ICON = "<img class='left-icon' src='/skin/blank.gif'/>" ;
  final public static String RIGHT_ICON = "<img class='right-icon' src='/skin/blank.gif'/>" ;
  final public static String BROWSE_PAGE_ICON = "<img class='browse-page-icon' src='/skin/blank.gif'/>" ;
  final public static String PLACE_BODY_ICON = "<img class='place-body-icon' src='/skin/blank.gif'/>" ;
  final public static String SAVE_ICON = "<img class='save-icon' src='/skin/blank.gif'/>" ;
}