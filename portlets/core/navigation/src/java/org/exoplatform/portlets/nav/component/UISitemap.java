package org.exoplatform.portlets.nav.component;

import java.util.ResourceBundle; 
/**
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2004/08/05 13:11:13 $
 * @author Fahrid Djebbari
 * 
 */

public class UISitemap extends UINavigation
{
	public UISitemap(ResourceBundle res) throws Exception 
	{
		super(res) ;
		setId("UISiteMap");
		setRendererType("SitemapRenderer");
	}
	
	public String getFamily( ) 
	{
		return "org.exoplatform.portlets.nav.component.UISitemap"; 
	}  

}
