package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Resource;

public class DataResource implements Serializable
{
	private static final long		serialVersionUID	= -8682876838356142210L;
	public static final String []	primaveraFields				= new String [] {
			"Name", "ObjectId", "ResourceType"			};

	private String					resourceName;
	private Integer					resourceId;

	public DataResource (Resource resource)
	{
		try
		{
			resourceName = resource.getName ();
			resourceId = resource.getObjectId ().toInteger ();
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	@Override
	public String toString ()
	{
		return resourceName;
	}

	@Override
	public boolean equals (Object obj)
	{
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof DataResource))
			return false;

		DataResource other = (DataResource) obj;
		return resourceId.equals (other.getResourceId ());
	}

	public String getResourceName ()
	{
		return resourceName;
	}

	public Integer getResourceId ()
	{
		return resourceId;
	}

}
