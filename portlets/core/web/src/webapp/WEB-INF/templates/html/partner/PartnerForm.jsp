<%
/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 *
 * Wed, Sep 24, 2003 @  
 * @author: Tuan Nguyen
 * @version: $Id: PartnerForm.jsp,v 1.1.1.1 2004/03/02 19:00:52 benjmestrallet Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 **/
%>
<%@ page import="java.util.*"%>
<%@ page import="javax.portlet.RenderRequest"%>
<%@ page import="javax.portlet.RenderResponse"%>
<%@ page import="org.exoplatform.portal.portlet.actions.RequestId"%>
<%@ page import="org.exoplatform.portal.utils.Formater"%>

<%
  RenderRequest prequest = (RenderRequest) request.getAttribute("javax.portlet.request") ;
  RenderResponse presponse = (RenderResponse) request.getAttribute("javax.portlet.response")  ;
  String actionURL = (String) presponse.createActionURL().toString() ; 
  ResourceBundle  res  = ResourceBundle.getBundle("PartnerResource") ;
  Formater ft  = Formater.getDefaultFormater() ;
  List partnerList = (List) prequest.getAttribute("partner-list") ;
  RequestId requestId = (RequestId) prequest.getPortletSession().getAttribute("requestId") ;
%>

This is a Test
