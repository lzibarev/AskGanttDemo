/**
 * @file DataDocumentStatusCode.java
 * @author Sergey S. Bulanov <maxF212@gmail.com>
 * @copyright 2012 Intellect-Partner, JSC
 * @version 0.1.0
 */
package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.DocumentStatusCode;

/**
 * Статус документа
 */
public class DataDocumentStatusCode implements Serializable
{
	private static final long		serialVersionUID	= 8392423613277342757L;
	public static final String []	primaveraAPIfields	= new String [] {
			"ObjectId", "Name"							};

	private Integer					id					= 0;
	private String					name				= null;

	public DataDocumentStatusCode (DocumentStatusCode dsc)
	{
		try
		{
			id = dsc.getObjectId ().toInteger ();
			name = dsc.getName ();
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public DataDocumentStatusCode (Integer documentStatusCodeId,
			String documentStatusCodeName)
	{
		id = documentStatusCodeId;
		name = documentStatusCodeName;
	}

	public Integer getId ()
	{
		return id;
	}

	public void setId (Integer id)
	{
		this.id = id;
	}

	public String getName ()
	{
		return name;
	}

	public void setName (String name)
	{
		this.name = name;
	}

	@Override
	public String toString ()
	{
		return name;
	}

	@Override
	public boolean equals (Object obj)
	{
		if (!(obj instanceof DataDocumentStatusCode))
			return false;

		return id == ((DataDocumentStatusCode) obj).id && name.equals (((DataDocumentStatusCode) obj).name);
	}
}
