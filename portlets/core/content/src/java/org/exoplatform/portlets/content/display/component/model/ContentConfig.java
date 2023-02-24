/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component.model;


import org.apache.commons.lang.StringUtils;
/**
 * Apr 26, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ContentConfig.java,v 1.1 2004/07/16 09:29:51 oranheim Exp $
 **/
public class ContentConfig {
	private String name_ ;
	private String title_ ;
  private String uri_ ;
  private String encoding_ ;
  private String content_ ;
  
  public ContentConfig() {
  }
  
  public ContentConfig(String name, String[] values) {
    name_ = name ;    
    for(int i = 0; i < values.length; i++) {
      String[] tmp =  StringUtils.split(values[i], "=") ; 
      if (tmp.length == 2) {
      	if("uri".equals(tmp[0])) {
      		uri_ = tmp[1] ;
        } else if ("encoding".equals(tmp[0])) {
          encoding_ = tmp[1] ;
        } else if ("title".equals(tmp[0])) {
          title_ = tmp[1] ;
        }
      }
    }
    content_ = null;
  }
  
  /**
   * @return Returns the encoding_.
   */
	public String getEncoding() { return encoding_; }
	/**
	 * @param encoding_  The encoding_ to set.
	 */
	public void setEncoding(String encoding_) { this.encoding_ = encoding_; }
	/**
	 * @return Returns the name_.
	 */
	public String getName() { return name_; }
	/**
	 * @param name_ 
   * The name_ to set.
	 */
	public void setName(String name_) { this.name_ = name_; }
	/**
	 * @return Returns the title_.
	 */
	public String getTitle() { return title_; }
	/**
	 * @param title_
	 *            The title_ to set.
	 */
	public void setTitle(String title_) { this.title_ = title_; }
  
	/**
	 * @return Returns the uri_.
	 */
	// TODO: This hack is dirty, and should not be init to "", but all renderer that thorws nullptr's should be handled instead
	public String getUri() { 
    if (uri_==null) 
      uri_ = "";   
    return uri_; 
  }   
  
	/**
	 * @param uri_
	 *            The uri_ to set.
	 */
	public void setUri(String uri_) { this.uri_ = uri_; }
	
	public String getContent() { return content_; }
	
	public void setContent(String content) { this.content_ = content; }
	
}