package org.thdl.lex.component;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;


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

}

