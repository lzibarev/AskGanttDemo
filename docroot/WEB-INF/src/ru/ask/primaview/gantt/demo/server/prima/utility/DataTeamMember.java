package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Resource;

public class DataTeamMember implements Serializable
{
	private static final long		serialVersionUID	= 8369269837555772914L;
	private static final String		tmfEmail			= "mail";
	private static final String		tmfPhoneWork		= "telephonenumber";
	private static final String		tmfPhoneWorkCity	= "homePhone";
	private static final String		tmfJobTitle			= "jobtitle";
	private static final String		tmfPhoneMobile		= "mobile";

	public static final String []	primaveraFields				= new String [] {
			"Name", "ObjectId"							};
	private Integer					resourceId			= 0;
	private String					tmImageFilename		= null;
	private String					resourceName		= null;
	private String					tmJobTitle			= null;
	private String					tmPhoneWork			= null;
	private String					tmPhoneMobile		= null;
	private String					tmEmail				= null;

	public DataTeamMember (Resource resource)
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

	public DataTeamMember (Resource resource, HashMap<String, String> tmInfo)
	{
		Logger logger = Logger.getRootLogger ();
		try
		{
			resourceName = resource.getName ();
			logger.debug ("resourceName " + resourceName);
			resourceId = resource.getObjectId ().toInteger ();
			if (tmInfo == null)
				return;

			if (tmInfo.containsKey (tmfPhoneWork))
			{
				tmPhoneWork = tmInfo.get (tmfPhoneWork);
				if (tmInfo.containsKey (tmfPhoneWorkCity))
					tmPhoneWork = new StringBuilder (tmPhoneWork).append (", ").append (tmInfo.get (tmfPhoneWorkCity)).toString ();
				logger.debug ("tmPhoneWork " + tmPhoneWork);
			}
			if (tmInfo.containsKey (tmfJobTitle))
			{
				tmJobTitle = tmInfo.get (tmfJobTitle);
				logger.debug ("tmJobTitle " + tmJobTitle);
			}
			if (tmInfo.containsKey (tmfPhoneMobile))
			{
				tmPhoneMobile = tmInfo.get (tmfPhoneMobile);
				logger.debug ("tmPhoneMobile " + tmPhoneMobile);
			}
			if (tmInfo.containsKey (tmfEmail))
			{
				tmEmail = tmInfo.get (tmfEmail);
				logger.debug ("tmEmail " + tmEmail);
			}
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.logger.fatal (this.getClass ().getName () + " create failed: " + e.getMessage ());
		}
		logger.debug ("DataTeamMember construct end");
	}

	public Integer getResourceId ()
	{
		return resourceId;
	}

	public void setResourceId (Integer resourceId)
	{
		this.resourceId = resourceId;
	}

	public String getTmImageFilename ()
	{
		return tmImageFilename;
	}

	public void setTmImageFilename (String tmImageFilename)
	{
		this.tmImageFilename = tmImageFilename;
	}

	public String getResourceName ()
	{
		return resourceName;
	}

	public void setResourceName (String resourceName)
	{
		this.resourceName = resourceName;
	}

	public String getTmJobTitle ()
	{
		return tmJobTitle;
	}

	public void setTmJobTitle (String tmJobTitle)
	{
		this.tmJobTitle = tmJobTitle;
	}

	public String getTmPhoneWork ()
	{
		return tmPhoneWork;
	}

	public void setTmPhoneWork (String tmPhoneWork)
	{
		this.tmPhoneWork = tmPhoneWork;
	}

	public String getTmPhoneMobile ()
	{
		return tmPhoneMobile;
	}

	public void setTmPhoneMobile (String tmPhoneMobile)
	{
		this.tmPhoneMobile = tmPhoneMobile;
	}

	public String getTmEmail ()
	{
		return tmEmail;
	}

	public void setTmEmail (String tmEmail)
	{
		this.tmEmail = tmEmail;
	}
}
