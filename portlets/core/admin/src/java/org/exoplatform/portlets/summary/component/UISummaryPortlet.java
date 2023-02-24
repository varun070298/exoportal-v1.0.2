/*
 * Created on Jan 28, 2005
 */
package org.exoplatform.portlets.summary.component;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import org.exoplatform.commons.utils.ExpressionUtil;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.context.PortletExternalContext;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.faces.core.component.model.LabelCell;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.PortalResources;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portal.model.Node;

/**
 * @author benjaminmestrallet
 */
public class UISummaryPortlet extends UIPortlet{

  final private static String USER_REGISTRATION_PATH = "UISummaryPortlet.path.user-registration";
  final private static String USER_MANAGEMENT_PATH = "UISummaryPortlet.path.user-management";
  final private static String PORTAL_MANAGEMENT_PATH = "UISummaryPortlet.path.portal-management";
  final private static String PORTLET_REGISTRY_PATH = "UISummaryPortlet.path.portlet-registry";
  final private static String I18N_PATH = "UISummaryPortlet.path.i18n";
  final private static String BACKUP_PATH = "UISummaryPortlet.path.backup";
  final private static String INDEXING_PATH = "UISummaryPortlet.path.indexing";
  final private static String WSRP_PATH = "UISummaryPortlet.path.wsrp";
  final private static String JMX_PATH = "UISummaryPortlet.path.jmx";
  final private static String ADMIN_CONSOLE_PATH = "UISummaryPortlet.path.admin-console";
  final private static String FORUM_PATH = "UISummaryPortlet.path.forum";
  final private static String FILE_PATH = "UISummaryPortlet.path.file";
  final private static String WORKFLOW_PATH = "UISummaryPortlet.path.workflow";
  final private static String JCR_PATH = "UISummaryPortlet.path.jcr";
  
  final public static Parameter CHANGE_NODE = new Parameter(ACTION, "changeNode");  
  
  public UISummaryPortlet(ResourceBundle rB) throws Exception{
    setId("UISummaryPortlet");
    setRendererType("ChildrenRenderer");
    
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
    String ownerURI = rinfo.getOwnerURI() ;    
    
    ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
    Node rootNode_ = portal.getRootNode() ;    
    
    UIGrid grid = (UIGrid)addChild(UIGrid.class);
 
    Parameter nodeUriParam = new Parameter("uri", "");
    Parameter[] changeNodeParams = { CHANGE_NODE, nodeUriParam };
            
    Row row = new Row();
    
    row.add(new LabelCell(generateLink(ownerURI, USER_REGISTRATION_PATH, 
        "#{UISummaryPortlet.button.user-registration}")));
    row.add(new LabelCell(generateLink(ownerURI, USER_MANAGEMENT_PATH, 
        "#{UISummaryPortlet.button.user-management}")));
    row.add(new LabelCell(generateLink(ownerURI, PORTAL_MANAGEMENT_PATH, 
    "#{UISummaryPortlet.button.portal-management}")));    
    row.add(new LabelCell(generateLink(ownerURI, PORTLET_REGISTRY_PATH, 
        "#{UISummaryPortlet.button.portlet-registry}")));
    grid.add(row);
    
    row= new Row();
    row.add(new LabelCell(generateLink(ownerURI, I18N_PATH, 
        "#{UISummaryPortlet.button.i18n}")));            
    row.add(new LabelCell(generateLink(ownerURI, BACKUP_PATH, 
        "#{UISummaryPortlet.button.backup}")));
    row.add(new LabelCell(generateLink(ownerURI, INDEXING_PATH, 
        "#{UISummaryPortlet.button.indexing}")));
    row.add(new LabelCell(generateLink(ownerURI, WSRP_PATH, 
        "#{UISummaryPortlet.button.wsrp}")));
    grid.add(row);
    
    row= new Row();
    row.add(new LabelCell(generateLink(ownerURI, JMX_PATH, 
        "#{UISummaryPortlet.button.jmx}")));
    row.add(new LabelCell(generateLink(ownerURI, ADMIN_CONSOLE_PATH, 
        "#{UISummaryPortlet.button.admin-console}")));
    row.add(new LabelCell(generateLink(ownerURI, FORUM_PATH, 
        "#{UISummaryPortlet.button.forum}")));
    row.add(new LabelCell(generateLink(ownerURI, FILE_PATH, 
        "#{UISummaryPortlet.button.file}")));
    grid.add(row);
    
    row= new Row();
    row.add(new LabelCell(generateLink(ownerURI, WORKFLOW_PATH, 
        "#{UISummaryPortlet.button.workflow}")));
    row.add(new LabelCell(generateLink(ownerURI, JCR_PATH, 
        "#{UISummaryPortlet.button.jcr}")));
    grid.add(row);                    
    
  }
  
  private String generateLink(String ownerURI, String path, 
      String label){   
    PortletExternalContext econtext =  
      (PortletExternalContext) FacesContext.getCurrentInstance().getExternalContext();
    ResourceBundle res = econtext.getApplicationResourceBundle() ;
    StringBuffer sB = new StringBuffer();
    sB.append("<a href='"); sB.append(ownerURI); sB.append(res.getString(path)); sB.append("'>") ;
    sB.append(ExpressionUtil.getExpressionValue(res,label)) ;
    sB.append("</a>") ;    
    return sB.toString();    
  }
  
}
