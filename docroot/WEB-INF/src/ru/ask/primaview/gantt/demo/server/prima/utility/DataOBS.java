package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.OBS;

public class DataOBS implements Serializable
{
	private static final long		serialVersionUID	= 835696778989706468L;
	public static final String []	primaveraFields		= new String [] {
			"Name", "ObjectId", "ParentObjectId"		};

	private Integer					id;
	private Integer					parentId;
	private Integer					level				= 0;
	private String					name;

	public DataOBS (OBS obs)
	{
		try
		{
			ObjectId objectId = obs.getObjectId ();
			id = objectId.toInteger ();
			parentId = obs.getParentObjectId () != null ? obs.getParentObjectId ().toInteger ()
					: 0;
			name = obs.getName ();
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public DataOBS (OBS obs, Integer level)
	{
		try
		{
			id = obs.getObjectId ().toInteger ();
			parentId = obs.getParentObjectId () != null ? obs.getParentObjectId ().toInteger ()
					: 0;
			name = obs.getName ();
			this.level = level;
		}
		catch (BusinessObjectException e)
		{
			e.printStackTrace ();
		}
	}

	@Override
	public String toString ()
	{
		StringBuilder result = new StringBuilder ();
		if (level > 0)
			for (int i = 0; i < level; i++)
				result.append ("_");

		return result.append (name).toString ();
	}

	@Override
	public boolean equals (Object obj)
	{
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof DataOBS))
			return false;

		DataOBS other = (DataOBS) obj;
		return id.equals (other.getId ());
	}

	public Integer getId ()
	{
		return id;
	}

	public String getName ()
	{
		return name;
	}

	public void setId (Integer id)
	{
		this.id = id;
	}

	public void setName (String name)
	{
		this.name = name;
	}

	public Integer getParentId ()
	{
		return parentId;
	}

	public void setParentId (Integer parentId)
	{
		this.parentId = parentId;
	}
}
