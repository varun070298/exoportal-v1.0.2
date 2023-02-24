/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.sf.hibernate.Session;
import org.exoplatform.services.backup.Metadata;
import org.exoplatform.services.backup.XMLObjectConverter;
import org.exoplatform.xml.object.XMLObject;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 21, 2004
 * @version $Id$
 */
public class ForumImporter {
  private Session session_ ;
  private ZipFile jar_ ;
  private  XMLObjectConverter converter_ ;
  
  public ForumImporter(ImportExportPluginImpl plugin, 
                       Session session, ZipFile zipFile) throws Exception {
    session_ = session ;
    jar_ = zipFile ;
    Metadata mdata = 
      plugin.getMetadata(ImportExportPluginImpl.SERVICE_META_DATA_ENTRY, zipFile) ;
    String dataVersion = mdata.getDataVersion() ;
    converter_ = plugin.getXMLObjectConverter(dataVersion) ;
  }
  
  public void importData() throws Exception {
    Enumeration e = jar_.entries() ;
    while(e.hasMoreElements()) {
      ZipEntry entry = (ZipEntry) e.nextElement() ;
      InputStream is = jar_.getInputStream(entry) ;
      XMLObject xmlobject = XMLObject.getXMLObject(is) ;
      if(converter_ != null) converter_.traverse(xmlobject) ;
      String entryName = entry.getName() ;
      if(entryName.endsWith(".category")) {
        CategoryImpl category = (CategoryImpl) xmlobject.toObject() ; 
        session_.saveOrUpdateCopy(category) ;
      } else if(entryName.endsWith(".forum")) {
        ForumImpl forum  = (ForumImpl) xmlobject.toObject() ;
        session_.saveOrUpdateCopy(forum) ;
      } else if(entryName.endsWith(".topic")) {
        TopicBackup backup = (TopicBackup) xmlobject.toObject() ; 
        TopicImpl topic = backup.getTopic() ;
        List posts = backup.getPosts() ;
        for(int i = 0 ; i < posts.size(); i++) {
          PostImpl post = (PostImpl) posts.get(i) ;
          session_.saveOrUpdateCopy(post) ;
        }
        session_.saveOrUpdateCopy(topic) ;
      }
      session_.flush() ;
    }
  }
}
