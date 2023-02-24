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
public class ContainerConverter extends ComponentConverter {

	public boolean canConvert(Class type) {
		return type.equals(Container.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w,	
			                MarshallingContext context) {
		Container container = (Container) source ;
		writeBasicProperties(w, container)  ;
    String title = container.getTitle() ;
    if(title != null && title.length() > 0) {
      w.startNode("title"); w.setValue(container.getTitle()) ;w.endNode();
    }
		writeChildren(container, w, context) ;
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Container container = new Container();
		readBasicProperty(reader, container) ;
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			String nodeName = reader.getNodeName() ;
			if("title".equals(nodeName)) {
			  container.setTitle(reader.getValue())  ;
      } else {
			  readChildren(container , nodeName, context) ;
      }
			reader.moveUp();
		}
		return container ;
	}
	
	final protected void writeChildren(Container container,HierarchicalStreamWriter w ,
			                               MarshallingContext context) {
		List children = container.getChildren();
		for(int i = 0; i < children.size(); i++) {
			Object o = children.get(i) ;
			if(o instanceof Body) w.startNode("body") ;
			else if (o instanceof Portlet) w.startNode("portlet") ;
			else  w.startNode("container") ;
			context.convertAnother(o) ;
			w.endNode() ;
		}
	}
	
	final protected void readChildren(Container container , String nodeName,
			                              UnmarshallingContext context) {
		Object comp = null ; 
		if("portlet".equals(nodeName)) {
			comp = context.convertAnother(container, Portlet.class) ;
		} else if("container".equals(nodeName)) {
			comp = context.convertAnother(container, Container.class) ;
		} else  {
			comp = context.convertAnother(container, Body.class) ;
		}
	  container.getChildren().add(comp) ;
	}
}