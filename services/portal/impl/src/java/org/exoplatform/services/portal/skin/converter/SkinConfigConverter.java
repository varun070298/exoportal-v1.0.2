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
public class SkinConfigConverter implements Converter {

	public boolean canConvert(Class type) {
		return type.equals(SkinConfig.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w, MarshallingContext context) {
		SkinConfig config = (SkinConfig) source ;
		throw new Error("Not support marshall yet") ;
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
	  SkinConfig config = new SkinConfig() ;
	  while (reader.hasMoreChildren()) {
			reader.moveDown();
			String nodeName = reader.getNodeName() ;
			if("portal-decorators".equals(nodeName)) {
				config.setPortalDecorators((List)context.convertAnother(config, List.class)) ;
			} else	if("page-decorators".equals(nodeName)) {
			  config.setPageDecorators((List)context.convertAnother(config, List.class)) ;
			} else	if("container-decorators".equals(nodeName)) {
			  config.setContainerDecorators((List)context.convertAnother(config, List.class)) ;
			} else	if("portlet-decorators".equals(nodeName)) {
			  config.setPortletDecorators((List)context.convertAnother(config, List.class)) ;
			} else {
				Object o = context.convertAnother(config, PortletStyleConfig.class) ;
				config.getPortletStyleConfig().add(o) ;
			}
			reader.moveUp();
		}
		return config ;
	}
}