package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.ProjectCodeAssignment;

public class DataProjectCodeAssignment implements Serializable
{
	private static final long		serialVersionUID	= -6261462029082150956L;
	public static final String []	projectFields		= new String [] { "WBSObjectId" };
	public static final String []	primaveraFields				= new String [] {
			"ProjectCodeObjectId", "ProjectCodeTypeName", "ProjectCodeValue",
			"ProjectCodeDescription"					};
	private Integer					typeId;
	private String					typeName;
	private String					value;
	private String					description;

	public DataProjectCodeAssignment (ProjectCodeAssignment pcaValue)
	{
		try
		{
			typeId = pcaValue.getProjectCodeTypeObjectId ().toInteger ();
			typeName = pcaValue.getProjectCodeTypeName ();
			value = pcaValue.getProjectCodeValue ();
			description = pcaValue.getProjectCodeDescription ();
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public Integer getTypeId ()
	{
		return typeId;
	}

	public String getTypeName ()
	{
		return typeName;
	}

	public String getValue ()
	{
		return value;
	}

	public String getDescription ()
	{
		return description;
	}
}
