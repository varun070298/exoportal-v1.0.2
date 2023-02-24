/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.search.component;

import org.apache.lucene.document.Document;
import org.exoplatform.commons.utils.HtmlUtil;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.indexing.IndexerPlugin;
import org.exoplatform.services.indexing.IndexingService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIViewDocument.java,v 1.2 2004/11/03 01:20:37 tuan08 Exp $
 */
public class UIViewDocument extends UIExoCommand {
  final static public String COMPONENT_FAMILY = "org.exoplatform.faces.search.component.UIViewDocument" ;
  public final static String TEXT_FORMAT = "TEXT_FORMAT" ;
  public final static String HTML_FORMAT = "HTML_FORMAT" ;
  public final static String XML_FORMAT = "XML_FORMAT" ;
  public final static String FORMAT_TYPE = "format" ;
  public final static String CHANGE_FORMAT_ACTION = "changeFormat" ;
 
  public static Parameter[] htmlFormatParams_ = { 
      new Parameter(ACTION, CHANGE_FORMAT_ACTION),
      new Parameter(FORMAT_TYPE, HTML_FORMAT) 
  };
  public static Parameter[] xmlFormatParams_ = { 
      new Parameter(ACTION, CHANGE_FORMAT_ACTION),
      new Parameter(FORMAT_TYPE, XML_FORMAT) 
  };
  public static Parameter[] textFormatParams_ = { 
      new Parameter(ACTION, CHANGE_FORMAT_ACTION),
      new Parameter(FORMAT_TYPE, TEXT_FORMAT) 
  };
  public static Parameter[] cancelParams_ = { 
      new Parameter(ACTION, CANCEL_ACTION) 
  };  

  private String text_ ;
  private Document document_ ;
  private IndexingService iservice_ ;
  
  public UIViewDocument(IndexingService iservice) {
  	setId("UIViewDocument") ;
    setClazz("UIViewDocument");
    setRendererType("ViewDocumentRenderer") ;
    iservice_ = iservice ;
    addActionListener(ChangeFormatActionListener.class, CHANGE_FORMAT_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  }
  
  public String getDocument() { return text_ ; }
  
  public void setDocument(Document doc) throws Exception {
    document_ = doc ;
    String module = doc.getField(IndexingService.MODULE_FIELD).stringValue() ;
    IndexerPlugin plugin = iservice_.getIndexerPlugin(module) ;
    text_  =  plugin.getObjectAsXHTML(null, doc.getField(IndexingService.IDENTIFIER_FIELD).stringValue());
  }
  
  public String getFamily() {  return COMPONENT_FAMILY  ; }
  
  static public class ChangeFormatActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIViewDocument uiView =  (UIViewDocument) event.getSource();
      String format = event.getParameter(FORMAT_TYPE) ;
      String module = uiView.document_.getField(IndexingService.MODULE_FIELD).stringValue() ;
      IndexerPlugin plugin = uiView.iservice_.getIndexerPlugin(module) ;
      String id = uiView.document_.getField(IndexingService.IDENTIFIER_FIELD).stringValue() ;
      if(HTML_FORMAT.equals(format)) {
        uiView.text_  =    plugin.getObjectAsXHTML(null, id);
      } else if(XML_FORMAT.equals(format)) {
        uiView.text_  =   HtmlUtil.showXmlTags(plugin.getObjectAsXML(null, id));
      } else {
        uiView.text_  =  "<pre>" + plugin.getObjectAsText(null, id) + "</pre>";
      }
    }
  } 

  static public class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIViewDocument uiView =  (UIViewDocument) event.getSource();
      uiView.setRenderedSibling(UISearchSummary.class) ;      
    }
  } 
}