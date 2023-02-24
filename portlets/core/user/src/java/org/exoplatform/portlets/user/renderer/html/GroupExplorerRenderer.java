/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.renderer.html;

import java.io.IOException;
import java.util.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.user.component.UIGroupExplorer;
import org.exoplatform.services.organization.Group;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: GroupExplorerRenderer.java,v 1.12 2004/10/22 14:23:31 tuan08 Exp $
 */
public class GroupExplorerRenderer extends  HtmlBasicRenderer {
  private static Parameter CHANGE_GROUP = new Parameter(ACTION , "changeGroup") ;
  private static Parameter ADD_GROUP = new Parameter(ACTION , "addGroup") ;
  private static Parameter[] deleteGroupParams_ = { new Parameter(ACTION , "deleteGroup")} ;

  final public void encodeBegin( FacesContext context, UIComponent component ) throws IOException {
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
    UIGroupExplorer uiExplorer = (UIGroupExplorer) component ;
    Collection childrenGroup = uiExplorer.getChildrenGroup() ;
    Group currentGroup = uiExplorer.getCurrentGroup() ;
    Group parentGroup = uiExplorer.getParentGroup() ;
    String groupLabel = "/" ;
    if (currentGroup != null) groupLabel =  currentGroup.getId(); 
    Object[] args = { groupLabel } ;
    w.write("<table class='UIGroupExplorer'>");
    w.  write("<tr>");
    w.    write("<th class='header' colspan='2'>");
    w.      write(ft_.format(res.getString("UIGroupExplorer.label.group"), args));
    w.    write("</th>");
    w.  write("</tr>");
    w.  write("<tr>");
    w.    write("<td class='group-tree'>");
    addGroupTreeTable(parentGroup, childrenGroup,  w, res, uiExplorer) ;
    w.    write("</td>");
    w.    write("<td>");
    renderChildren(context, uiExplorer) ;
    w.    write("</td>");
    w.  write("</tr>");
    w.  write("<tr>");
    w.    write("<td colspan='2' class='footer'>");
    String parentGroupId = "" ;
    if (currentGroup != null) parentGroupId = currentGroup.getId() ;
    Parameter groupParam = new Parameter(UIGroupExplorer.GROUP_ID, parentGroupId) ;
    Parameter[] addGroupParams = { ADD_GROUP,groupParam } ;
    linkRenderer_.render(w, uiExplorer, res.getString("UIGroupExplorer.button.add-group"), addGroupParams) ;
    linkRenderer_.render(w, uiExplorer, res.getString("UIGroupExplorer.button.delete-group"), deleteGroupParams_) ;
    w.    write("</td>");
    w.  write("</tr>");
    w.write("</table>");
  }

  private void addGroupTreeTable(Group parentGroup, Collection childrenGroup, ResponseWriter w,  
                                 ResourceBundle res, UIGroupExplorer uiExplorer) throws IOException {
    Parameter groupIdParam = new Parameter(UIGroupExplorer.GROUP_ID, "") ;
    Parameter[] changeGroupParams = { CHANGE_GROUP,groupIdParam } ;
    Iterator i = childrenGroup.iterator() ;
    String parentGroupId = "" ;
    if (parentGroup != null) parentGroupId = parentGroup.getId() ;
    String groupIcon = res.getString("UIGroupExplorer.img.group-icon") ;
    w.write("<table>");
    w.  write("<tr>");
    w.    write("<td>");
    groupIdParam.setValue(parentGroupId) ;
    linkRenderer_.render(w, uiExplorer, "[..]", changeGroupParams) ;
    w.    write("</td>");
    w.  write("</tr>");
    while(i.hasNext()) {
      Group group = (Group)i.next() ;
      w.write("<tr>");
      w.  write("<td style='padding-left: 10px'>");
      w.    write(groupIcon);
      groupIdParam.setValue(group.getId()) ;
      linkRenderer_.render(w, uiExplorer, group.getGroupName(), changeGroupParams) ;
      w.  write("</td>");
      w.write("</tr>");
    }
    w.write("</table>");
  }
}