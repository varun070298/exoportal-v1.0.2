package org.exoplatform.services.portal.impl.converter;

import java.util.* ;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Preference;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author Tuan Nguyen
 */
public class ExoPortletPreferencesConverter extends ComponentConverter {

	public boolean canConvert(Class type) {
		return type.equals(ExoPortletPreferences.class) ;
	}

	public void marshal(Object source, HierarchicalStreamWriter w,
			                MarshallingContext context) {
		ExoPortletPreferences prefs = (ExoPortletPreferences) source ;
		Iterator i = prefs.values().iterator() ;
		while(i.hasNext()) {
			w.startNode("preference") ;
			Preference pref = (Preference) i.next() ;
			context.convertAnother(pref) ;
			w.endNode() ;
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		ExoPortletPreferences prefs = new ExoPortletPreferences() ;
		while (reader.hasMoreChildren()) {
			reader.moveDown() ;
			Preference pref = (Preference) context.convertAnother(prefs, Preference.class) ;
			reader.moveUp();
			prefs.put(pref.getName(), pref) ;
		}
		return prefs ;
	}
}