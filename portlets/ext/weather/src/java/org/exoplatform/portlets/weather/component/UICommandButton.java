/**
 * Jul 16, 2004, 3:55:38 PM
 * @author: F. MORON
 * @email: francois.moron@rd.francetelecom.com
 * 
 * */

package org.exoplatform.portlets.weather.component;

import org.exoplatform.faces.core.component.UIExoCommand;

public class UICommandButton extends UIExoCommand {
	protected String	type_;
	protected String	name_;
	protected String	label_;

	public UICommandButton(String type, String name, String label) {
		type_ = type;
		name_ = name;
		label_ = label;
	   setId(name) ;
		setImmediate(true);
		setRendererType("CommandButtonRenderer");
	}
	
	public String getType() {
		return type_;
	}

	public String getName() {
		return name_;
	}

	public String getLabel() {
		return label_;
	}
}
