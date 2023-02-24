package org.exoplatform.services.organization;

import java.util.* ;

public interface  MembershipType  {
  public String   getName()  ;
  public void     setName(String s)  ;

  public String   getDescription()  ;
  public void     setDescription(String s) ;

  public String   getOwner()  ;
  public void     setOwner(String s) ;

  public Date     getCreatedDate()  ;
  public void     setCreatedDate(Date d)  ;

  public Date     getModifiedDate()  ;
  public void     setModifiedDate(Date d) ;
}
