package org.exoplatform.services.portal.impl.converter;

import org.exoplatform.services.portal.model.*;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Tuan Nguyen
 */
abstract public class ComponentConverter implements Converter {

	public void writeBasicProperties(HierarchicalStreamWriter w, Component comp) {
		if(comp.getId() != null) w.addAttribute("id", comp.getId()) ;
		if(comp.getRenderer() != null) w.addAttribute("renderer", comp.getRenderer()) ;
		if(comp.getDecorator() != null)  w.addAttribute("decorator", comp.getDecorator()) ;
		if(comp.getWidth() != null)  w.addAttribute("width", comp.getWidth()) ;
		if(comp.getHeight() != null)   w.addAttribute("height", comp.getHeight()) ;
	}
	
	public void readBasicProperty(HierarchicalStreamReader reader, Component comp) {
		comp.setId(reader.getAttribute("id")) ;
		comp.setRenderer(reader.getAttribute("renderer")) ;
		comp.setDecorator(reader.getAttribute("decorator")) ;
		comp.setWidth(reader.getAttribute("width")) ;
		comp.setHeight(reader.getAttribute("height")) ;
	}
}