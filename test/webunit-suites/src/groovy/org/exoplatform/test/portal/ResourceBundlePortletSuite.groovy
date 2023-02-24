/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.portal ;

import org.exoplatform.test.web.*;
import org.exoplatform.test.web.condition.*;
import org.exoplatform.test.web.unit.*;
import org.exoplatform.test.web.validator.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ResourceBundlePortletSuite.java,v 1.1 2004/10/11 23:27:29 tuan08 Exp $
 **/
public class ResourceBundlePortletSuite extends WebUnitSuite {
  final static public String EXOPLATFORM_ACCOUNT = "exoplatform" ;
  final static public String ACCOUNT_TO_DELETE = "accountToDelete" ;
  
  public ResourceBundlePortletSuite(String path) {
    super("ResourcesPortletSuite", "Go to resources portket and test all the basic actions") ;
    goToPage(path) ;
    addWebUnit(
        new SubmitFormUnit("SubmitSearcResourceForm", "search for exo user resources").
        setFormName("searchForm").
        setField(WebUnit.ACTION ,"searchResource").
        setField("name", "*users.exo*").
        setField("language","en").
        addValidator(new ExpectLinkWithURLValidator("op=view&objectId=", 1))
    ); //expect 1 resources are found
    addWebUnit(
        new CurrentResponseUnit("CheckNoAdminRole", "Check there is no delete resource link for non admin user").
        addCondition(new NoRoleCondition("admin")).
        addValidator(new ExpectLinkWithURLValidator("op=delete&objectId=", 0)).
        addValidator(new ExpectLinkWithTextValidator("New Resource", 0))
    );
    addWebUnit(
        new ClickURLLinkUnit("ClickViewResource", "Click on the first view resource link").
        setURL("op=view&objectId=").
        addValidator(new ExpectLinkWithTextValidator("cancel"))
    ) ; 
    addWebUnit(
        new SubmitFormUnit("ClickOnCancel", "Click on cancel button and go back to the resource list").
        setFormName("resource").
        setField(WebUnit.ACTION ,"PhaseId[1].cancel").
        addValidator(new ExpectLinkWithURLValidator("op=view&objectId=", 1))
    ) ;
    //=================user with admin role action=================================
    addWebUnit(
        new CurrentResponseUnit("CheckAdminRole", "Check there is  delete resource link for  admin user").
        addCondition(new HaveRoleCondition("admin")).
        addValidator(new ExpectLinkWithURLValidator("op=delete&resourceId=", 1)).
        addValidator(new ExpectLinkWithTextValidator("New Resource", 1))
    );           
    // test create new resource 
    addWebUnit(
        new SubmitFormUnit("ClickOnNewResource", "Click on New Resource link").
        setFormName("searchForm").
        setField(WebUnit.ACTION,"newResource").
        addCondition(new HaveRoleCondition("admin")).
        addValidator(new ExpectFormNameValidator("resource"))
    ) ;
    addWebUnit(
        new SubmitFormUnit("SubmitResourceWithError", "save resource").
        setFormName("resource").
        setField(WebUnit.ACTION,"save").
        setField("text", "This is a test").
        setField("language","en").
        addCondition(new HaveRoleCondition("admin")).
        addValidator(new ExpectTextValidator("You need to enter a name for the resource"))
    );
    addWebUnit(
        new SubmitFormUnit("SubmitResourceNoError", "save resource").
        setFormName("resource").
        setField(WebUnit.ACTION, "save").
        setField("text", "This is a test").
        setField("name","locale.users.test").
        setField("language","en").
        addCondition(new HaveRoleCondition("admin")).
        addValidator(new ExpectTextValidator("Your Resource has been updated successfully"))
    );
    addWebUnit(
        new SubmitFormUnit("ClickOnCancel", "Click on cancel and go back to resurce list").
        setFormName("resource").
        setField(WebUnit.ACTION, "PhaseId[1].cancel").
        addCondition(new HaveRoleCondition("admin")).
        addValidator(new ExpectFormNameValidator("searchForm"))
    );
    //test delete resource
    addWebUnit(
        new SubmitFormUnit("SearchNewCreatedResource", "search for the new created resource").
        setFormName("searchForm").
        setField("op","searchResource").
        setField("name", "locale.users.test").
        setField("language","en").
        addCondition(new HaveRoleCondition("admin")).
        addValidator(new ExpectLinkWithURLValidator("op=delete&objectId=", 1))
    ); 
    addWebUnit(
        new ClickURLLinkUnit("ClickDeleteResource", "Click on the delete resource link").
        setURL("op=delete&objectId=").
        addCondition(new HaveRoleCondition("admin")).
        addValidator(new ExpectLinkWithURLValidator("op=view&objectId=", 0))
    ) ;
  }
}