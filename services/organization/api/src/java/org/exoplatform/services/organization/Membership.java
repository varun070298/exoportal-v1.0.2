package org.exoplatform.services.organization;

public interface Membership {
  /**
   * the type of Membership allows distinction between 'hierarchical'
   * and 'supportive' Memberships.
   */
  public String getMembershipType();
  public void setMembershipType(String type);
  public String getId() ;
  public String getGroupId() ;
  public String getUserName() ;
}
