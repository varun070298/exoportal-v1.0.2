/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.List ;
import java.util.ArrayList ;
import java.util.Date;
import java.text.SimpleDateFormat ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 25, 2004
 * @version $Id$
 */
public class ObjectQuery {
  private static SimpleDateFormat ft_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") ;
  private Class type_ ;
  private String orderBy_ ;
  private List  parameters_ ;  
  
  public ObjectQuery(Class type) {
    type_ = type ;
    parameters_ = new ArrayList(3) ;
  }
  
  public ObjectQuery addEQ(String field, Object value) {
    if(value != null) {
      parameters_.add(new Parameter(" = ", field, value)) ;
    }
    return this ;
  }
  
  public ObjectQuery addGT(String field, Object value) {
    if(value != null) {
      parameters_.add(new Parameter(" > ", field, value)) ;
    }
    return this ;
  }
  
  public ObjectQuery addLT(String field, Object value) {
    if(value != null) {
      parameters_.add(new Parameter(" < ", field, value)) ;
    }
    return this ;
  }
  
  public ObjectQuery addLIKE(String field, String value) {
    if(value != null && value.length() > 0)  {
      parameters_.add(new Parameter(" like ", field, value.replace('*', '%'))) ;
    }
    return this ;
  }
  
  public ObjectQuery setAscOrderBy(String field) {
    orderBy_ = " order by o." + field + " asc";
    return this ;
  }
  
  public ObjectQuery setDescOrderBy(String field) {
    orderBy_ = " order by o." + field + " desc";
    return this ;
  }
  
  public String getHibernateQuery() {
    StringBuffer b = new StringBuffer() ;
    b.append("from o in class ").append(type_.getName()) ;
    if(parameters_.size() > 0) {
      b.append(" where ") ;
      for(int i = 0; i < parameters_.size(); i ++) {
        if(i > 0) b.append(" and ") ;
        Parameter p = (Parameter) parameters_.get(i) ;
        if(p.value_ instanceof String) {
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(p.value_).append("'") ;
        } else if(p.value_ instanceof Date) {
          String value = ft_.format((Date) p.value_) ;
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(value).append("'") ;
        } else {
          b.append(" o.").append(p.field_).append(p.op_).append(p.value_);
        }
      }
    }
    if(orderBy_ != null )   b.append(orderBy_ );
    return b.toString() ;
  }
  
  public String getHibernateCountQuery() {
    StringBuffer b = new StringBuffer() ;
    b.append("select count(o) from o in class ").append(type_.getName()) ;
    if(parameters_.size() > 0) {
      b.append(" where ") ;
      for(int i = 0; i < parameters_.size(); i ++) {
        if(i > 0) b.append(" and ") ;
        Parameter p = (Parameter) parameters_.get(i) ;
        if(p.value_ instanceof String) {
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(p.value_).append("'") ;
        } else if(p.value_ instanceof Date) {
          String value = ft_.format((Date) p.value_) ;
          b.append(" o.").append(p.field_).append(p.op_).append("'").append(value).append("'") ;
        } else {
          b.append(" o.").append(p.field_).append(p.op_).append(p.value_);
        }
      }
    }
    return b.toString() ;
  }
  
  static class Parameter {
    String op_ ;
    String field_ ;
    Object value_ ;
    
    Parameter(String op, String field , Object value) {
      op_ = op ;
      field_ = field ;
      value_ = value ;
    }
  }
}
