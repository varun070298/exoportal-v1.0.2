/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content;

import java.util.HashMap ;
import java.util.ResourceBundle;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MimeTypeUtil.java,v 1.1 2004/02/29 02:33:04 dhodnett Exp $
 */
public class MimeTypeManager {
  static private MimeTypeManager singleton_ ;
  
  private HashMap mimeTypeMapping_ ;
  private MimeType defaultMimeType_ ;
  private MimeType directoryType_ ;

  private MimeTypeManager() {
    mimeTypeMapping_ = new HashMap() ;
    ResourceBundle res = ResourceBundle.getBundle("locale.portlet.content.MimeType") ;
    String extensions = res.getString("support.extensions") ;
    String[] ext = extensions.split(",") ;
    for(int i =0; i < ext.length; i++) {
      String extension = ext[i].trim() ;
      String mimetype = res.getString("mimetype." + extension) ;
      String icon = res.getString("mimetype." + extension + ".icon") ;
      MimeType mt = new MimeType(extension, mimetype, icon) ;
      mimeTypeMapping_.put(extension, mt) ;
    }
    defaultMimeType_ = (MimeType)mimeTypeMapping_.get("bin") ;
    directoryType_ = (MimeType)mimeTypeMapping_.get("directory") ;
  }
  
  public MimeType getMimeTypeByOfFile(String fileName) {
    String ext = getFileExtension(fileName) ;
    MimeType mimeType = (MimeType) mimeTypeMapping_.get(ext) ;
    if (mimeType == null) { 
      mimeType = defaultMimeType_ ;
    }
    return mimeType ;
  }
  
  public MimeType getDirectoryType() { return directoryType_ ; }
  
  public String getFileExtension(String fileName) {
    int idx = fileName.lastIndexOf(".") ;
    String ext = "bin" ;
    if (idx > 0) {
      ext = fileName.substring(idx + 1, fileName.length()) ; 
    }
    ext = ext.toLowerCase() ;
    return ext ;
  }
  
  static public MimeTypeManager getInstance() { 
    if(singleton_ == null) {
      singleton_ = new MimeTypeManager() ;
    }
    return singleton_ ;
  }
  
  static public class MimeType {
    private String fileExtension_ ;
    private String mimeType_ ;
    private String icon_ ;
    
    public MimeType(String ext, String type, String icon) {
      fileExtension_ = ext ;
      mimeType_ = type ;
      icon_ = icon ;
    }
    
    public String getExtension() { return fileExtension_ ; }
    
    public String getMimeType() { return mimeType_ ; }
    
    public String getIcon() { return icon_ ; }
  }
}
