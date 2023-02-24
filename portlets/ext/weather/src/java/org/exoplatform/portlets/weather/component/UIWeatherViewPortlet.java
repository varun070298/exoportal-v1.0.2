/**
 * Wed, Jun 08, 2004 @ 17:03
 * @author: Fran�ois MORON
 * @email: francois.moron@rd.francetelecom.com
 **/

/* IMPORTANT 
 * This portlet use a properties file (/WEB-INF/classes/WeatherResource.properties).
 * Check proxy properties and set it according to your network configuration. 
 * Generaly "proxySet" must be "false" (no use of proxy server)
 */

package org.exoplatform.portlets.weather.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.weather.WeatherData;
import org.exoplatform.portlets.weather.WeatherUtil;

import com.capeclear.www.GlobalWeather_xsd.Station;


public class UIWeatherViewPortlet extends UIPortlet {
	
	private WeatherUtil weatherUtil_;
	private WeatherData	weatherData_;

 	private UIWeatherTitle uiWeatherTitle;
 	private UIWeatherView uiWeatherView;
 	private UISelectStationForm uiSelectStationForm;
 	
	public UIWeatherViewPortlet() {
		weatherData_ = new WeatherData();
		weatherUtil_ = WeatherUtil.getInstance(this);
		
	   setId("UIWeatherViewPortlet") ;
		setRendererType("ChildrenRenderer");
		
		List children = getChildren();
		
		uiWeatherTitle = new UIWeatherTitle(); 
		uiWeatherTitle.setId("UIWeatherViewPortletTitle");
		uiWeatherTitle.setRendered(true);
		uiWeatherView = new UIWeatherView();
		uiWeatherView.setRendered(true);
		uiSelectStationForm = new UISelectStationForm();
		uiSelectStationForm.setRendered(false);
		

		if (weatherUtil_.isServiceInitialized()) {
			uiWeatherView.setContextPath(weatherUtil_.getContextPath());
			uiWeatherView.setResources(weatherUtil_.getResources());
		}
		process();
		children.add(uiWeatherTitle);
		children.add(uiWeatherView);
		children.add(uiSelectStationForm);

		//FormActionExceptionHandler unknownException = 
		//	new FormActionExceptionHandler("Error ActionListener portlet weather (view mode)");
		uiWeatherView.getUIWeatherForm().
				addActionListener(new SearchStationListener().
		      setActionToListen("WeatherFormSearch")) ;
		uiSelectStationForm.addActionListener(new SelectStationListener().
				setActionToListen("SelectStationFormOk")) ;
	}

	private void process() {
		String code;
		
		if (!weatherUtil_.isServiceInitialized()) {
			uiWeatherTitle.setTitle("GlobalWeather Service can not be initialized");
			uiWeatherTitle.setRendered(true);
			uiWeatherView.setRendered(false);
			uiSelectStationForm.setRendered(false);
		}
		else {
			try {
				weatherUtil_.checkRequest(weatherData_);
			}
			catch (Exception ex) {
				ex.printStackTrace() ;
				weatherUtil_.setServiceAvailable(false);
			}
			if (!weatherUtil_.isServiceAvailable()) {
				uiWeatherTitle.setTitle("Service is not available");
				uiWeatherTitle.setRendered(true);
				uiWeatherView.setRendered(false);
				uiSelectStationForm.setRendered(false);
			}
			else {
				uiWeatherView.setStationFound(weatherData_.isStationFound());
				if (weatherData_.isStationFound()) {
					if (weatherData_.getNbStations() == 1) {
						Station station = weatherData_.getStationsList()[0];
						uiWeatherTitle.setTitle(station.getName()+" ("+station.getCountry()+")");
						try {
							uiWeatherView.setWeatherReport(weatherUtil_.getWeatherReport(weatherData_.getDisplayedStationCode()));
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						uiWeatherTitle.setRendered(true);
						uiWeatherView.setRendered(true);
						uiSelectStationForm.setRendered(false);
					} else if (weatherData_.getNbStations() > 1) {
						uiWeatherTitle.setTitle("S�lection d'une station");
						uiWeatherTitle.setRendered(true);
						uiWeatherView.setRendered(false);
						uiSelectStationForm.setStationsList(weatherData_.getStationsList());
						uiSelectStationForm.setRendered(true);
					}
				} 
				else {
					uiWeatherTitle.setTitle("Station inconnue");
					uiWeatherView.setWeatherReport(null);
					uiWeatherTitle.setRendered(true);
					uiWeatherView.setRendered(true);
					uiSelectStationForm.setRendered(false);
				}
			}
		}
	}
	
	public class SearchStationListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			weatherData_.setSearchStationCode(uiWeatherView.getSearchStationCode());
			uiWeatherView.setSearchStationCode("");
			weatherData_.setSearchStationName(uiWeatherView.getSearchStationName());
			uiWeatherView.setSearchStationName("");
			weatherData_.setDisplayedStationCode("");
			process();
		}
	}
  
	public class SelectStationListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			weatherData_.setSearchStationCode("");
			weatherData_.setSearchStationName("");
			weatherData_.setDisplayedStationCode(uiSelectStationForm.getSelectStationCode());
			process();
		}
	}
}

