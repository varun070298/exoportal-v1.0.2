package org.exoplatform.portlets.exomvc ;

import org.exoplatform.portlet.exomvc.config.* ;
import org.exoplatform.portlet.exomvc.exception.* ;
import org.exoplatform.portlets.exomvc.decorator.PageBorderDecorator ;
import org.exoplatform.portlets.exomvc.decorator.ToolbarDecorator ;

public class ConfigureScript implements Configure {
  
  public void configure(Configuration config) {
    ExceptionHandler defaultExceptionHandler = new DefaultExceptionHandler() ;
    //create a chain of decorator for the pages
    pageDecorator = new ToolbarDecorator().
                        addPageDecorator(new PageBorderDecorator())
    /****************************************************************************
     *            C O N F I G U R E    V I E W    M O D E                       *
     ****************************************************************************/
    config.addViewModePage(
      new GroovyPageConfig().
      setGroovyScript("org/exoplatform/portlets/exomvc/ListScript.groovy").
      setPageName("groovy.list.script.page").
      setDescription("list the available scripts").
      addExceptionHandler(defaultExceptionHandler) 
    ) ;

    config.addViewModePage(
      new GroovyPageConfig().
      setGroovyScript("org/exoplatform/portlets/exomvc/ViewScript.groovy").
      setPageName("groovy.view.script.page").
      setPageDecorator(pageDecorator).
      setDescription("show the script to the user").
      addExceptionHandler(defaultExceptionHandler) 
    ) ;

    config.addViewModePage(
      new GroovyPageConfig().
      setGroovyScript("org/exoplatform/portlets/exomvc/EditScript.groovy").
      setPageName("groovy.edit.script.page").
      setPageDecorator(pageDecorator).
      setDescription("edit script form").
      addExceptionHandler(defaultExceptionHandler) 
    ) ;

    config.addViewModePage(
      new GroovyPageConfig().
      setGroovyScript("org/exoplatform/portlets/exomvc/NewUser.groovy").
      setPageName("groovy.new.user.page").
      setPageDecorator(pageDecorator).
      setDescription("new user form").
      addExceptionHandler(defaultExceptionHandler) 
    ) ;

    //----------------------------list user pages-----------------------------------
    config.addViewModePage(
      new POJOPageConfig().
      setClassName("org.exoplatform.portlets.exomvc.pojo.ListUserPage").
      setPageName("pojo.list.user.page").
      setDescription("list the users in the organization service, using plain old java object style").
      setPageDecorator(pageDecorator).
      addExceptionHandler(defaultExceptionHandler) 
    ) ;

    config.addViewModePage(
      new GroovyPageConfig().
      setGroovyScript("org/exoplatform/portlets/exomvc/ListUser.groovy").
      setPageName("groovy.list.user.page").
      setDescription("list the users in the organization service").
      setPageDecorator(pageDecorator).
      addExceptionHandler(defaultExceptionHandler) 
    ) ;

    config.addViewModePage(
      new JSPPageConfig().
      setPageClassName("org.exoplatform.portlets.exomvc.jsp.ListUserPage").
      setJSPPage("ListUserPage.jsp").
      setPageName("jsp.list.user.page").
      setDescription("list the users in the organization service, using jsp script").
      setPageDecorator(pageDecorator).
      addExceptionHandler(defaultExceptionHandler)
    ) ;

    config.addViewModePage(
      new VelocityPageConfig().
      setPageClassName("org.exoplatform.portlets.exomvc.velocity.ListUserPage").
      setTemplate("ListUserPage.vm").
      setPageName("velocity.list.user.page").
      setDescription("list the users in the organization service, using velocity script").
      setPageDecorator(pageDecorator).
      addExceptionHandler(defaultExceptionHandler)
    ) ;
    /****************************************************************************
     *            C O N F I G U R E    E D I T    M O D E                       *
     ****************************************************************************/
    /****************************************************************************
     *            C O N F I G U R E    H E L P    M O D E                       *
     ****************************************************************************/
    /****************************************************************************
     *            C O N F I G U R E    C O N F I G    M O D E                   *
     ****************************************************************************/
  }
}
