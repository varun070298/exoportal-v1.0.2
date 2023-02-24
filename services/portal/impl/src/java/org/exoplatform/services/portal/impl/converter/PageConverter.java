package org.exoplatform.services.portal.impl.converter;

import org.exoplatform.services.portal.model.*;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Tuan Nguyen
 */
public class PageConverter extends ContainerConverter {

	public boolean canConvert(Class type) {
		return type.equals(Page.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w,	
			                MarshallingContext context) {
		Page page = (Page) source ;
		writeBasicProperties(w, page)  ;
		writePageProperties(w, page)  ;
		writeChildren(page, w,  context) ;
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Page page = new Page();
		readBasicProperty(reader, page) ;
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if(readPageProperty(reader, page)) {
			} else {
				String nodeName = reader.getNodeName() ;
				readChildren(page , nodeName, context) ;
			}
			reader.moveUp();
		}
		return page ;
	}
	
	public void writePageProperties(HierarchicalStreamWriter w, Page page) {
		if(page.getOwner() != null) {
			w.startNode("owner"); w.setValue(page.getOwner()) ;w.endNode();
		}
		if(page.getName() != null) {
			w.startNode("name"); w.setValue(page.getName()) ;w.endNode();
		}
		if(page.getTitle() != null) {
			w.startNode("title"); w.setValue(page.getTitle()) ;w.endNode();
		}
		if(page.getIcon() != null) {
			w.startNode("icon"); w.setValue(page.getIcon()) ;w.endNode();
		}
		if(page.getState() != null) {
			w.startNode("state"); w.setValue(page.getState()) ;w.endNode();
		}
		if(page.getViewPermission() != null) {
			w.startNode("viewPermission"); w.setValue(page.getViewPermission()) ;w.endNode();
		}
		if(page.getEditPermission() != null) {
			w.startNode("editPermission"); w.setValue(page.getEditPermission()) ;w.endNode();
		}
	}
	
	public boolean readPageProperty(HierarchicalStreamReader reader, Page page) {
		String nodeName = reader.getNodeName() ;
		if("owner".equals(nodeName)) {
			page.setOwner(reader.getValue()) ; return true ;
		} else if("name".equals(nodeName)) {
			page.setName(reader.getValue()) ;	return true ;
		} else if("title".equals(nodeName)) {
			page.setTitle(reader.getValue()) ; return true ;
		} else if("icon".equals(nodeName)) {
			page.setIcon(reader.getValue()) ;	return true ;
		} else if("state".equals(nodeName)) {
			page.setState(reader.getValue()) ; return true ;
		} else if("viewPermission".equals(nodeName)) {
			page.setViewPermission(reader.getValue()) ;	return true ;
		} else if("editPermission".equals(nodeName)) {
			page.setEditPermission(reader.getValue()) ;	return true ;
		}
		return false ;
	}
}