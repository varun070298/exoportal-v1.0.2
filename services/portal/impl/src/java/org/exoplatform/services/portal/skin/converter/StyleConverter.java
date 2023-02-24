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
public class StyleConverter implements Converter {

	public boolean canConvert(Class type) {
		return type.equals(Style.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w, MarshallingContext context) {
		Style style = (Style) source ;
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
	  Style style = new Style(reader.getAttribute("name"), reader.getAttribute("url")) ;
		return style ;
	}
}