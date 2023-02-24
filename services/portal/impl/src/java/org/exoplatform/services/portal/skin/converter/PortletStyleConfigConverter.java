package org.exoplatform.services.portal.skin.converter;

import java.util.* ;
import org.exoplatform.services.portal.skin.model.*;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Tuan Nguyen
 */
public class PortletStyleConfigConverter implements Converter {

	public boolean canConvert(Class type) {
		return type.equals(PortletStyleConfig.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w, MarshallingContext context) {
	  PortletStyleConfig decorator = (PortletStyleConfig) source ;
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
	  PortletStyleConfig psconfig = new PortletStyleConfig() ;
	  while (reader.hasMoreChildren()) {
			reader.moveDown();
			String nodeName = reader.getNodeName() ;
			if("portlet-name".equals(nodeName)) {
				psconfig.setPortletName(reader.getValue()) ;
			} else {
				Object o = context.convertAnother(psconfig, Style.class) ;
				psconfig.getStyles().add(o) ;
			}
			reader.moveUp();
		}
		return psconfig ;
	}
}