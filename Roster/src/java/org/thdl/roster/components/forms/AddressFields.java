package org.thdl.roster.components.forms;

import java.util.*;
import org.apache.torque.*;
import org.apache.tapestry.*;
import org.apache.tapestry.form.*;
import org.thdl.roster.*;
import org.thdl.roster.om.*;


public class AddressFields extends BaseComponent
{
//attributes
    private Address addressBean;
	 private IPropertySelectionModel countryModel;
//accessors

	public void setAddressBean(Address addressBean) {
		this.addressBean = addressBean;
	}

	public Address getAddressBean() throws org.apache.torque.TorqueException
	{
		return addressBean;
	}

	public void setCountryModel(IPropertySelectionModel countryModel) {
		this.countryModel = countryModel;
	}
	public IPropertySelectionModel getCountryModel() {
		if ( null == countryModel )
			setCountryModel( buildCountryModel() );
		return countryModel;
	}
//helpers
	public IPropertySelectionModel buildCountryModel()
	{
		try 
		{
			if ( ! Torque.isInit() )
			{
				Global global = (Global) getPage().getGlobal();
				Torque.init( global.getTorqueConfig() );
			}
			EntitySelectionModel countryModel = new EntitySelectionModel();
			LinkedList list = new LinkedList( CountryPeer.doSelectAll() );
			Country country;
			ListIterator looper = list.listIterator( 0 );
			while ( looper.hasNext() )
			{
				country = (Country) looper.next();
				countryModel.add( country.getId(), country.getCountry() );
			}
			return countryModel;
		}
		catch (Exception te) {
			throw new ApplicationRuntimeException( te.getMessage(), te );
		}
	}
//synthetic attribute accessors
}