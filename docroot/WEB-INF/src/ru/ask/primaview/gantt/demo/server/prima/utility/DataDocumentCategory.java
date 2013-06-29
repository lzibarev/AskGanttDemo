/**
 * @author Sergey S. Bulanov <maxF212@gmail.com>
 * @copyright 2012 Intellect-Partner, JSC
 * @version 0.1.0
 */
package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.DocumentCategory;

/**
 * Категория документа
 */
public class DataDocumentCategory implements Serializable
{
	private static final long		serialVersionUID	= 1666755456057927152L;
	public static final String []	primaveraAPIfields	= new String [] {
			"ObjectId", "Name"							};

	protected Integer				id					= 0;
	protected String				name				= null;

	public DataDocumentCategory (DocumentCategory dc)
	{
		try
		{
			id = dc.getObjectId ().toInteger ();
			name = dc.getName ();
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public DataDocumentCategory (Integer categoryId, String cateforyName)
	{
		id = categoryId;
		name = cateforyName;
	}

	public DataDocumentCategory (DataDocumentCategory ddc)
	{
		this.id = ddc.id;
		this.name = ddc.name;
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
		if (!(obj instanceof DataDocumentCategory))
			return false;

		return id == ((DataDocumentCategory) obj).id && name.equals (((DataDocumentCategory) obj).name);
	}
}