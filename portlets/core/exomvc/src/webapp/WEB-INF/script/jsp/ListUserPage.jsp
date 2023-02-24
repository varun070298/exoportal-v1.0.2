<%
/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
%>
<%@ page import="java.util.*"%>
<%@ page import="javax.portlet.RenderRequest"%>
<%@ page import="javax.portlet.RenderResponse"%>
<%@ page import="org.exoplatform.portlet.exomvc.JSPPage"%>
<%@ page import="org.exoplatform.services.organization.User"%>
<%
  RenderRequest prequest = (RenderRequest) request.getAttribute("javax.portlet.request") ;
  RenderResponse presponse = (RenderResponse) request.getAttribute("javax.portlet.response") ;
  String portletURL = presponse.createActionURL().toString() ;
  String pageURL = (String)prequest.getAttribute(JSPPage.PAGE_URL) ; 
  List users = (List) prequest.getAttribute("jsp.list.user.page.users") ;
%>

<table width="100%" border="1">
  <tr>
    <th>User Name</th>
    <th>First Name</th>
    <th>Last  Name</th>
    <th>Email</th>
  </tr>
  <%for(int i = 0 ; i < users.size(); i++) {%>
  <%  User user = (User) users.get(i) ; %>
      <tr>
        <td><%=user.getUserName()%></td>
        <td><%=user.getFirstName()%></td>
        <td><%=user.getLastName()%></td>
        <td><%=user.getEmail()%></td>
      </tr>
  <%}%>
</table>
