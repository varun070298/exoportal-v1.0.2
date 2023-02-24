/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.wsrp.component;

import java.util.* ;
import javax.faces.context.FacesContext ;

import org.apache.commons.logging.Log;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.services.wsrp.type.*;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIOfferedPortlet.java,v 1.9 2004/06/30 19:54:42 tuan08 Exp $
 */
public class UIOfferedPortlet extends UIGrid implements Node{
  protected static Log log_ = getLog("org.exoplatform.portlets.wsrp") ;
  private static Parameter[] backParams_ = 
      { new Parameter(FacesConstants.ACTION, "back") };
  
  private Cell portletHandle_ ;
  private Cell groupId_ ;
  private Cell title_ ;
  private Cell shortTitle_ ;
  private Cell displayName_ ;
  private Cell keywords_ ;
  private Cell markupTypes_ ;
  private Cell userCategories_ ;
  private Cell userProfileItems_ ;
  private Cell usesMethodGet_ ;
  private Cell defaultMarkupSecure_ ;
  private Cell onlySecure_ ;
  private Cell userContextStoredInSession_ ;
  private Cell templatesStoredInSession_ ;
  private Cell hasUserSpecificState_ ;
  private Cell doesUrlTemplateProcessing_ ;
  private Cell extensions_ ;
   
  public UIOfferedPortlet(ResourceBundle res) {
    setId("offered-portlet-detail");

    portletHandle_ = new Cell("") ;
    groupId_ = new Cell("") ;
    title_ = new Cell("") ;
    shortTitle_ = new Cell("") ;
    displayName_ = new Cell("") ;
    keywords_ = new Cell("") ;
    markupTypes_ = new Cell("") ;
    userCategories_ = new Cell("") ;
    userProfileItems_ = new Cell("") ;
    usesMethodGet_ = new Cell("") ;
    defaultMarkupSecure_ = new Cell("") ;
    onlySecure_ = new Cell("") ;
    userContextStoredInSession_ = new Cell("") ;
    templatesStoredInSession_ = new Cell("") ;
    hasUserSpecificState_ = new Cell("") ;
    doesUrlTemplateProcessing_ = new Cell("") ;
    extensions_ = new Cell("") ;
   
    String backButton = "#{UIOfferedPortlet.button.back}" ; 
    setClazz("UIOfferedPortlet") ;
    add(new HeaderRow().
        add(new Cell("#{UIOfferedPortlet.header.offered-portlet-detail}").
            addHeight("30").addColspan("2"))) ;
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.portlet-handle}")).
        add(portletHandle_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.group-id}")).
        add(groupId_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.title}")).
        add(title_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.short-title}")).
        add(shortTitle_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.display-name}")).
        add(displayName_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.keywords}")).
        add(keywords_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.markup-types}")).
        add(markupTypes_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.user-categories}")).
        add(userCategories_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.user-profile-items}")).
        add(userProfileItems_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.uses-method-get}")).
        add(usesMethodGet_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.default-markup-secure}")).
        add(defaultMarkupSecure_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.only-secure}")).
        add(onlySecure_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.user-context-stored-in-session}")).
        add(userContextStoredInSession_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.templates-stored-in-session}")).
        add(templatesStoredInSession_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.user-specific-state}")).
        add(hasUserSpecificState_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.url-template-processing}")).
        add(doesUrlTemplateProcessing_));
    add(new Row().
        add(new LabelCell("#{UIOfferedPortlet.label.extensions}")).
        add(extensions_));
  }
  
  public void populate(PortletDescription desc) {
    StringBuffer value = new StringBuffer() ;
    portletHandle_.setValue(desc.getPortletHandle()) ;
    groupId_.setValue(desc.getGroupID()) ;
    title_.setValue(getValue(desc.getTitle())) ;
    shortTitle_.setValue(getValue(desc.getShortTitle())) ;
    displayName_.setValue(getValue(desc.getDisplayName())) ; 
    
    LocalizedString[] keywords = desc.getKeywords() ;
    if(keywords != null){
      for (int i = 0; i < keywords.length; i++) {
        value.append(getValue(keywords[i])).append(" ");
      }
    }
    keywords_.setValue(value.toString()) ;
    
    MarkupType[] types = desc.getMarkupTypes() ;
    value.setLength(0) ;
    for (int i = 0; i < types.length; i++) {
      value.append(types[i].getMimeType()).append(" ");
    }
    markupTypes_.setValue(value.toString()) ;

    String[] userCategories = desc.getUserCategories() ;
    if( userCategories != null) {
      value.setLength(0) ;
      for (int i = 0; i < userCategories.length; i++) {
        value.append(userCategories[i]).append(" ");
      }
      userCategories_.setValue(value.toString()) ;
    } else {
      userCategories_.setValue("") ;
    }
    
    String[] userProfileItems = desc.getUserProfileItems() ;
    if( userProfileItems != null) {
      value.setLength(0) ;
      for (int i = 0; i < userProfileItems.length; i++) {
        value.append(userProfileItems[i]).append(" ");
      }
      userProfileItems_.setValue(value.toString()) ;
    } else {
      userProfileItems_.setValue("") ;
    }

    usesMethodGet_.setValue(desc.getUsesMethodGet().toString()) ;
    if(desc.getDefaultMarkupSecure() != null)
      defaultMarkupSecure_.setValue(desc.getDefaultMarkupSecure().toString()) ;
    if(desc.getOnlySecure() != null)
      onlySecure_.setValue(desc.getOnlySecure().toString()) ;
    if(desc.getUserContextStoredInSession() != null)
      userContextStoredInSession_.setValue(desc.getUserContextStoredInSession().toString());
    if(desc.getTemplatesStoredInSession() != null)
      templatesStoredInSession_.setValue(desc.getTemplatesStoredInSession().toString()) ;
    if(desc.getHasUserSpecificState() != null)
      hasUserSpecificState_.setValue(desc.getHasUserSpecificState().toString()) ;
    if(desc.getDoesUrlTemplateProcessing() != null)
      doesUrlTemplateProcessing_.setValue(desc.getDoesUrlTemplateProcessing().toString()) ;

    extensions_.setValue("N/A") ;
  }
  
  public String getComponentType() { return COMPONENT_TYPE; }

  private String getValue(LocalizedString s) {
    if (s == null) return "" ;
    else return s.getValue() ;
  }

  public String getName() {
    return "";
  }

  public String getIcon() {
    return "no-icon";
  }

  public String getDescription() {
    return "no-description";
  }

}
