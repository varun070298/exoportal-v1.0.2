package org.exoplatform.services.portal.impl.converter;

import java.util.* ;
import org.exoplatform.services.portletcontainer.pci.model.Preference;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Tuan Nguyen
 */
public class PreferenceConverter extends ComponentConverter {

	public boolean canConvert(Class type) {
		return type.equals(Preference.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w,
			                MarshallingContext context) {
		Preference pref = (Preference) source ;
		w.	startNode("name"); w.setValue(pref.getName()) ; w.endNode() ;
		List values = pref.getValues() ;
		for(int i = 0; i < values.size(); i++) {
			String value = (String)values.get(i) ;
		  if(value != null) {
		    w.startNode("value"); w.setValue(value) ; w.endNode() ;
		  }	
		}
		if(pref.isReadOnly()) {
			w.startNode("read-only"); w.setValue("true") ; w.endNode() ;
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Preference pref = new Preference() ;
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			String nodeName = reader.getNodeName() ;
			if("name".equals(nodeName)) {
				pref.setName(reader.getValue());
			} else if("value".equals(nodeName)) {
			  String value  = reader.getValue() ;
				pref.addValue(value) ;
			} else if("read-only".equals(nodeName)) {
				pref.setReadOnly("true".equals(reader.getValue())) ;
			}
			reader.moveUp();
		}
		return pref;
	}
}