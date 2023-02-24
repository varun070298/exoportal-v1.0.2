/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import net.sf.hibernate.*;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.dialect.Dialect;
import org.apache.commons.logging.Log;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.log.LogService;
import org.exoplatform.commons.exception.ObjectNotFoundException;
import org.exoplatform.container.configuration.*;

/**
 * Created by The eXo Platform SARL        .
 * @author Tuan Nguyen tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * @author dhodnett
 * $Id: HibernateServiceImpl.java,v 1.3 2004/10/30 02:27:52 tuan08 Exp $
 */
public class HibernateServiceImpl implements HibernateService  {
	
	private SessionFactory factory_ ; 
	private Configuration config_ ;
	private ThreadLocal threadLocal_ ;
	private Log log_ ;
  private DatabaseService dbservice_ ;
  private String serviceName_ ;
  private String datasource_ ;
	
  public HibernateServiceImpl(DatabaseService dbService,  LogService lservice) {
    log_ = lservice.getLog(getClass());
    dbservice_ = dbService ;
    threadLocal_ = new ThreadLocal() ;
  } 
  
	public HibernateServiceImpl(DatabaseService dbService, 
                              LogService lservice, 
                              ConfigurationManager confService) throws Exception {
		this(dbService, lservice) ;
    ServiceConfiguration sconf = confService.getServiceConfiguration(HibernateService.class) ;
    PropertiesParam param = sconf.getPropertiesParam("exo.hibernate.service") ;
    Map properties = param.getProperties() ; 
    configure(param.getName(), properties) ;
	} 
	
	void configure(String name, Map properties) throws Exception {
    serviceName_ = name ;
	  config_ = new Configuration() ;
    datasource_ = (String) properties.get("hibernate.datasource.name") ;
	  String dbType = dbservice_.getDatabaseType(datasource_);
	  setDialect(config_, dbType);
	  Iterator i = properties.entrySet().iterator() ;
	  while(i.hasNext()) {
	    Map.Entry entry = (Map.Entry) i.next() ;
	    config_.setProperty((String)entry.getKey(), (String)entry.getValue()) ;
	  }
	} 

  private void setDialect(Configuration config , String dbType){
		if(DatabaseService.HSQL.equals(dbType)) {
			config.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.HSQLDialect");
		} else if(DatabaseService.MYSQL.equals(dbType)) {
			config.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.MySQLDialect");
		} else if(DatabaseService.DB2.equals(dbType)) {
			config.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.DB2Dialect");
		} else if(DatabaseService.ORACLE.equals(dbType)) {
			config.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.Oracle9Dialect");
		} else if(DatabaseService.POSTGRESQL.equals(dbType)) {
			config.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.PostgreSQLDialect");
		} else if(DatabaseService.SQL_SERVER.equals(dbType)) {
			config.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.SQLServerDialect");
		}
	}
  
  public String getServiceName() { return serviceName_ ; }
  
	public Session openSession()  throws Exception {
		Session currentSession = (Session)threadLocal_.get() ;
		if(currentSession == null) {
      log_.debug("open new hibernate session in openSession()") ;
			SessionFactory factory = getSessionFactory() ;
			currentSession = factory.openSession();
			threadLocal_.set(currentSession) ;
		}
		return currentSession ;
	}
	
	public Session openNewSession()  throws Exception {
		Session currentSession = (Session)threadLocal_.get() ;
		if(currentSession != null) {
			closeSession(currentSession) ;
		}
    log_.debug("open new hibernate session in openSession()") ;
		SessionFactory factory = getSessionFactory() ;
		currentSession = factory.openSession();
		threadLocal_.set(currentSession) ;
		return currentSession ;
	}
	
	public SessionFactory getSessionFactory() {
		if (factory_ == null) {
			synchronized(this) {
				try {
					factory_ = config_.buildSessionFactory();  // Hibernate2 init
				} catch (Throwable t) {
					log_.error("HibernateServiceImpl: could not configure hibernate using: ", t) ;
				}
			}
		}
		return factory_ ;  
	}
	
