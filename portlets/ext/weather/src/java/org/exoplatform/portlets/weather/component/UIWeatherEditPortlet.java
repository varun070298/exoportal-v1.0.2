/**
 * Tue, Jun 29, 2004 @ 10:55
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

import javax.faces.context.FacesContext;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.weather.WeatherData;
import org.exoplatform.portlets.weather.WeatherUtil;


public class UIWeatherEditPortlet extends UIPortlet {
	
	private WeatherUtil weatherUtil_;
	private WeatherData	weatherData_;
 	private boolean storeUserPref_;
	boolean editOK_;

 	private UIWeatherTitle uiWeatherTitle;
 	private UIWeatherForm uiWeatherForm;
 	private UISelectStationForm uiSelectStationForm;
 	
	public UIWeatherEditPortlet() {
		weatherData_ = new WeatherData();
		weatherUtil_ = WeatherUtil.getInstance(this);
		storeUserPref_ = false;

	   setId("UIWeatherEditPortlet") ;
		setRendererType("ChildrenRenderer");
		
		List children = getChildren();
		
		uiWeatherTitle = new UIWeatherTitle(); 
		uiWeatherTitle.setId("UIWeatherEditPortletTitle");
		uiWeatherTitle.setRendered(true);
		
		uiWeatherForm = new UIWeatherForm();
		uiWeatherForm.setRendered(true);
		uiSelectStationForm = new UISelectStationForm();
		uiSelectStationForm.setRendered(false);

		process();
		children.add(uiWeatherTitle);
		children.add(uiWeatherForm);
		children.add(uiSelectStationForm);

		//FormActionExceptionHandler unknownException = 
		//	new FormActionExceptionHandler("Error ActionListener portlet weather (edit mode)");
		uiWeatherForm.addActionListener(new SearchStationListener().
		      setActionToListen("WeatherFormSearch")) ;
		uiSelectStationForm.addActionListener(new SelectStationListener().
				setActionToListen("SelectStationFormOk")) ;
	}

	private void process() {
		String code;
		
		if (!weatherUtil_.isServiceInitialized()) {
			uiWeatherTitle.setTitle("GlobalWeather Service can not be initialized");
			uiWeatherTitle.setRendered(true);
			uiWeatherForm.setRendered(false);
			uiSelectStationForm.setRendered(false);
		}
		else {
			try {
				weatherUtil_.checkRequest(weatherData_);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				weatherUtil_.setServiceAvailable(false);
			}
			if (!weatherUtil_.isServiceAvailable()) {
				uiWeatherTitle.setTitle("Service is not available");
				uiWeatherTitle.setRendered(true);
				uiWeatherForm.setRendered(false);
				uiSelectStationForm.setRendered(false);
			}
			else {
				if (weatherData_.isStationFound()) {
					if (weatherData_.getNbStations() == 1) {
						uiWeatherTitle.setTitle("Weather Portlet Preferences ("+
								weatherData_.getStationsList()[0].getName()+")");
						if (storeUserPref_) {
							try {
								weatherData_.getPreferences().setValue("code",weatherData_.getDisplayedStationCode().toUpperCase());
								weatherData_.getPreferences().store();
								editOK_ = true;
							}
							catch (Exception ex) {
								storeUserPref_ = false;
								ex.printStackTrace();
								uiWeatherTitle.setTitle("Weather Portlet Preferences : Erreur durant la sauvegarde");
							}
							if (editOK_) {
								uiWeatherForm.setSearchStationCode(weatherData_.getPreferredStationCode());
								uiWeatherForm.setSearchStationName("");
								// GO TO VIEW MODE
								ActionResponse response = (ActionResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
								try {
									response.setPortletMode(PortletMode.VIEW);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						else {
							uiWeatherForm.setSearchStationCode(weatherData_.getPreferredStationCode());
							uiWeatherForm.setSearchStationName("");
						}
						uiWeatherTitle.setRendered(true);
						uiWeatherForm.setRendered(true);
						uiSelectStationForm.setRendered(false);
					} else if (weatherData_.getNbStations() > 1) {
						uiWeatherTitle.setTitle("S�lection d'une station");
						uiWeatherTitle.setRendered(true);
						uiWeatherForm.setRendered(false);
						uiSelectStationForm.setStationsList(weatherData_.getStationsList());
						uiSelectStationForm.setRendered(true);
					}
				} 
				else {
					uiWeatherTitle.setTitle("Weather Portlet Preferences : Station inconnue !");
					storeUserPref_ = false;
					uiWeatherTitle.setRendered(true);
					uiWeatherForm.setRendered(true);
					uiSelectStationForm.setRendered(false);
				}
			}
		}
	}
	
	public class SearchStationListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			editOK_ = false;
			storeUserPref_ = true;
			weatherData_.setSearchStationCode(uiWeatherForm.getSearchStationCode());
			weatherData_.setSearchStationName(uiWeatherForm.getSearchStationName());
			weatherData_.setDisplayedStationCode("");
			process();
		}
	}
  
	public class SelectStationListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			editOK_ = false;
			storeUserPref_ = true;
			weatherData_.setSearchStationCode("");
			weatherData_.setSearchStationName("");
			weatherData_.setDisplayedStationCode(uiSelectStationForm.getSelectStationCode());
			process();
		}
	}
}

