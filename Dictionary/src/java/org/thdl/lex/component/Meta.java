package org.thdl.lex.component;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.thdl.lex.*;


/**
 * @author     Hibernate CodeGenerator
 * @created    October 10, 2003
 */
public class Meta extends BaseMeta implements Serializable
{

	/**
	 *  Description of the Method
	 *
	 * @param  properties                 Description of the Parameter
	 * @exception  LexComponentException  Description of the Exception
	 */
	public void populate( Map properties ) throws LexComponentException
	{
		try
		{
			BeanUtils.populate( this, properties );
		}
		catch ( IllegalAccessException iae )
		{
			throw new LexComponentException( iae );
		}
		catch ( java.lang.reflect.InvocationTargetException ite )
		{
			throw new LexComponentException( ite );
		}

	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public String toString()
	{
		return new ToStringBuilder( this )
				.toString();
	}



	/**
	 * default constructor
	 */
	public Meta() { }


	/**
	 *Constructor for the Meta object
	 *
	 * @param  user         Description of the Parameter
	 * @param  preferences  Description of the Parameter
	 */
	public Meta( LexUser user, Preferences preferences )
	{
		setCreatedBy( user.getId() );
		setModifiedBy( user.getId() );
		setCreatedByProjSub( preferences.getProjectSubject() );
		setModifiedByProjSub( preferences.getProjectSubject() );
		setSource( preferences.getSource() );//default source is Self from Lex.Sources
		setLanguage( preferences.getLanguage() );
		setScript( preferences.getScript() );
		setDialect( preferences.getDialect() );
		setNote( preferences.getNote() );
	}

}

