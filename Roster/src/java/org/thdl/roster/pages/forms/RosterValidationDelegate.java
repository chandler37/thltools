package org.thdl.roster.pages.forms;



import org.apache.tapestry.IMarkupWriter;

import org.apache.tapestry.IRequestCycle;



import org.apache.tapestry.form.IFormComponent;

import org.apache.tapestry.valid.ValidationDelegate;



/**

 *

 *  @author Howard Lewis Ship

 *  @version $Id$

 *  @since 1.0.6

 *

 **/



public class RosterValidationDelegate extends ValidationDelegate

{

    public void writeAttributes(IMarkupWriter writer, IRequestCycle cycle) 

    {

        if (isInError())

            writer.attribute("class", "field-error");

    }



    public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle)

    {

        if (isInError())

        {

            writer.print(" ");

            writer.beginEmpty("img");

            writer.attribute("src", "images/workbench/Warning-small.gif");

            writer.attribute("height", 20);

            writer.attribute("width", 20);

        }

    }



    public void writeLabelPrefix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)

    {

        if (isInError(component))

        {

            writer.begin("span");

            writer.attribute("class", "label-error");

        }

    }



    public void writeLabelSuffix(IFormComponent component, IMarkupWriter writer, IRequestCycle cycle)

    {

        if (isInError(component))

            writer.end(); // <span>

    }

}