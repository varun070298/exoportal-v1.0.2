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
 * @version: $Id: OrganizationPortletSuite.java,v 1.3 2004/10/22 14:23:55 tuan08 Exp $
 **/
public class OrganizationPortletSuite extends WebUnitSuite {
  final static public String EXOPLATFORM_ACCOUNT = "exoplatform" ;
  final static public String ACCOUNT_TO_DELETE = "accountToDelete" ;
  
  public OrganizationPortletSuite(String path) {
    super("OrganizationPortletSuite", "Go to organization page and test all the basic actions") ;
    goToPage(path) ;
    //create exoplatform account , the account may already existed , it is ok
    addWebUnit(CommonUnit.createNewAccountUnit(EXOPLATFORM_ACCOUNT, EXOPLATFORM_ACCOUNT)) ;
    //  //create accountToDelete account , the account may already existed , it is ok
    addWebUnit(CommonUnit.createNewAccountUnit(ACCOUNT_TO_DELETE, ACCOUNT_TO_DELETE)) ;
    
    addWebUnit(
      new ClickLinkWithText("ClickManagement", "Go to the management page").
      setTextLink("Management")
    ) ;
    //======================User List tab=============================================
    addWebUnit(
      new SubmitFormUnit("SubmitSearcUserForm", "search for user to delete").
      setFormName("quickSearchForm").
      setField("searchField","username").
      setField("term", ACCOUNT_TO_DELETE)
    ) ;
    addWebUnit(
      new ClickURLLinkUnit("ClickDeleteAccount", "Delete Account").
      setURL("op=delete&objectId="+ ACCOUNT_TO_DELETE)
    ) ;
    addWebUnit(
      new SubmitFormUnit("SubmitSearcUserForm", "search for user to delete").
      setFormName("quickSearchForm").
      setField("searchField","username").
      setField("term", "accountToDel*").
      addValidator(new ExpectNoTextValidator(ACCOUNT_TO_DELETE))
    ) ;
    addWebUnit(
      new SubmitFormUnit("SubmitSearcUserForm", "search for exoplatform user").
      setFormName("quickSearchForm").
      setField("searchField","username").
      setField("term", EXOPLATFORM_ACCOUNT)
    ) ;
    addWebUnit(
      new ClickURLLinkUnit("ClickViewUserProfile", "Click the view user profile icon").
      setURL("op=viewProfile&objectId="+ EXOPLATFORM_ACCOUNT).
      addValidator(new ExpectTextValidator("Personal Info"))
    ) ; 
    addWebUnit(
      new ClickLinkWithText("ClickBackLink", "Go back to the user list screen").
      setTextLink("Back")
    ) ;
    //======================user info screen==========================================
    addWebUnit(
      new ClickURLLinkUnit("ClickViewUserInfo", "Click the view user info icon").
      setURL("op=viewUserInfo&objectId="+ EXOPLATFORM_ACCOUNT).
      addValidator(new ExpectFormNameValidator("membership"))
    ) ;
    addWebUnit(
      new ClickLinkWithText("ClickBackLink", "Go back to the user list screen").
      setTextLink("Back")
    ) ;
    //======================group tab==========================================
    addWebUnit(
      new ClickURLLinkUnit("GoToGroupsTab", "Click on the groups tab").
      setURL("op=selectTab&tabId=UIGroupNode").
      addValidator(new ExpectTextValidator("Group::/"))
    ) ;
    //  create a new group 
    addWebUnit(
      new ClickLinkWithText("ClickAddGroup", "Go to the group form").
      setTextLink("Add Group").
      addValidator(new ExpectFormNameValidator("groupForm"))
    ) ;
    addWebUnit(
      new SubmitFormUnit("CreateNewGroup", "Create 'test' group").
      setFormName("groupForm").
      setField(WebUnit.ACTION, "save").
      setField("groupName", "newgroup").
      setField("description", "a new group").
      addValidator(new ExpectLinkWithTextValidator("newgroup"))
    ) ;
    //  Go to user list , select an user and assign the a membership 
    // to the user in the new created group
    addWebUnit(
      new ClickURLLinkUnit("GoToListUserTab", "Click on the users tab").
      setURL("op=selectTab&tabId=usernode").
      addValidator(new ExpectLinkWithURLValidator("uicomponent=UIListUser&op=viewUserInfo"))
    ) ;
    addWebUnit(
      new ClickURLLinkUnit("ClickViewUserInfo", "Click the view user info icon").
      setURL("op=viewUserInfo&objectId="+ EXOPLATFORM_ACCOUNT).
      addValidator(new ExpectFormNameValidator("membership"))
    ) ;
    addWebUnit(
      new SubmitFormUnit("SubmitNewMembership", "Assign 'member' to the user in the 'newgroup' group").
      setFormName("membership").
      setField(WebUnit.ACTION, "save").
      setField("mname", "member").
      setField("groupId", "/newgroup").
      addValidator(new ExpectTextValidator("newgroup"))
    ) ;
    addWebUnit(
      new ClickLinkWithText("ClickBackLink", "Go back to the user list screen").
      setTextLink("back").
      addValidator(new ExpectLinkWithURLValidator("uicomponent=UIListUser&op=viewUserInfo"))
   ) ;
    
    // go to the group tab and delete the new created user
    addWebUnit(
      new ClickURLLinkUnit("GoToGroupsTab", "Click on the groups tab").
      setURL("op=selectTab&tabId=UIGroupNode").
      addValidator(new ExpectTextValidator("Group::/"))
    ) ;
    addWebUnit(
      new ClickLink("Click:newgroup", "Go to 'newgroup'").
      setTextLink("newgroup").
      addValidator(new ExpectTextValidator("Group::/newgroup"))
    ) ;
    addWebUnit(
      new ClickLink("ClickRemoveGroup", "Remove 'newgroup'").
      setTextLink("Remove Group").
      addValidator(new ExpectTextValidator("Group::/")).
      addValidator(new ExpectLinkWithTextValidator("newgroup", 0))
    ) ;
    //======================membership tab==========================================
    addWebUnit(
      new ClickURLLinkUnit("GoToMembershipsTab", "Click on the memberships tab").
      setURL("op=selectTab&tabId=UIMembershipNode").
      addValidator(new ExpectLinkWithTextValidator("Add"))
    ) ;
    addWebUnit(
      new ClickURLLinkUnit("DeleteMemebershipIfExist", "remove 'newmembership' if exist").
      setURL("op=deleteMembership&objectId=newmembership").
      addCondition(new HaveLinkWithURLCondition("op=deleteMembership&objectId=newmembership"))
    ) ;
    // create new memberhip
    addWebUnit(
      new ClickLink("ClickAddMembershipType", "Click Add Membership type").
      setTextLink("Add").
      addValidator(new ExpectFormNameValidator("membershipTypeForm"))
    ) ;
    addWebUnit(
      new SubmitFormUnit("SubmitNewRole", "Create a new System Role").
      setFormName("membershipTypeForm").
      setField(WebUnit.ACTION, "save").
      setField("name", "newmembership").
      setField("description", "This is a new membership").
      addValidator(new ExpectTextValidator("This is a new membership"))
    ) ;
    //  Go to user list , select an user and assign the new created membership 
    // to the user
    addWebUnit(
      new ClickURLLinkUnit("GoToListUserTab", "Click on the users tab").
      setURL("op=selectTab&tabId=usernode").
      addValidator(new ExpectLinkWithURLValidator("uicomponent=UIListUser&op=viewUserInfo"))
    ) ;
    addWebUnit(
      new ClickURLLinkUnit("ClickViewUserInfo", "Click the view user info icon").
      setURL("op=viewUserInfo&objectId="+ EXOPLATFORM_ACCOUNT).
      addValidator(new ExpectFormNameValidator("membership"))
    ) ;
    addWebUnit(
      new SubmitFormUnit("SubmitNewMembership", "Assign 'newmembership' to the user in portal group").
      setFormName("membership").
      setField(op:"save",mname:"newmembership",groupId:"/portal").
      addValidator(new ExpectTextValidator("newmembership"))
    ) ;
    addWebUnit(
      new ClickLinkWithText("ClickBackLink", "Go back to the user list screen").
      setTextLink("Back")
    ) ;
    //Go back to memebership tab and delete the new created memebership, then check
    //the new created membershipt that assign to an user, it should be removed as well
    addWebUnit(
      new ClickURLLinkUnit("GoToMembershipsTab", "Click on the memberships tab").
      setURL("op=selectTab&tabId=UIMembershipNode").
      addValidator(new ExpectLinkWithTextValidator("Add"))
    ) ;
    addWebUnit(
      new ClickURLLinkUnit("DeleteNewMembership", "remove 'newmembership'").
      setURL("op=deleteMembership&objectId=newmembership").
      addValidator(new ExpectLinkWithURLValidator("op=deleteMembership&objectId=newmembership", 0))
    ) ;
    // Go to user info page and make sure the membership that assigned to him is removed
    addWebUnit(
      new ClickURLLinkUnit("GoToListUserTab", "Click on the users tab").
      setURL("op=selectTab&tabId=usernode").
      addValidator(new ExpectLinkWithURLValidator("uicomponent=UIListUser&op=viewUserInfo"))
    ) ;
    addWebUnit(
      new ClickURLLinkUnit("ClickViewUserInfo", "Click the view user info icon").
      setURL("op=viewUserInfo&objectId="+ EXOPLATFORM_ACCOUNT).
      addValidator(new ExpectNoTextValidator("newmembership"))
    ) ;
    addWebUnit(
      new ClickLinkWithText("ClickBackLink", "Go back to the user list screen").
      setTextLink("Back")
    ) ;
  }
}
