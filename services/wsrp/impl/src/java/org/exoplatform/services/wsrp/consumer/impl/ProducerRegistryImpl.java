/*
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 */

package org.exoplatform.services.wsrp.consumer.impl;

import org.apache.commons.logging.Log;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.consumer.ProducerRegistry;

import net.sf.hibernate.Session;
import net.sf.hibernate.Hibernate;

import java.util.*;
import java.net.URL;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 2 févr. 2004
 * Time: 23:04:48
 */
public class ProducerRegistryImpl implements ProducerRegistry {

  private static String[]  MAPPING =
    {
      "org/exoplatform/services/wsrp/consumer/impl/ProducerData.hbm.xml"
    };

  private static final String queryAllProducer =
      "from pd in class org.exoplatform.services.wsrp.consumer.impl.ProducerData";
  private static final String queryProducer =
      "from pd in class org.exoplatform.services.wsrp.consumer.impl.ProducerData " +
      "where pd.id = ?";

  private long lastModifiedTime_;
  private Map producers;
  private HibernateService hservice_;
  private Log log_;

  public ProducerRegistryImpl(DatabaseService dbService,
                              HibernateService hservice,
                              LogService lservice) {
    hservice_ = hservice;
    log_ = lservice.getLog("org.exoplatform.services.wsrp");
    hservice.addMappingFiles(MAPPING) ;
    //checkDatabase(dbService);
    producers = loadProducers();
    lastModifiedTime_ = System.currentTimeMillis();
  }

  private Map loadProducers() {
    Map map = new HashMap();
    try {
      Collection c = loadAll();
      for (Iterator iterator = c.iterator(); iterator.hasNext();) {
        ProducerData producerData = (ProducerData) iterator.next();
        ((ProducerImpl)producerData.getProducer()).init();
        map.put(producerData.getId(), producerData.getProducer());
      }
    } catch (Exception e) {
      log_.error("Error", e) ;
    }
    return map;
  }

  public void addProducer(Producer producer) {
    try {
      save(producer);
      producers.put(producer.getID(), producer);
      lastModifiedTime_ = System.currentTimeMillis();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Producer getProducer(String id) {
    return (Producer) producers.get(id);
  }

  public Iterator getAllProducers() {
    return producers.values().iterator();
  }

  public Producer removeProducer(String id) {
    try {
      remove(id);
      producers.remove(id);
      lastModifiedTime_ = System.currentTimeMillis();
      Producer producer = (Producer) producers.get(id);
      return producer;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void removeAllProducers() throws Exception {
    removeAll() ;
    producers.clear();
    lastModifiedTime_ = System.currentTimeMillis();
  }

  public boolean existsProducer(String id) {
    return producers.containsKey(id);
  }

  public Producer createProducerInstance() {
    return new ProducerImpl();
  }

  public long getLastModifiedTime() {
    return lastModifiedTime_;
  }

  final public void save(Producer p) throws Exception {
  	Session session = hservice_.openSession();
  	ProducerData data = load(p.getID());
  	if (data == null) {
  		data = new ProducerData();
  		data.setId(p.getID());
  		data.setProducer(p);
  		session.save(data);
  	} else {
  		data.setProducer(p);
  		session.update(data);
  	}
  	session.flush();
  }

  final public Collection loadAll() throws Exception {
  	Session session = hservice_.openSession();
  	return session.find(queryAllProducer);
  }


  final public ProducerData load(String id) throws Exception {
  	Session session = hservice_.openSession();
  	ProducerData data = load(id, session);
  	return data;
  }

  final public ProducerData load(String id, Session session) throws Exception {
    ProducerData data = null;
    List l = session.find(queryProducer, id, Hibernate.STRING);
    if (l.size() > 1) {
      throw new Exception("Expect only one configuration but found" + l.size());
    } else if (l.size() == 1) {
      data = (ProducerData) l.get(0);
    }
    return data;
  }
  
  final public void remove(String id) throws Exception {
  	Session session = hservice_.openSession();
  	session.delete(queryProducer, id, Hibernate.STRING);
  	session.flush();
  }

  final public void removeAll()  throws Exception {
  	Session session = hservice_.openSession();
  	session.delete(queryAllProducer);
  	session.flush();
  }
}
