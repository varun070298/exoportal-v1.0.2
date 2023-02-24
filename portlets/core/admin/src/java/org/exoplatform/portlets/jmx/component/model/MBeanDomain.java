/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.jmx.component.model;

import java.util.* ;
import javax.management.*;
/**
 * Jul 29, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MBeanDomain.java,v 1.1 2004/07/31 14:56:21 tuan08 Exp $
 */
public class MBeanDomain {
  private String domainName_ ;
  private String label_ ;
  private List domains_ ;
  private List mbeanNames_ ;
  private boolean select_ ;
 
	public MBeanDomain(String name) {
		domains_ = new ArrayList() ;
    mbeanNames_ = new ArrayList() ;
    domainName_ = name ;
    label_ = name ;
    int idx = name.lastIndexOf('.') ;
    if(idx > 0) label_ = name.substring(idx + 1 , name.length()) ;
  }
  
  public String getDomainName() { return domainName_ ; }
  public String getLabel() { return label_ ; }
  
  public List getMBeanNames() { return mbeanNames_  ;}  
  
  public List  getDomains() { return domains_ ; }
  
  public boolean isSelect() { return select_ ; }
  public void    setSelect(boolean b) { select_ = b ; }
  
  public void addObjectName(ObjectName oname) {
    String canonicalName = oname.getCanonicalName() ;
    for(int i = 0 ; i < domains_.size(); i++) {
    	MBeanDomain mdomain = (MBeanDomain) domains_.get(i) ;
      if(canonicalName.startsWith(mdomain.getDomainName())) {
      	mdomain.addObjectName(oname) ;
        return ;
      }
    }
  	if(oname.getDomain().equals(domainName_)) {
      mbeanNames_.add(new MBeanDescription(oname)) ;
    } else { 
      String domain = oname.getDomain() ;
      int idx = domain.indexOf(".", domainName_.length() + 1) ;
      if(idx > 0) {
        domain = domain.substring(0, idx) ; 
      }
      MBeanDomain mdomain = new MBeanDomain(domain) ;
      mdomain.addObjectName(oname) ;
      domains_.add(mdomain) ;
    }
  }
  
  public MBeanDomain findMBeanDomain(String domain) {
    if(domainName_.equals(domain)) return this ;
    for(int i = 0; i < domains_.size(); i++) {
    	MBeanDomain mdomain = (MBeanDomain) domains_.get(i) ;
      MBeanDomain temp = mdomain.findMBeanDomain(domain) ;
      if(temp != null)  return temp ;
    }
    return null ;
  }
  
  public ObjectName findMBeanObjectname(String id) {
    for(int i = 0; i < mbeanNames_.size(); i++) {
      MBeanDescription desc = (MBeanDescription) mbeanNames_.get(i) ;
      if(id.equals(desc.getId())) return desc.getObjectName() ;
    }
    for(int i = 0; i < domains_.size(); i++) {
      MBeanDomain mdomain = (MBeanDomain) domains_.get(i) ;
      ObjectName name = mdomain.findMBeanObjectname(id) ;
      if(name != null) return name ;
    }
    return null ; 
  }
  
  static public class MBeanDescription {
    private String id_ ;
    private String label_ ;
    private ObjectName oname_ ;
    
  	public  MBeanDescription (ObjectName name) {
      oname_ = name ;
      id_ = new String(Integer.toString(name.hashCode())) ;
      label_ = name.getKeyProperty("type") ;
      if(label_ == null) {
        label_ = "mbean" ;
      } else {
        int idx = label_.lastIndexOf('.') ;
        if(idx > 0) label_ = label_.substring(idx + 1 , label_.length()) ; 
      }
    }

    public String getId() { return id_ ;}
    public String getLabel() { return label_ ; }
    public ObjectName getObjectName() { return oname_ ; }
  }
}