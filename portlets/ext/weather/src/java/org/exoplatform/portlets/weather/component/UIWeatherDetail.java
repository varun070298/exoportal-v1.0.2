/**
 * Jun 16, 2004, 11:43:17 AM
 * @author: Fran�ois MORON
 * @email: francois.moron@rd.francetelecom.com
 **/

package org.exoplatform.portlets.weather.component;

import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.portlets.weather.component.model.*;


public class UIWeatherDetail extends UIGrid
{
	private String stationDetail_;
	private String iconSrc_;
	private String temperature_;
	private String wind_;

	public UIWeatherDetail() {
		super();
		setId("UIWeatherDetail");
		setRendererType("GridRenderer");
		
		stationDetail_ = null;
		iconSrc_ = null;
		temperature_ = null;
		wind_ = null;
		updateTree();
	}

	public void setStationDetail(String pStationDetail) {
		stationDetail_ = pStationDetail;
		updateTree();
	}
	
	public void setTemperature(String pTemperature) {
		temperature_ = pTemperature;
		updateTree();
	}

	public void setIconSrc(String pIconSrc) {
		iconSrc_ = pIconSrc;
		updateTree();
	}

	public void setWind(String pWind) {
		wind_ = pWind;
		updateTree();
	}

	private void updateTree() {
		clear();
		if (stationDetail_!= null) {
			add(new Row().add(new LabelCell(stationDetail_)));
		}
		if (iconSrc_!= null) add(new Row().add(new ImageCell(iconSrc_,iconSrc_)));
		if (temperature_!= null) add(new Row().add(new LabelCell("T� : "+temperature_+" �C")));
		if (wind_!= null) add(new Row().add(new LabelCell("Vent : "+wind_)));
	}
}
