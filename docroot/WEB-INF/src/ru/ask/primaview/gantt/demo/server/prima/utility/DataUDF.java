package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

import ru.ask.primaview.gantt.demo.server.prima.PrimaveraServiceUtils;

import com.primavera.ServerException;
import com.primavera.common.value.BeginDate;
import com.primavera.common.value.EndDate;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.UDFType;
import com.primavera.integration.client.bo.object.UDFValue;
import com.primavera.integration.network.NetworkException;

public class DataUDF implements Serializable
{
	private static final long		serialVersionUID	= 2318385438376288842L;

	// private static SimpleDateFormat _format = null; // формат времени
	public static final String []	primaveraFields		= new String [] {
			"UDFTypeTitle", "CodeValue", "Description", "Text", "Double",
			"Integer", "FinishDate", "StartDate", "UDFTypeDataType" };
	public static final String		doNotShowPattern	= ".*not for the monitor.*";

	private String					typeTitle;
	private String					codeValue;
	private String					description;
	private String					text;
	private String					dataType;
	private String					fieldName;

	public DataUDF (UDFValue value)
	{
		try
		{
			typeTitle = value.getUDFTypeTitle ();

			codeValue = value.getCodeValue ();
			dataType = value.getUDFTypeDataType ().toString ();
			description = value.getDescription ();
			text = value.getText ();
			if (dataType.equals ("Double"))
			{
				text = String.valueOf (value.getDouble ());
			}
			else if (dataType.equals ("Integer"))
			{
				text = String.valueOf (value.getInteger ());
			}
			else if (dataType.equals ("Finish Date"))
			{
				EndDate date = value.getFinishDate ();
				text = PrimaveraServiceUtils.dateFormatDDdMMdYYYY (date);
			}
			if (dataType.equals ("Start Date"))
			{
				BeginDate date = value.getStartDate ();
				text = PrimaveraServiceUtils.dateFormatDDdMMdYYYY (date);
			}
			UDFType type = value.loadUDFType (new String [0]);
			fieldName = type.getUDFFieldName ();
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
		catch (ServerException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
		catch (NetworkException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public String getTypeTitle ()
	{
		return typeTitle;
	}

	public String getCodeValue ()
	{
		return codeValue;
	}

	public String getDescription ()
	{
		return description;
	}

	public String getText ()
	{
		return text;
	}

	public String getDataType ()
	{
		return dataType;
	}

	public String getFieldName ()
	{
		return fieldName;
	}

}