	public void closeSession(Session session) {
		if (session == null) return ;
		try {
			session.close();
      log_.debug("close hibernate session in openSession(Session session)") ;
		} catch (Throwable t) { log_.error("Error: " , t)  ; }
    threadLocal_.set(null) ;
	}
	
	public void closeSession() {
		Session currentSession = (Session)threadLocal_.get() ;
		if(currentSession != null) {
			closeSession(currentSession) ;
      log_.debug("close hibernate session in openSession()") ;
		}
	}
	
	public Object findExactOne(Session session, String query, String id) throws Exception {
		List l = session.find(query, id, Hibernate.STRING);
		if ( l.size() == 0) {
			throw new ObjectNotFoundException("Cannot find the object with id: " + id);
		} else if ( l.size() > 1) {
			throw new Exception("Expect only one object but found" + l.size());
		} else {
			return  l.get(0) ;
		}
	}
	
	public Object findOne(Session session, String query, String id) throws Exception {
		List l = session.find(query, id, Hibernate.STRING);
		if ( l.size() == 0) {
			return null ;
		} else if ( l.size() > 1) {
			throw new Exception("Expect only one object but found" + l.size());
		} else {
			return  l.get(0) ;
		}
	}
  
	public Object findOne(Class clazz, Serializable id) throws Exception {
    Session session = openSession() ;
    Object obj = session.get(clazz, id) ;
    return obj ;
  }
  
	public Object findOne(ObjectQuery q) throws Exception {
    Session session = openSession() ;
	  List l = session.find(q.getHibernateQuery());
    if ( l.size() == 0) {
      return null ;
    } else if ( l.size() > 1) {
      throw new Exception("Expect only one object but found" + l.size());
    } else {
      return  l.get(0) ;
    }
	}
	
  public Object create(Object obj) throws Exception {
    Session session = openSession() ;
    session.save(obj) ;
    session.flush();
    return obj ; 
  }
  
  public Object update(Object obj) throws Exception {
    Session session = openSession() ;
    session.update(obj) ;
    session.flush();
    return obj ; 
  }
  
  public Object save(Object obj) throws Exception {
    Session session = openSession() ;
    session.saveOrUpdateCopy(obj) ;
    session.flush();  
    return obj ; 
  }
  
  public Object remove(Object obj) throws Exception {
    Session session = openSession() ;
    session.delete(obj) ;
    session.flush();
    return obj ; 
  }
 
  public Object remove(Class clazz , Serializable id) throws Exception  {
    Session session = openSession() ;
    Object obj = session.get(clazz, id) ;
    session.delete(obj) ;
    session.flush() ;
    return obj ;
  }
  
  public Object remove(Session session ,Class clazz , Serializable id) throws Exception {
    Object obj = session.get(clazz, id) ;
    session.delete(obj) ;
    return obj ;
  }
  
	synchronized public void addMappingFiles(String[] path) {
    closeSession() ;
		ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
		try {
      Configuration temp = new Configuration() ;
			for (int i = 0 ; i < path.length; i++) {
				URL url = cl.getResource(path[i]) ;
				config_.addURL(url) ;
        temp.addURL(url) ;
			}
      createTables(temp) ;
			if (factory_ != null) {
				factory_.close() ;
				factory_ = null ;
			}
		} catch (Throwable t) {
			log_.error("HibernateServiceImpl: could not configure hibernate", t);
		}
	}
	
	private void createTables(Configuration config) throws Exception {
		Dialect dialect = Dialect.getDialect(config_.getProperties()) ;
		String[] script = config_.generateSchemaCreationScript(dialect) ;
		for (int i = 0; i < script.length; i++) {
			createTable(script[i]) ;
		}
	}
	
	private void createTable(String script) { 
    Connection conn = null ;
		try {
      conn = dbservice_.getDataSource(datasource_).getConnection();
			PreparedStatement ps = conn.prepareStatement(script);
			ps.executeUpdate();
      conn.commit() ;
      conn.close() ;
		} catch (Exception ex) {
			log_.info("Try create the table but fail. probably the table are already created");
		} finally { 
      if(conn != null) {
        try {
         conn.close() ;
        } catch(Exception ex) {
          //log_.error(ex);
        }
      }
    }
	}
}
