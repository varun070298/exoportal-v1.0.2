/**
 * Jun 16, 2004, 11:43:17 AM
 * @author: Fran�ois MORON
 * @email: francois.moron@rd.francetelecom.com
 **/

package org.exoplatform.portlets.weather.component;

import javax.faces.context.FacesContext;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.container.client.http.HttpClientInfo;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.LabelCell;
import org.exoplatform.faces.core.component.model.Row;


public class UIWeatherTitle extends UIGrid
{
	private String title_;

	public UIWeatherTitle() {
		super();
		setId("UIWeatherTitle");
		HttpClientInfo client = 
      (HttpClientInfo)SessionContainer.getInstance().getMonitor().getClientInfo() ;
    String type = client.getClientType() ;
		if (type.equals(HttpClientInfo.MOBILE_BROWSER_TYPE)) {
			setRendererType("GridRenderer");
		} else  {
			setRendererType("GridRenderer");
		}

		updateTree();
	}

	public void setTitle(String pTitle) {
		title_ = pTitle;
		updateTree();
	}

	public void decode(FacesContext context) {
	}

	private void updateTree() {
		clear();
		add(new Row().add(new LabelCell(title_)));
	}
}
