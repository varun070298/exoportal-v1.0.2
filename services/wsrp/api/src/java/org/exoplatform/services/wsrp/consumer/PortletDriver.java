package org.exoplatform.services.wsrp.consumer;

import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.type.*;

/**
 * The portlet driver is a wrapper for all action which can be performed on an
 * portlet. There is one portlet driver for all instances of an portlet.
 *
 * @author Benjamin Mestrallet
 */
public interface PortletDriver {

  /**
   * Get the portlet this driver is bound to.
   *
   * @return The enity
   */
  public WSRPPortlet getPortlet();

  /**
   * This method is used to retrieve the markup generated by the portlet instance.
   *
   * @return The markup response generated by portlet
   */
  public MarkupResponse getMarkup(WSRPMarkupRequest markupRequest,
                                  UserSessionMgr userSession,
                                  String path)
      throws WSRPException;

  /**
   * This method is used to perform a blocking interaction on the portlet instance.
   */
  public BlockingInteractionResponse performBlockingInteraction(InteractionRequest actionRequest,
                                                                UserSessionMgr userSession,
                                                                String path)
      throws WSRPException;

  /**
   * Clone the portlet
   *
   * @return The new portlet context
   */
  public PortletContext clonePortlet(UserSessionMgr userSession) throws WSRPException;

  /**
   *
   **/
  public void initCookie() throws WSRPException;

  /**
   * Destroy the producer portlets specified in the entiyHandles array.
   */
  public DestroyPortletsResponse destroyPortlets(String[] portletHandles,
                                                 UserSessionMgr userSession)
      throws WSRPException;

  /**
   * Inform the producer that the sessions specified in the sessionIDs array
   * will no longer be used by the consumer and can therefor be released.
   */
  public ReturnAny releaseSessions(String[] sessionIDs,
                                   UserSessionMgr userSession)
      throws WSRPException;


  public PortletDescriptionResponse getPortletDescription(UserSessionMgr userSession,
                                                          String[] desiredLocales)
      throws WSRPException;


  public PortletPropertyDescriptionResponse getPortletPropertyDescription(UserSessionMgr userSession)
      throws WSRPException;

  public PropertyList getPortletProperties(String[] names, UserSessionMgr userSession)
      throws WSRPException;

  public PortletContext setPortletProperties(PropertyList properties, UserSessionMgr userSession)
      throws WSRPException;
}
