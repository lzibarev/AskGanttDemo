package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.ProjectPortfolio;

public class DataProjectPortfolio implements Serializable
{
	private static final long		serialVersionUID	= 7851674465852089108L;
	public static final String []	primaveraFields		= new String [] {
			"ObjectId", "Name"							};

	private Integer					id					= 0;
	private String					name				= "";

	public DataProjectPortfolio (ProjectPortfolio p)
	{
		try
		{
			id = p.getObjectId ().toInteger ();
			name = p.getName ();
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public Integer getId ()
	{
		return id;
	}

	public String getName ()
	{
		return name;
	}
}
