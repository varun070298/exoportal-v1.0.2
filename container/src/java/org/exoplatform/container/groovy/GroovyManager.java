/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.groovy;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.* ;
import org.exoplatform.commons.utils.IOUtil;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 8, 2004
 * @version $Id$
 */
public class GroovyManager {
  private URL classpath_ ;
  private Map cacheObject_ ;
  private GroovyClassLoader gcl_  ;
  private List listeners_ ;
  private boolean dispose_ ;
  
  public GroovyManager(URL classpath) throws Exception {
    classpath_ = classpath ;
    listeners_ = new ArrayList(5) ;
    listeners_.add(new GroovyManagerListener()) ;
    cacheObject_ = new HashMap() ;
    gcl_ = createGroovyClassLoader() ;
    dispose_ = false ;
  }
  
  public boolean isDispose() { return dispose_ ; }
  public void    setDispose(boolean b) { dispose_ = b ;  }
  
  public String getGroovyClassPath() { return classpath_.toString() ; }
 
  synchronized public void addListener(GroovyManagerListener listener) {
    listeners_.add(listener) ;
  }
  
  synchronized public void removeListener(GroovyManagerListener listener) {
    listeners_.remove(listener) ;
  }
  
  synchronized public void removeAllListener() { listeners_.clear() ; }
  
  public Object getObject(String resource) throws Exception {
    GroovyObject  gobject=  getGroovyObject(resource) ;
    if(gobject.getType() == null) {
      gobject.setType(gcl_ );
      GroovyManagerListener.reload(listeners_, gobject) ;
    }
    return gobject.getObject() ; 
  }
  
  public GroovyClassLoader getGroovyClassLoader() { return gcl_ ; }
  
  public GroovyObject getGroovyObject(String resource) throws Exception {
    GroovyObject gobject = (GroovyObject) cacheObject_.get(resource) ;
    if(gobject == null) {
      synchronized (cacheObject_){
        gobject = new GroovyObject(resource) ;
        gobject.setType(gcl_ );
        GroovyManagerListener.load(listeners_, gobject) ;
        cacheObject_.put(resource, gobject) ;
      }
    }
    return gobject ;
  }
  
  public String getGroovyObjectAsText(String resource) throws Exception {
    return IOUtil.getStreamContentAsString(gcl_.getResourceAsStream(resource)) ;
  }
  
  public void saveGroovyObject(String resource, String text) throws Exception {
    String url = gcl_.getResource(resource).toString() ;
    if(!url.startsWith("file:")) {
      throw new Exception("The  resources is in a jar file........") ;
    }
    String filePath = url.substring("file:".length() + 1, url.length()) ;
    FileOutputStream os = new FileOutputStream(filePath) ;
    os.write(text.getBytes()) ;
    os.close() ;
  }
  
  private GroovyClassLoader createGroovyClassLoader() throws Exception {
    ClassLoader parentLoader = Thread.currentThread().getContextClassLoader() ;
    if(classpath_ != null) {
      URL[] url = { classpath_ } ;
      parentLoader =  new URLClassLoader(url, Thread.currentThread().getContextClassLoader()) ;
      String cpath = System.getProperty("java.class.path") ;
      String newpath = classpath_.toString() ;
      newpath = newpath.substring("file:".length(), newpath.length()) ;
      if(cpath.indexOf(newpath) < 0) {
        cpath = cpath + File.pathSeparator + newpath ;
        System.setProperty("java.class.path", cpath) ;
      } 
    } 
    return  new GroovyClassLoader(parentLoader) ;
  }
  
  public void checkModifiedObjects() throws Exception {
    Iterator i = cacheObject_.values().iterator() ;
    while(i.hasNext()) {
      GroovyObject gobject = (GroovyObject) i.next() ;
      if(gobject.isReloadable() && isObjectModified(gobject)) {
        reloadGroovyObjects();
        return ;
      }
    }
  }
  
  public void reloadGroovyObjects() throws Exception {
    synchronized(cacheObject_) {
      Iterator i = cacheObject_.values().iterator() ;
      gcl_ = createGroovyClassLoader() ;
      while(i.hasNext()) {
        GroovyObject gobject = (GroovyObject) i.next() ;
        GroovyManagerListener.unload(listeners_, gobject) ;
        gobject.setObject(null) ;
      }
    }
  }
  
  private boolean isObjectModified(GroovyObject gobject) throws Exception {
    URL resourceURL  = gcl_.getResource(gobject.getGroovyResource()) ;
    //in case of redeploying the resource , the  resource url will be not found 
    //for a period , until the server explode the war file.
    if(resourceURL == null) return false ;
    String url =  resourceURL.toString();
    String filePath = url.substring("file:".length(), url.length()) ;
    File file = new File(filePath) ;
    long lastModifiedTime = file.lastModified() ;
    if(lastModifiedTime >  gobject.getLoadTime())  {
      return true ;
    }
    return false ;
  }
}