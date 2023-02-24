package org.exoplatform.portlets.nav.component;

import org.exoplatform.faces.core.component.UIExoComponentBase;


/**
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2004/08/05 13:11:13 $
 * @author Fahrid Djebbari
 * 
 */

public class UINavigationEdit extends UIExoComponentBase  
{	
	public static String COMPONENT_ID = "navigation-edit";
	public static String RENDERER_TYPE = "NavigationEditRenderer";
	
	public UINavigationEdit()
	{
		this.setId(COMPONENT_ID);
		this.setRendererType(RENDERER_TYPE);
	}	
}
