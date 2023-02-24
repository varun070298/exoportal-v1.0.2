/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.util.*;

import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.event.ExoActionEvent;
/**
 * Wed, Dec 22, 2003 @ 23:14
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: UIFileUpload.java,v 1.5 2004/06/22 21:46:04 tuan08 Exp $
 */
public class UIFileUpload extends UIExoCommand {
	public static final String UPLOAD_ACTION = "upload";
	public static final String CANCEL_ACTION = "cancel";

  private int numberOfField_;
  private String userAction_;
  private HashMap userInputs_;
  private String parentUri_;
  private boolean showCancel_ = true ;
  private boolean showHeader_ = true ;

  public UIFileUpload() {
    setRendererType("FileUploadRenderer");
    setId("UIFileUpload");
    numberOfField_ = 3;
    userInputs_ = new HashMap();
  }
  
  public void decode(FacesContext context) {
  	Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String uicomponent = (String) paramMap.get(UICOMPONENT) ;
    if (!getId().equals(uicomponent)) return ;
    PortletRequest request = (PortletRequest) context.getExternalContext().getRequest();
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    userInputs_.clear();
    try {
      if (FileUpload.isMultipartContent(httpRequest)) {
        DiskFileUpload upload = new DiskFileUpload();
        List items = upload.parseRequest(httpRequest);
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
          FileItem item = (FileItem) iter.next();
          if (!item.isFormField()) {
            String fileField = item.getFieldName();
            String fileName = item.getName();
            byte[] buf = item.get();
            if (buf != null && buf.length > 0) {
              userInputs_.put(fileField, new UserInput(null, fileName, buf));
            }
          }
        }
        iter = items.iterator();
        while (iter.hasNext()) {
          FileItem item = (FileItem) iter.next();
          if (item.isFormField()) {
            String name = item.getFieldName();
            if (FacesConstants.ACTION.equals(name)) {
              userAction_ = item.getString();
            } else {
              String value = item.getString();
              int index = name.lastIndexOf("-");
              String number = name.substring(index + 1, name.length());
              String fileField = "file-" + number;
              UserInput input = (UserInput) userInputs_.get(fileField);
              if (input != null) {
                input.setName(value);
              }
            }
          }
        }
      } else {
        userAction_ = "cancel";
      }
      broadcast(new ExoActionEvent(this, userAction_));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public int getNumberOfField() { return numberOfField_; }
  public void setNumberOfField(int numberOfField) {  numberOfField_ = numberOfField;}

  public String getUserAction() { return userAction_; }

  public String getParentUri() { return parentUri_; }

  public void setParentUri(String uri) { parentUri_ = uri; }

  public Collection getUserInputs() { return userInputs_.values(); }
  
  public boolean isShowCancel() { return showCancel_ ; }
  public void    setShowCancel(boolean b) { showCancel_ = b ; }
  
  public boolean isShowHeader() { return showHeader_ ; }
  public void    setShowHeader(boolean b) { showHeader_ = b ; }

  public class UserInput {
    String fileName_;
    String name_;
    byte[] buf_;

    public UserInput(String name, String fileName, byte[] buf) {
      name_ = name;
      fileName_ = fileName;
      buf_ = buf;

    }

    public String getName() { return name_; }

    public void setName(String name) {  name_ = name;  }

    public String getFileName() {  return fileName_;  }

    public void setFileName(String name) {  fileName_ = name; }

    public byte[] getStream() { return buf_;   }

    public void setStream(byte[] buf) { buf_ = buf; }
  }
}
