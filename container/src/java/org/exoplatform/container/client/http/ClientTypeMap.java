/**
 * Jul 8, 2004, 3:47:17 PM
 * @author: F. MORON
 * @email: francois.moron@laposte.net
 * 
 * */
package org.exoplatform.container.client.http;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class ClientTypeMap {
  private ArrayList clientList_;
  private static ClientTypeMap singleton_;
  
  private void loadClientsInfos() {
    try {
      ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
      java.net.URL url = cl.getResource("conf/portal/clients-type.xml") ;
      SAXReader reader = new SAXReader();
      Document document = reader.read(url.openStream());
      List list = document.selectNodes("//clients-type/client-type");
      HttpClientType clientInfo;
      for(int i = 0; i <  list.size(); i++) {
        Node node = (Node) list.get(i) ;
        String name = node.selectSingleNode("name").getText() ;
        String userAgentPattern = node.selectSingleNode("userAgentPattern").getText() ;
        String preferredMimeType = node.selectSingleNode("preferredMimeType").getText() ;
        String renderer = node.selectSingleNode("type").getText() ;
        if (renderer.length() > 0) 
          clientInfo = new HttpClientType(name, userAgentPattern, preferredMimeType, renderer);
        else clientInfo = new HttpClientType(name, userAgentPattern, preferredMimeType);
        addClientInfo(clientInfo);
      } 
    } catch (Exception ex) {
      ex.printStackTrace() ;
    }
  }
  
  public ClientTypeMap() {
    clientList_ = new ArrayList() ;
    loadClientsInfos();
  }
  
  protected void addClientInfo(HttpClientType clientInfo) {
    clientList_.add(clientInfo);
  }
  
  /*
   * @return ClientInfo according to userAgent parameter and first
   * ClientInfo (ie5) if not found or if userAgent is null 
   * 
   */
  public HttpClientType findClient(String userAgent) {
    if (userAgent == null) return (HttpClientType) clientList_.get(0);
    if (userAgent.equals("")) return (HttpClientType) clientList_.get(0);
    HttpClientType client;
    for (int i = 0; i < clientList_.size(); i++) {
      client = (HttpClientType) clientList_.get(i);
      String userAgentPattern = client.getUserAgentPattern(); 
      if (userAgentPattern != null) {
        try {
          if (userAgent.matches(userAgentPattern)) return client;
        } catch (PatternSyntaxException e) {
          e.printStackTrace() ;
          return (HttpClientType) clientList_.get(0);
        }
      }
    }
    
    return (HttpClientType) clientList_.get(0);
  }
  
  public static ClientTypeMap getInstance() {
    if (singleton_ == null) {
      synchronized (ClientTypeMap.class) {
        singleton_ = new ClientTypeMap();
      }
    }
    return singleton_;
  }
}
