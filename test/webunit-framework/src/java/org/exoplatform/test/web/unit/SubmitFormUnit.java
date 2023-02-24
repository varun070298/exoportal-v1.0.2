/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.unit;

import java.util.* ;
import java.io.File;
import org.exoplatform.test.web.*;
import com.meterware.httpunit.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: SubmitFormUnit.java,v 1.3 2004/10/12 14:21:12 tuan08 Exp $
 **/
public class SubmitFormUnit extends WebUnit {
  private String    formName_;
  private ArrayList parameters_;
  private String submitButton_ ;

  public SubmitFormUnit(String name, String description) {
    super(name, description);
    parameters_ = new ArrayList(5);
  }

  public SubmitFormUnit setFormName(String name) {
    formName_ = name;
    return this;
  }

  public SubmitFormUnit setField(String field, String value) {
    parameters_.add(new BasicParameter(field, value));
    return this;
  }

  public SubmitFormUnit setSubmitButton(String name) {
    submitButton_ = name ;
    return this;
  }
  
  public SubmitFormUnit setField(Map map) {
    Iterator i = map.entrySet().iterator() ;
    while(i.hasNext()) {
      Map.Entry entry = (Map.Entry) i.next() ;
      parameters_.add(new BasicParameter((String)entry.getKey(), (String)entry.getValue()));
    }
    return this;
  }

  public SubmitFormUnit setFileField(String field, File value) {
    ExoUploadFileSpec[] array = { new ExoUploadFileSpec(value) };
    parameters_.add(new FileParameter(field, array));
    return this;
  }
  
  public SubmitFormUnit setFileField(String field, String resourceName,  java.io.InputStream in) {
    ExoUploadFileSpec[] array = { new ExoUploadFileSpec(field, in, resourceName) };
    parameters_.add(new FileParameter(field, array));
    return this;
  }  
  
  
  public SubmitFormUnit setCheckBox(String field, boolean state) {
    parameters_.add(new CheckboxParameter(field, state));
    return this;
  }

  public WebResponse execute(WebResponse previousResponse, WebTable block, ExoWebClient client)
      throws Exception {
    WebForm form = Util.findFormWithName(previousResponse, block, formName_);
    for (int i = 0; i < parameters_.size(); i++) {
      Parameter param = (Parameter) parameters_.get(i);
      param.setWebFormValue(form, client) ;
    }
    WebResponse response = null ;
    if(submitButton_ == null) {
      response = form.submit();
    } else {
      response = form.submit(form.getSubmitButton(submitButton_));
    }
    return response;
  }

  interface Parameter {
    public void setWebFormValue(WebForm form, ExoWebClient client) ;
  }
  
  class ExoUploadFileSpec extends UploadFileSpec {
    String resourceName_ ;
    public ExoUploadFileSpec(java.io.File file) {
      super(file) ;
      resourceName_ = file.getAbsolutePath();
    }
    
    public ExoUploadFileSpec(String fileName, java.io.InputStream inputStream, String resourceName) {
      super(fileName, inputStream, null) ;
      resourceName_ = resourceName ;
    }
  }
  
  static class BasicParameter implements Parameter {
    String field_;
    String value_;

    public BasicParameter(String field, String value) {
      field_ = field;
      value_ = value;
    }
    
    public void setWebFormValue(WebForm form, ExoWebClient client)  {
      String value = getRealValue(client, value_);
      if (form.isReadOnlyParameter(field_) || form.isHiddenParameter(field_)) {
        //HttpUnitOptions.setParameterValuesValidated( false ) ;
        //form.getRequest().setParameter(param.field_ , value ) ;
        form.getScriptableObject().setParameterValue(field_, value);
      } else {
        form.setParameter(field_, value);
      }
    }
  }

  static class FileParameter implements Parameter {
    String           field_;
    ExoUploadFileSpec[] value_;

    public FileParameter(String field, ExoUploadFileSpec[] value) {
      field_ = field;
      value_ = value;
    }
    
    public void setWebFormValue(WebForm form, ExoWebClient client)  {
      form.setParameter(field_, value_);
    }
  }
  
  class CheckboxParameter implements Parameter {
    String           field_;
    boolean          state_;

    public CheckboxParameter(String field, boolean state) {
      field_ = field;
      state_ = state ;
    }

    public void setWebFormValue(WebForm form, ExoWebClient client)  {
      //form.setCheckbox(field_, state_) ;
      form.toggleCheckbox(field_) ;
    }
  }
  
  public String getActionDescription() { 
    return "This web unit submit the data of a form."; 
  }
  
  public String getExtraInfo() { 
    StringBuffer b = new StringBuffer() ;
    b.append("<pre>") ;
    b.  append("Form name: ").append(formName_).append("\n") ;
    for(int i = 0; i < parameters_.size() ; i++) {
      Object o = parameters_.get(i) ;
      if(o instanceof BasicParameter) {
        BasicParameter param = (BasicParameter) o ;
        b.append("Field: ").append(param.field_).append(", value: ").append(param.value_).append("\n") ;
      } else  if(o instanceof FileParameter) {
        FileParameter param = (FileParameter) o ;
        b.append("Field: ").append(param.field_).append("\n") ;
        for(int j = 0; j < param.value_.length; j++) {
          b.append("  file: ").append(param.value_[j].resourceName_).append("\n") ;
        }
      } else  if(o instanceof CheckboxParameter ) {
        CheckboxParameter param = (CheckboxParameter) o ;
        b.append("Field: ").append(param.field_).append(", state: ").append(param.state_).append("\n") ;
      }
    }
    b.append("</pre>") ;
    return b.toString() ;
  }
}
