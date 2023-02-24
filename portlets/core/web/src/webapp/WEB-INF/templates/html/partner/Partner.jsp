<%
/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 *
 * Wed, Sep 24, 2003 @  
 * @author: Tuan Nguyen
 * @version: $Id: Partner.jsp,v 1.1.1.1 2004/03/02 19:00:52 benjmestrallet Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 **/
%>
<%@ page import="java.util.*"%>
<%@ page import="javax.portlet.RenderRequest"%>
<%@ page import="javax.portlet.RenderResponse"%>
<%@ page import="org.exoplatform.portal.portlet.actions.RequestId"%>
<%@ page import="org.exoplatform.portal.utils.Formater"%>
<%@ page import="org.exoplatform.portal.portlets.partner.Partner"%>

<%
  RenderRequest prequest = (RenderRequest) request.getAttribute("javax.portlet.request") ;
  RenderResponse presponse = (RenderResponse) request.getAttribute("javax.portlet.response")  ;
  String actionURL = (String) presponse.createActionURL().toString() ; 
  ResourceBundle  res  = ResourceBundle.getBundle("PartnerResource") ;
  Formater ft  = Formater.getDefaultFormater() ;
  List partnerList = (List) prequest.getAttribute("partner-list") ;
  String logo = presponse.encodeURL("/web/images/logo/exo-platform-logo-100x37.jpg") ;
%>
<table cellpadding="0" cellspacing="0" border="0">
  <%for (int i = 0; i < partnerList.size(); i++) {%>
  <%  Partner partner =  (Partner) partnerList.get(i) ;%>
    <tr>
      <td>
        <table>
          <tr>
            <td><%=partner.getName()%></td>
          </tr>
          </tr>
            <td><img src="<%=partner.getLogo()%>"/></td>
          <tr>
        </table>
      </td>
    </tr>
  <%}%>
  <tr>
    <td align="center"><img src="<%=logo%>"/></td>
  </tr>
  <tr>
    <td>      <br> 
      <p style="font-weight: bold">If you are : </p>      
      <img src="/web/images/partner/hardware.png"/>a hardware company,<br>
      <img src="/web/images/partner/software.png"/>a software company,<br>
      <img src="/web/images/partner/integrator.png"/>a system integrator,<br>
      <img src="/web/images/partner/consultant.png"/>a consultant,<br>      
      <p>
      and are willing to devote significant resources to a 
      partnership between our companies, please 
      <a href="mailto:partner@exoplatform.com">contact us</a>
      </p>
    </td>
  </tr>
</table>
