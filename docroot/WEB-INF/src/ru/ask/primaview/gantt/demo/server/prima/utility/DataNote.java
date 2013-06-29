/**
 * @author Sergey S. Bulanov <maxF212@gmail.com>
 * @copyright 2012 Intellect-Partner, JSC
 * @version 0.1.0
 */
package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.ActivityNote;
import com.primavera.integration.client.bo.object.ProjectNote;

public class DataNote implements Serializable
{
	private static final long		serialVersionUID	= -8740667804515730201L;
	public static final Integer		NOTE_TYPE_ACTIVITY	= 1;
	public static final Integer		NOTE_TYPE_PROJECT	= 2;

	public static final String []	primaveraFields			= new String [] {
			"NotebookTopicObjectId", "NotebookTopicName", "Note" };

	protected String				topicName;
	protected String				note;
	protected Integer				topicId;
	protected Integer				type;
	protected Integer				parentId;										// id проекта или работы

	public DataNote (ProjectNote value)
	{
		try
		{
			topicName = value.getNotebookTopicName ();
			topicId = value.getNotebookTopicObjectId ().toInteger ();
			note = value.getNote ();
			parentId = value.getProjectObjectId ().toInteger ();
			type = NOTE_TYPE_PROJECT;
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public DataNote (ActivityNote value)
	{
		try
		{
			topicName = value.getNotebookTopicName ();
			topicId = value.getNotebookTopicObjectId ().toInteger ().intValue ();
			note = value.getNote ();
			parentId = value.getActivityObjectId ().toInteger ();
			type = NOTE_TYPE_ACTIVITY;
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.logger.fatal (this.getClass ().getName () + " create failed: " + e.getMessage ());
		}
	}

	public String getTopicName ()
	{
		return topicName;
	}

	public String getNote ()
	{
		return note;
	}

	public int getTopicId ()
	{
		return topicId;
	}

	public int getType ()
	{
		return type;
	}

	public int getParentId ()
	{
		return parentId;
	}

}
