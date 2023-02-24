/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlet.faces.component;

import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.FormButton;
import org.exoplatform.faces.core.component.model.LabelCell;
import org.exoplatform.faces.core.component.model.ListComponentCell;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 31 ao�t 2004
 */
public class UIPreferencesForm extends UISimpleForm {
  private static final String ENCODING = "pref_";
  protected ResourceBundle res;


  public UIPreferencesForm(ResourceBundle res) {
    super("preferencesForm", "post", null);
    setClazz("UIPreferencesForm");
    this.res = res;
    FacesContext context = FacesContext.getCurrentInstance();
    PortletRequest request = (PortletRequest) context.getExternalContext().getRequest();
    PortletPreferences prefs = request.getPreferences();
    Enumeration names = prefs.getNames();
    String[] defaultValues = {""};
    while (names.hasMoreElements()) {
      String name = (String) names.nextElement();
      Row row = new Row();
      row.add(new LabelCell(name));
      ListComponentCell listComponentCell = new ListComponentCell();
      String[] values = prefs.getValues(name, defaultValues);
      for (int i = 0; i < values.length; i++) {
        String value = values[i];
        UIStringInput input = new UIStringInput(ENCODING  + name, value);
        listComponentCell.add(this, input);
      }
      add(row.add(listComponentCell));
    }

    add(new Row().
        add(new ListComponentCell().
        add(new FormButton("Save", SAVE_ACTION)).addAlign("center")));

    addActionListener(ExecuteActionListener.class, SAVE_ACTION);
  }

  static public class ExecuteActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      PortletRequest request = (PortletRequest) facesContext.getExternalContext().getRequest();
      PortletPreferences prefs = request.getPreferences();
      
      Enumeration enum = request.getParameterNames();
      while (enum.hasMoreElements()) {
        String paramName = (String) enum.nextElement();
        if (paramName.startsWith(ENCODING)) {
          String name = paramName.substring(5, paramName.length());
          String[] values =  request.getParameterValues(paramName);
          prefs.setValues(name, values);
        }
      }
      prefs.store();
      //((ActionResponse) facesContext.getExternalContext().getResponse()).setPortletMode(PortletMode.VIEW);
    }
  }
}
