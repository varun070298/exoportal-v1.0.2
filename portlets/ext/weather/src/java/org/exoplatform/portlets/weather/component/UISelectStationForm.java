/**
 * Wed, Jun 18, 2004 @ 10:12
 * @author: Fran�ois MORON
 * @email: francois.moron@rd.francetelecom.com
 **/

package org.exoplatform.portlets.weather.component;

import java.util.ArrayList;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;


import com.capeclear.www.GlobalWeather_xsd.*;

public class UISelectStationForm extends UISimpleForm
{
	private String selectStationCode_;
	private Station [] stationsList_;
	private UISelectBox uiSelectBox_; 
	
	public UISelectStationForm() {
		super("selectStationForm", "post", null);
		setId("UISelectStationForm");

		uiSelectBox_ = new UISelectBox("UISelectBox","",null);
		updateTree();
	}
	
	public String getSelectStationCode() {
		if (uiSelectBox_ != null) return uiSelectBox_.getValue();
		else return null;
	}

	public void setStationsList(Station [] pStationsList) {
		stationsList_ = pStationsList;
		updateTree();
	}
	
	private void updateTree() {
		clear();
		if (stationsList_ != null) {
			ArrayList liste = new ArrayList();
			for (int i = 0; i < stationsList_.length; i++)
			{
				String nomStation = stationsList_[i].getName();
				String codeStation = stationsList_[i].getIcao();
				if (codeStation == null) codeStation = stationsList_[i].getIata(); 
				String paysStation = stationsList_[i].getCountry();
				liste.add(new SelectItem(nomStation+" ("+paysStation+")",codeStation));
			}
			String defaultCode = stationsList_[0].getIcao();
			if (defaultCode == null) defaultCode = stationsList_[0].getIata();
			uiSelectBox_.setValue(defaultCode);
			uiSelectBox_.setOptions(liste);
			add(new Row().
				   add(new ComponentCell(this, uiSelectBox_)));
		}
		
		add(new Row().
			   add(new ListComponentCell().
			   add(new FormButton("OK","SelectStationFormOk")).
				addColspan("2").addAlign("center")));
	}
}
