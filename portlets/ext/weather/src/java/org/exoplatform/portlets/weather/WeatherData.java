/**
 * Jul 20, 2004, 2:52:59 PM
 * @author: F. MORON
 * @email: francois.moron@rd.francetelecom.com
 * 
 * */

package org.exoplatform.portlets.weather;

import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import com.capeclear.www.GlobalWeather_xsd.Station;

public class WeatherData {
	private PortletPreferences preferences_;
	private String preferredStationCode_;
	private String displayedStationCode_;
	private String searchStationCode_;
	private String searchStationName_;
	private Station [] stationsList_;
	private boolean stationFound_;
	private int nbStations_;
	
	public WeatherData() {
		PortletRequest request = 
			(PortletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		preferences_ = request.getPreferences();
	}
	
	public String getDisplayedStationCode() {
		return displayedStationCode_;
	}

	public PortletPreferences getPreferences() {
		return preferences_;
	}

	public String getPreferredStationCode() {
		return preferences_.getValue("code","LFRN");
//		preferredStationCode_ = preferences_.getValue("code","LFRN");
//		return preferredStationCode_;
	}
	
	public String getSearchStationCode() {
		return searchStationCode_;
	}

	public String getSearchStationName() {
		return searchStationName_;
	}

	public int getNbStations() {
		return nbStations_;
	}

	public Station[] getStationsList() {
		return stationsList_;
	}

	public boolean isStationFound() {
		return stationFound_;
	}
	
	public void setDisplayedStationCode(String displayedStationCode) {
		displayedStationCode_ = displayedStationCode;
	}

	public void setNbStations(int nbStations) {
		nbStations_ = nbStations;
	}

	public void setPreferredStationCode(String preferredStationCode) {
		preferredStationCode_ = preferredStationCode;
	}

	public void setSearchStationCode(String searchStationCode) {
		searchStationCode_ = searchStationCode;
	}

	public void setStationFound(boolean stationFound) {
		stationFound_ = stationFound;
	}

	public void setSearchStationName(String searchStationName) {
		searchStationName_ = searchStationName;
	}

	public void setStationList(Station [] stationsList) {
		stationsList_ = stationsList;
	}
}
