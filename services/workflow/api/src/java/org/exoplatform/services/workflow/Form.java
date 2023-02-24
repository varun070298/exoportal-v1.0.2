package org.exoplatform.services.workflow;

import java.util.Map;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 mai 2004
 */
public interface Form {

  public List getVariableFormats();

  public List getSubmitButtons();

  public String getStateName();

  public ResourceBundle getResourceBundle();

  public class Attribute {
    String name_ ;
    Object value_ ;

    public Attribute(String name, Object value) {
      name_ = name ;
      value_ = value ;
    }

    public String getName() {
      return name_;
    }

    public Object getValue() {
      return value_;
    }
  }

}
