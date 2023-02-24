package org.exoplatform.services.portal.impl.converter;

import java.util.List;
import org.exoplatform.services.portal.model.PageNode;
import org.exoplatform.services.portal.model.PageReference;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Tuan Nguyen
 */
public class NodeConfigConverter implements Converter {
	
	public boolean canConvert(Class type) {
		return type.equals(PageNode.class) ;
	}
	
	public void marshal(Object source, HierarchicalStreamWriter w, MarshallingContext context) {
		PageNode node = (PageNode) source ;
		writeProperties(w, node)  ;
		List children = node.getChildren() ;
		if(children != null && children.size() > 0) {
			for(int i = 0; i < children.size(); i++) {
				w.startNode("node") ;
				context.convertAnother(children.get(i)) ;
				w.endNode() ;
			}
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		PageNode node = new PageNode();
		List children = node.getChildren() ;
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if(readProperty(reader, node)) {
			} else {
				Object o = context.convertAnother(node, PageNode.class) ;
				children.add(o) ;
			}
			reader.moveUp();
		}
		return node ;
	}
	
	public void writeProperties(HierarchicalStreamWriter w, PageNode node) {
		if(node.getUri() != null) {
			w.startNode("uri"); w.setValue(node.getUri()) ;w.endNode();
		}
		if(node.getName() != null) {
			w.startNode("name"); w.setValue(node.getName()) ;w.endNode();
		}
		if(node.getLabel() != null) {
			w.startNode("label"); w.setValue(node.getLabel()) ;w.endNode();
		}
    if(node.getViewPermission() != null) {
      w.startNode("viewPermission"); w.setValue(node.getViewPermission()) ;w.endNode();
    }
    if(node.getEditPermission() != null) {
      w.startNode("editPermission"); w.setValue(node.getEditPermission()) ;w.endNode();
    }
		if(node.getIcon() != null) {
			w.startNode("icon"); w.setValue(node.getIcon()) ;w.endNode();
		}
		List list = node.getPageReference() ;
		for(int i = 0; i < list.size(); i++) {
		  PageReference pref = (PageReference) list.get(i) ;
		  w.startNode("pageReference");
		  if(!pref.isVisible()) w.addAttribute("visible", "false") ;
		  w.addAttribute("type", pref.getType()) ;
		  w.  setValue(pref.getPageReference()) ;
		  w.endNode();
		}
		if(node.getDescription() != null) {
			w.startNode("description"); w.setValue(node.getDescription()) ;w.endNode();
		}
	}
	
	public boolean readProperty(HierarchicalStreamReader reader, PageNode node) {
		String nodeName = reader.getNodeName() ;
		if("uri".equals(nodeName)) {
			node.setUri(reader.getValue()) ;
			return true ;
		} else if("name".equals(nodeName)) {
			node.setName(reader.getValue()) ;
			return true ;
		} else if("label".equals(nodeName)) {
			node.setLabel(reader.getValue()) ;
			return true ;
		} else if("icon".equals(nodeName)) {
			node.setIcon(reader.getValue()) ;	return true ;
    } else if("viewPermission".equals(nodeName)) {
      node.setViewPermission(reader.getValue()) ; return true ;
    } else if("editPermission".equals(nodeName)) {
      node.setEditPermission(reader.getValue()) ; return true ;
		} else if("pageReference".equals(reader.getNodeName())) {
		  PageReference pref = new PageReference() ;
		  if("false".equals(reader.getAttribute("visible"))) pref.setVisible(false) ;
		  String type = reader.getAttribute("type") ; 
		  if(type != null) pref.setType(type) ;
		  pref.setPageReference(reader.getValue()) ;
			node.getPageReference().add(pref) ;
			return true ;
		} else if("description".equals(nodeName)) {
			node.setDescription(reader.getValue()) ;
			return true ;
		}
		return false ;
	}
}