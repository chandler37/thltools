package org.thdl.roster.components;

import org.apache.tapestry.*;
import org.thdl.roster.om.Country;

public class CountryDisplay extends BaseComponent
{
//attributes
    private Country countryBean;
//accessors
	public void setCountryBean(Country countryBean) {
		this.countryBean = countryBean;
	}
	public Country getCountryBean() {
		return countryBean;
	}
//synthetic attribute accessors
//helper
}