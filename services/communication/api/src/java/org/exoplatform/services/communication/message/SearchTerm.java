/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 28, 2004
 * @version $Id: SearchTerm.java,v 1.1 2004/08/29 01:56:53 tuan08 Exp $
 */
public class SearchTerm {
  private int limit_ ;
  private String fromTerm_ ;
  private String toTerm_ ;
  private String subjectTerm_ ;
  private String bodyTerm_ ;
  
  public int getLimit() { return limit_ ; }
  public void setLimit(int limit) { limit_ = limit ; }
  
  public String getFromTerm() { return fromTerm_ ; }
  public void   setFromTerm(String term) { fromTerm_ = term ; }
  
  public String getToTerm() { return toTerm_ ; }
  public void   setToTerm(String term) { toTerm_ = term ; }
  
  public String getSubjectTerm() { return subjectTerm_ ; }
  public void   setSubjectTerm(String term) { subjectTerm_ = term ; }
  
  public String getBodyTerm() { return bodyTerm_ ; }
  public void   setBodyTerm(String term) { bodyTerm_ = term ; }
}
