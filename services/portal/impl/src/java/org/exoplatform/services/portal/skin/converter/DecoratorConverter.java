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
public class DecoratorConverter implements Converter {

	public boolean canConvert(Class type) {
		return type.equals(Decorator.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w, MarshallingContext context) {
		Decorator decorator = (Decorator) source ;
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
	  Decorator decorator = new Decorator() ;
	  while (reader.hasMoreChildren()) {
			reader.moveDown();
			String nodeName = reader.getNodeName() ;
			if("renderer-type".equals(nodeName)) {
				decorator.setRendererType(reader.getValue()) ;
			} else {
				Object o = context.convertAnother(decorator, Style.class) ;
				decorator.getStyles().add(o) ;
			}
			reader.moveUp();
		}
		return decorator ;
	}
}