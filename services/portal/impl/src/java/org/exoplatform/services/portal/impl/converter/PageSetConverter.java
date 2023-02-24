package org.exoplatform.services.portal.impl.converter;

import java.util.* ;
import org.exoplatform.services.portal.model.*;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Tuan Nguyen
 */
public class PageSetConverter extends ComponentConverter {

	public boolean canConvert(Class type) {
		return type.equals(PageSet.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w,	
			                MarshallingContext context) {
		PageSet pageSet = (PageSet) source ;
		List pages = pageSet.getPages() ;
		for(int i = 0; i < pages.size(); i++) {
			Object o = pages.get(i) ;
			context.convertAnother(o) ;
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		PageSet pageSet = new PageSet();
		List pages = pageSet.getPages() ;
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			Object comp = context.convertAnother(pageSet, Page.class) ;
			pages.add(comp) ;
			reader.moveUp();
		}
		return pageSet ;
	}
}