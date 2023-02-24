package org.exoplatform.services.portal.impl.converter;

import org.exoplatform.services.portal.model.*;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Tuan Nguyen
 */
public class PortletConverter extends ComponentConverter {

	public boolean canConvert(Class type) {
		return type.equals(Portlet.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w,	
			                MarshallingContext context) {
		Portlet portlet = (Portlet) source ;
		writeBasicProperties(w, portlet)  ;
		writePortletProperties(w, portlet)  ;
		if(portlet.getPortletPreferences() != null) {
			w.startNode("portlet-preferences") ;
			context.convertAnother(portlet.getPortletPreferences()) ;
			w.endNode() ;
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Portlet portlet = new Portlet();
		readBasicProperty(reader, portlet) ;
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if(readPortletProperty(reader, portlet)) {
			} else {
				ExoPortletPreferences prefs = 
					(ExoPortletPreferences)context.convertAnother(portlet, ExoPortletPreferences.class) ;
				portlet.setPortletPreferences(prefs) ;
			}
			reader.moveUp();
		}
		return portlet ;
	}
	
	public void writePortletProperties(HierarchicalStreamWriter w, Portlet comp) {
		if(comp.getTitle() != null) {
			w.startNode("title"); w.setValue(comp.getTitle()) ;w.endNode();
		}
		if(comp.getWindowId() != null) {
			w.startNode("windowId"); w.setValue(comp.getWindowId()) ;w.endNode();
		}
		if(comp.getPortletStyle() != null) {
			w.startNode("portlet-style"); w.setValue(comp.getPortletStyle()) ;w.endNode();
		}
		if(comp.getShowInfoBar() == false) {
			w.startNode("showInfoBar"); w.setValue("false") ;w.endNode();
		}
		if(comp.getShowWindowState() == false) {
			w.startNode("showWindowState"); w.setValue("false") ;w.endNode();
		}
    if(comp.getShowPortletMode() == false) {
      w.startNode("showPortletMode"); w.setValue("false") ;w.endNode();
    }
	}
	
	public boolean readPortletProperty(HierarchicalStreamReader reader, Portlet comp) {
		String nodeName = reader.getNodeName() ;
		if("title".equals(nodeName)) {
			comp.setTitle(reader.getValue()) ;
			return true ;
		} else if("windowId".equals(nodeName)) {
			comp.setWindowId(reader.getValue()) ;
			return true ;
		} else if("portlet-style".equals(nodeName)) {
			comp.setPortletStyle(reader.getValue()) ;
			return true ;
		} else if("showInfoBar".equals(nodeName)) {
			comp.setShowInfoBar("true".equals(reader.getValue())) ;
			return true ;
		} else if("showWindowState".equals(nodeName)) {
			comp.setShowWindowState("true".equals(reader.getValue())) ;
			return true ;
    } else if("showPortletMode".equals(nodeName)) {
      comp.setShowPortletMode("true".equals(reader.getValue())) ;
      return true ;
		}
		return false ;
	}
}