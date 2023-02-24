/**
 * Wed, Jun 08, 2004 @ 17:03
 * @author: Fran�ois MORON
 * @email: francois.moron@rd.francetelecom.com
 **/

package org.exoplatform.portlets.weather.component;

import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.FormButton;
import org.exoplatform.faces.core.component.model.LabelCell;
import org.exoplatform.faces.core.component.model.ListComponentCell;
import org.exoplatform.faces.core.component.model.Row;

public class UIWeatherForm extends UISimpleForm
{
	private UIStringInput uiSearchStationCode_;
	private UIStringInput uiSearchStationName_;

	public UIWeatherForm() {
		super("weatherForm", "post", null);
		setId("UIWeatherForm");
		
		uiSearchStationCode_ = new UIStringInput("code","");
		uiSearchStationName_ = new UIStringInput("ville","");
		
		add(new Row().
			add(new LabelCell("Code : ")).
		   add(new ComponentCell(this, uiSearchStationCode_)));
		add(new Row().
		   add(new LabelCell("Ville : ")).
		   add(new ComponentCell(this, uiSearchStationName_)));
		
		add(new Row().
			   add(new ListComponentCell().
			   add(new FormButton("Search","WeatherFormSearch")).
				addColspan("2").addAlign("center")));
	}

	public String getSearchStationCode() {
		return uiSearchStationCode_.getValue();
	}

	public String getSearchStationName() {
		return uiSearchStationName_.getValue();
	}

	public void setSearchStationCode(String pSearchStationCode) {
		uiSearchStationCode_.setValue(pSearchStationCode);
	}

	public void setSearchStationName(String pSearchStationName) {
		uiSearchStationName_.setValue(pSearchStationName);
	}
}