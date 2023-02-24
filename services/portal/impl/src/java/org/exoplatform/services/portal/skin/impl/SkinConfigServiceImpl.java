/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.skin.impl;

import java.util.*;
import java.io.InputStream;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.converter.*;
import org.exoplatform.services.portal.skin.model.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;


/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 * tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class SkinConfigServiceImpl implements SkinConfigService {
  static private XStream xstream_;

  private Map portalDecoratorMap_;
  private Map pageDecoratorMap_;
  private Map containerDecoratorMap_;
  private Map portletDecoratorMap_;
  private Map portletStyleConfigMap_;

  public SkinConfigServiceImpl() {
    portalDecoratorMap_ = new HashMap();
    pageDecoratorMap_ = new HashMap();
    containerDecoratorMap_ = new HashMap();
    portletDecoratorMap_ = new HashMap();
    portletStyleConfigMap_ = new HashMap();
  }

  public void addConfiguration(InputStream is) throws Exception {
    String xml = IOUtil.getStreamContentAsString(is);
    XStream xstream = getXStreamInstance();
    SkinConfig config = (SkinConfig) xstream.fromXML(xml);

    addDecorator(portalDecoratorMap_, config.getPortalDecorators());
    addDecorator(pageDecoratorMap_, config.getPageDecorators());
    addDecorator(containerDecoratorMap_, config.getContainerDecorators());
    addDecorator(portletDecoratorMap_, config.getPortletDecorators());

    List portletStyleConfigs = config.getPortletStyleConfig();
    if (portletStyleConfigs == null) return;
    for (int i = 0; i < portletStyleConfigs.size(); i++) {
      PortletStyleConfig psconfig = (PortletStyleConfig) portletStyleConfigs.get(i);
      addPortletStyle(portletStyleConfigMap_, psconfig);
    }
  }

  private void addPortletStyle(Map portletStyleConfigMap_, PortletStyleConfig psconfig) {
    if (portletStyleConfigMap_.containsKey(psconfig.getPortletName())) {
      PortletStyleConfig config = (PortletStyleConfig) portletStyleConfigMap_.get(psconfig.getPortletName());
      List styles2add = psconfig.getStyles();
      for (Iterator iterator = styles2add.iterator(); iterator.hasNext();) {
        Style style = (Style) iterator.next();
        config.addStyle(style);
      }
    } else {
      portletStyleConfigMap_.put(psconfig.getPortletName(), psconfig);
    }
  }

  public void addConfiguration(String fileUrl) throws Exception {

  }

  public Collection getPortalDecorators() {
    return portalDecoratorMap_.values();
  }

  public Decorator getPortalDecorator(String rendererType) {
    return getDecorator(portalDecoratorMap_, rendererType);
  }

  public Style getPortalDecoratorStyle(String rendererType, String styleName) {
    Decorator decorator = getPortalDecorator(rendererType);
    return searchStyle(decorator, styleName);
  }

  public Collection getPageDecorators() {
    return pageDecoratorMap_.values();
  }

  public Decorator getPageDecorator(String rendererType) {
    return getDecorator(pageDecoratorMap_, rendererType);
  }

  public Style getPageDecoratorStyle(String rendererType, String styleName) {
    Decorator decorator = getPageDecorator(rendererType);
    return searchStyle(decorator, styleName);
  }

  public Collection getContainerDecorators() {
    return containerDecoratorMap_.values();
  }

  public Decorator getContainerDecorator(String rendererType) {
    return getDecorator(containerDecoratorMap_, rendererType);
  }

  public Style getContainerDecoratorStyle(String rendererType, String styleName) {
    Decorator decorator = getContainerDecorator(rendererType);
    return searchStyle(decorator, styleName);
  }

  public Collection getPortletDecorators() {
    return portletDecoratorMap_.values();
  }

  public Decorator getPortletDecorator(String rendererType) {
    return getDecorator(portletDecoratorMap_, rendererType);
  }

  public Style getPortletDecoratorStyle(String rendererType, String styleName) {
    Decorator decorator = (Decorator) portletDecoratorMap_.get(rendererType);
    return searchStyle(decorator, styleName);
  }

  public List getPortletStyles(String portletName) {
    PortletStyleConfig config = (PortletStyleConfig) portletStyleConfigMap_.get(portletName);
    if (config == null) return null;
    return config.getStyles();
  }

  public Style getPortletStyle(String portletName, String styleName) {
    PortletStyleConfig config = (PortletStyleConfig) portletStyleConfigMap_.get(portletName);
    if (config == null) return null;
    List list = config.getStyles();
    for (int i = 0; i < list.size(); i++) {
      Style style = (Style) list.get(i);
      if (styleName.equals(style.getName())) return style;
    }
    return null;
  }

  private Decorator getDecorator(Map map, String rendererType) {
    if (rendererType == null || rendererType.length() == 0 || "default".equals(rendererType)) {
      return (Decorator) map.values().iterator().next();
    }
    return (Decorator) map.get(rendererType);
  }

  private Style searchStyle(Decorator decorator, String styleName) {
    if (decorator == null) return null;
    List list = decorator.getStyles();
    for (int i = 0; i < list.size(); i++) {
      Style style = (Style) list.get(i);
      if (styleName.equals(style.getName())) return style;
    }
    return null;
  }

  private void addDecorator(Map map, List decorators) {
    if (decorators == null) return;
    for (int i = 0; i < decorators.size(); i++) {
      Decorator decorator = (Decorator) decorators.get(i);
      String rendererType = decorator.getRendererType();
      if (map.containsKey(rendererType)) {
        Decorator deco2update = (Decorator) map.get(rendererType);
        List styles2add = decorator.getStyles();
        for (Iterator iterator = styles2add.iterator(); iterator.hasNext();) {
          Style style = (Style) iterator.next();
          deco2update.addStyle(style);
        }
      } else {
        map.put(rendererType, decorator);
      }
    }
  }

  public static XStream getXStreamInstance() {
    if (xstream_ == null) {
      xstream_ = new XStream(new XppDriver());
      xstream_.alias("skin-config", SkinConfig.class);
      xstream_.alias("decorator", Decorator.class);
      xstream_.alias("style", Decorator.class);
      xstream_.alias("portlet-style-config", PortletStyleConfig.class);
      xstream_.registerConverter(new SkinConfigConverter());
      xstream_.registerConverter(new DecoratorConverter());
      xstream_.registerConverter(new StyleConverter());
      xstream_.registerConverter(new PortletStyleConfigConverter());
    }
    return xstream_;
  }
}