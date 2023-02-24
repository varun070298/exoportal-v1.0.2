/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.rss.component;


import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.cache.*;
/**
 * Sat, Jan 03, 2004 @ 11:16
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: UIRss.java,v 1.10 2004/09/21 20:16:22 tuan08 Exp $
 */
public class UIRss extends UIExoCommand {
  private static long TIME_TO_LIVE = 1000 * 60 * 30;

  private String rssURL_;
  private int itemToShow_;
  private Channel channel_;
  private ExoCache cache_ ;

  public UIRss(CacheService cservice) throws Exception {
    FacesContext context = FacesContext.getCurrentInstance();
    PortletRequest request = (PortletRequest) context.getExternalContext().getRequest();
    rssURL_ = request.getPreferences().getValue("url", "/exo-news.xml");
    String tmp = request.getPreferences().getValue("item-to-show", "5");
    cache_ = cservice.getCacheInstance(getClass().getName()) ;
    channel_ = loadChannel(rssURL_);
    try {
      itemToShow_ = Integer.parseInt(tmp);      
    } catch (Exception ex) {
      itemToShow_ = 5;
    }
    setId("UIRss");
    setRendererType("RssRenderer");

    addActionListener(UpdateActionListener.class, UPDATE_ACTION);
  }

  public String getRssURL() {
    return rssURL_;
  }

  public int getItemToShow() {
    return itemToShow_;
  }

  public Channel getChannel() {
    return channel_;
  }

  private Channel loadChannel(String url) throws Exception {
    Channel channel = (Channel) cache_.get(url);
    long currentTime = System.currentTimeMillis();
    if (channel != null &&
        currentTime < (channel.getUpdateTime() + TIME_TO_LIVE)) {
      cache_.remove(url);
      channel = null;
    }

    if (channel == null) {
      synchronized (cache_) {
        String realUrl = null;
        if (url.startsWith("http:") || url.startsWith("file:")) {
          realUrl = url;
        } else {
          FacesContext context = FacesContext.getCurrentInstance();
          ExternalContext econtext = context.getExternalContext();
          realUrl = econtext.getResource(url).toString();
        }
        channel = Channel.parse(realUrl);
        cache_.put(url, channel);
      }
    }
    return channel;
  }
  
  public static class UpdateActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIRss uiRss = (UIRss) event.getComponent();
      uiRss.cache_.remove(uiRss.rssURL_);
      uiRss.channel_ = uiRss.loadChannel(uiRss.rssURL_);
    }
  }
}