/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.application;

import java.text.MessageFormat;
import java.util.ResourceBundle ;
import javax.faces.application.FacesMessage;
import org.exoplatform.commons.utils.ExpressionUtil;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 29, 2004
 * @version $Id$
 */
public class ExoFacesMessage extends FacesMessage {
  private Object[] args_ ;
  
  public ExoFacesMessage(FacesMessage.Severity severity,  String summary, String detail) {
    super(severity, summary, detail) ;
  }
  
  public ExoFacesMessage(FacesMessage.Severity severity,  String summary, String detail, Object[] args) {
    super(severity, summary, detail) ;
    args_ = args ;
  }
  
  public ExoFacesMessage(String summary, String detail) {
    super(summary, detail) ;
  }
  
  public ExoFacesMessage(String summary, String detail, Object[] args) {
    super(summary, detail) ;
    args_ = args ;
  }
  

  public ExoFacesMessage(String summary) {
    super(summary) ;
  }
  
  public ExoFacesMessage(String summary, Object[] args) {
    super(summary) ;
    args_ = args ;
  }
  
  public String getSummary(ResourceBundle res) {
    String s =  ExpressionUtil.getExpressionValue(res, getSummary()) ;
    if(args_ != null) s = MessageFormat.format(s, args_) ;
    return s ;
  }
  
  public String getDetail(ResourceBundle res) {
    String s =  ExpressionUtil.getExpressionValue(res, getSummary()) ;
    if(args_ != null) s = MessageFormat.format(s, args_) ;
    return s ;
  }
}