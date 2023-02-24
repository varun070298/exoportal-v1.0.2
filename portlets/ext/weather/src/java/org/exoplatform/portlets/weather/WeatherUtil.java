/*
 * Jun 29, 2004, 11:33:30 AM
 * @author: F. MORON
 * @email: francois.moron@rd.francetelecom.com
 * @version: WeatherConfigurator.java
 * 
 * */

package org.exoplatform.portlets.weather;

import java.util.HashMap;
import java.util.ResourceBundle;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import org.exoplatform.faces.core.Util;
import org.exoplatform.faces.core.component.UIPortlet;

import com.capeclear.www.GlobalWeather_wsdl.GlobalWeather_ServiceLocator;
import com.capeclear.www.GlobalWeather_wsdl.StationInfo;
import com.capeclear.www.GlobalWeather_xsd.Station;
import com.capeclear.www.GlobalWeather_xsd.WeatherReport;


public class WeatherUtil {

	private static WeatherUtil singleton_;
 	private GlobalWeather_ServiceLocator globalWeatherServiceLocator_;
	private boolean serviceInitialized_;
	private boolean serviceAvailable_;
	private ResourceBundle resources_;
	private String contextPath_;
	private HashMap weatherDataMap_;

	public WeatherUtil() {
		serviceAvailable_ = false;
		try {
			globalWeatherServiceLocator_ = new GlobalWeather_ServiceLocator();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			serviceInitialized_ = false;
		}
		serviceInitialized_ = true;

		if (serviceInitialized_) {
			String proxySet = null;
			ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext() ;
			resources_ =  Util.getApplicationResourceBundle()  ;
			try {
				proxySet = resources_.getString("proxySet");
			} catch (Exception ex) {
				ex.printStackTrace() ;
			}
			if (proxySet != null) {
				if (proxySet.equals("true")) {
					try {
						String proxyHost = resources_.getString("proxyHost");
						String proxyPort = resources_.getString("proxyPort");
						System.setProperty("http.proxySet", "true");
						System.setProperty("http.proxyHost", proxyHost);
						System.setProperty("http.proxyPort", proxyPort);
					} catch (Exception ex) {
						ex.printStackTrace() ;
					}
				}
			}
			PortletRequest request = (PortletRequest) eContext.getRequest();
			contextPath_ = request.getContextPath();
		}
	}
	
	public void checkRequest(WeatherData weatherData) throws Exception {
		StationInfo stationInfo;
		
		weatherData.setStationList(null);
		weatherData.setStationFound(false);
		weatherData.setNbStations(0);

		if (weatherData.getSearchStationCode() == null) weatherData.setSearchStationCode("");
		if (weatherData.getSearchStationName() == null) weatherData.setSearchStationName("");
//		if (weatherData.getPreferredStationCode() == null) weatherData.setPreferredStationCode("LFRN");
		if (weatherData.getDisplayedStationCode() == null) weatherData.setDisplayedStationCode("");
		
		if (weatherData.getDisplayedStationCode().equals("")) {
			if (weatherData.getSearchStationCode().equals("")) {
				if (weatherData.getSearchStationName().equals("")) 
					weatherData.setDisplayedStationCode(weatherData.getPreferredStationCode());
				else weatherData.setDisplayedStationCode("");
			}
			else weatherData.setDisplayedStationCode(weatherData.getSearchStationCode());
		}

		if (weatherData.getDisplayedStationCode().equals("") 
				&& weatherData.getSearchStationName().equals("")) return;

			try {
			stationInfo = globalWeatherServiceLocator_.getStationInfo();
		}
		catch (Exception ex) {
			throw ex;
		}
		serviceAvailable_ = true;
		weatherData.setStationFound(false);
		weatherData.setNbStations(0);
		if (!weatherData.getDisplayedStationCode().equals("")) {
			weatherData.setStationList(stationInfo.searchByCode(weatherData.getDisplayedStationCode())); 
			if (weatherData.getStationsList().length == 0) {
			}
			else if (weatherData.getStationsList().length == 1) {
				weatherData.setStationFound(true);
				weatherData.setNbStations(1);
			}
			else if (weatherData.getStationsList().length > 1) {
				weatherData.setStationFound(true);
				weatherData.setNbStations(weatherData.getStationsList().length);
			}
		}

		//	 Si pas de station trouv�e pour le code et un nom de ville � rechercher
		if (!weatherData.isStationFound() && !weatherData.getSearchStationName().equals("")) {
			Station [] stationsList = stationInfo.searchByName(weatherData.getSearchStationName());
			weatherData.setStationList(stationsList);
			if (stationsList.length == 0) {
				// Pas de station trouvee pour ce nom de ville
				weatherData.setStationFound(false);
				weatherData.setNbStations(0);
			}
			else if (stationsList.length == 1) {
				// Une station trouvee pour ce nom de ville
				weatherData.setStationFound(true);
				weatherData.setNbStations(1);
				weatherData.setDisplayedStationCode(stationsList[0].getIata());
				if (weatherData.getDisplayedStationCode() == null) 
					weatherData.setDisplayedStationCode(stationsList[0].getIcao());
			}
			else if (stationsList.length > 1) {
				// Plusieurs stations trouvees pour ce nom de ville
				weatherData.setStationFound(true);
				weatherData.setNbStations(stationsList.length);
			}
		}
	}

	public String getContextPath() {
		return contextPath_;
	}

	public GlobalWeather_ServiceLocator getGlobalWeatherServiceLocator() {
		return globalWeatherServiceLocator_;
	}

	public ResourceBundle getResources() {
		return resources_;
	}

	public WeatherReport getWeatherReport(String displayedStationCode) throws Exception{
		if (displayedStationCode == null) return null;
		WeatherReport weatherReport = null;
		try {
			weatherReport = globalWeatherServiceLocator_.getGlobalWeather().getWeatherReport(displayedStationCode);
		}
		catch (Exception ex) {
			throw ex;
		}
		return weatherReport;
	}
	
	public boolean isServiceAvailable() {
		return serviceAvailable_;
	}

	public boolean isServiceInitialized() {
		return serviceInitialized_;
	}

	public void setServiceAvailable(boolean serviceAvailable) {
		this.serviceAvailable_ = serviceAvailable;
	}
	
	public static WeatherUtil getInstance(UIPortlet uiPortlet) {
		if (singleton_ == null) {
			synchronized (WeatherUtil.class) {
				singleton_ = new WeatherUtil();
			}
		}
		return singleton_;
	}
}
