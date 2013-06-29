package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.ActivityStep;

public class DataActivityStep implements Serializable
{
	private static final long		serialVersionUID	= -487111083505004565L;
	public static final String []	primaveraFields		= new String [] {
			"ObjectId", "Name", "PercentComplete", "Description" };

	private String					name;
	private Integer					percent;
	private String					description;
	private Integer					id;

	public DataActivityStep (ActivityStep as)
	{
		try
		{
			id = as.getObjectId ().toInteger ();
			name = as.getName ();
			percent = (int) Math.round (as.getPercentComplete ().doubleValue () * 100D);
			description = as.getDescription ();
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}

	}

	public String getName ()
	{
		return name;
	}

	public Integer getPercent ()
	{
		return percent;
	}

	public String getDescription ()
	{
		return description;
	}

	public Integer getId ()
	{
		return id;
	}
}
