/**
 * Wed, Jun 08, 2004 @ 17:03
 * @author: Fran�ois MORON
 * @email: francois.moron@rd.francetelecom.com
 **/

package org.exoplatform.portlets.weather.component;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.component.UIExoComponentBase;

import com.capeclear.www.GlobalWeather_xsd.Direction;
import com.capeclear.www.GlobalWeather_xsd.Layer;
import com.capeclear.www.GlobalWeather_xsd.Sky;
import com.capeclear.www.GlobalWeather_xsd.WeatherReport;
import com.capeclear.www.GlobalWeather_xsd.Wind;


public class UIWeatherView extends UIExoComponentBase
{
	private boolean stationFound_;
	private WeatherReport weatherReport_;
	private ResourceBundle resources_;
	private String contextPath_;

	private UIWeatherDetail uiWeatherDetail;
	private UIWeatherForm uiWeatherForm;
	
	public UIWeatherView() {
		super();
		setId("UIWeatherView");
		setRendererType("ChildrenRenderer");

		List children = getChildren();

		uiWeatherDetail = new UIWeatherDetail();
		uiWeatherForm = new UIWeatherForm();

		weatherReport_ = null;
		stationFound_ = false;
		resources_ = null;
		contextPath_ = null;
		updateTree();
		children.add(uiWeatherDetail);
		children.add(uiWeatherForm);
	}

	public String getSearchStationCode() {
		return uiWeatherForm.getSearchStationCode();
	}

	public String getSearchStationName() {
		return uiWeatherForm.getSearchStationName();
	}
	
	public UIWeatherForm getUIWeatherForm() {
		return uiWeatherForm;
	}
	
	public void setSearchStationCode(String pSearchStationCode) {
		uiWeatherForm.setSearchStationCode(pSearchStationCode);
	}

	public void setSearchStationName(String pSearchStationName) {
		uiWeatherForm.setSearchStationName(pSearchStationName);
	}

	public void setStationFound(boolean pStationFound) {
		stationFound_ = pStationFound;
		updateTree();
	}
	
	public void setWeatherReport(WeatherReport pWeatherReport) {
		weatherReport_ = pWeatherReport;
		updateTree();
	}
	
	public void setResources(ResourceBundle pResources) {
		resources_ = pResources;
		updateTree();
	}
	
	public void setContextPath(String pContextPath) {
		contextPath_ = pContextPath;
		updateTree();
	}

	public void decode(FacesContext context) {
	}

	private void updateTree() {
		if (weatherReport_ != null) {
			// station detail
			Calendar timeStamp = weatherReport_.getTimestamp();
			uiWeatherDetail.setStationDetail(timeStamp.getTime().toLocaleString());			
			// sky icon
			Sky sky = weatherReport_.getSky();
			if (sky != null) {
				String typeSky = null;
				String iconFile = null;
				Layer [] layers = sky.getLayers();
				if (layers != null) {
					if (layers.length > 0) {
						typeSky = layers[0].getType().getValue();
						if (typeSky != null)
						{
							if (typeSky.equals("CLOUD")) {
								int extent = layers[0].getExtent();
								if ((extent == 1) || (extent == 2) || (extent == 3) || (extent == 4)) {
									typeSky+="_"+String.valueOf(extent);
								}
							}
							try {
								iconFile = resources_.getString(typeSky+"_DAY_ICON");
							}
							catch(Exception ex) {
							}
							try {
								uiWeatherDetail.setIconSrc(contextPath_+resources_.getString("ICONS_SOURCE_PATH")+iconFile);
							}
							catch(Exception ex) {
							}
						}
					}
				}
			}
			// temperature
			uiWeatherDetail.setTemperature(String.valueOf(weatherReport_.getTemperature().getAmbient()));
			// wind
			Wind wind = weatherReport_.getWind();
			if (wind != null) {
				String stringWind = null;
				try {
					DecimalFormat df = new DecimalFormat("# ###.###");
					stringWind = df.format(wind.getPrevailing_speed()) + " m/s";
				} catch (Exception ex) {
				}
				Direction direction = wind.getPrevailing_direction();
				if (direction != null) {
					StringTokenizer strTk = new StringTokenizer(direction.getString(),"(");
					if (stringWind != null) stringWind += " (" + strTk.nextToken().toString().trim()+")";
					else stringWind = strTk.nextToken().toString();
				}
				uiWeatherDetail.setWind(stringWind);
			}
			uiWeatherDetail.setRendered(true);
		}
		else {
			uiWeatherDetail.setRendered(false);
		}
		uiWeatherForm.setRendered(true);
	}
}
