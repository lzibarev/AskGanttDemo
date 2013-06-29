package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;
import java.util.Date;

import com.primavera.ServerException;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Activity;
import com.primavera.integration.network.NetworkException;

/**
 * Обертка для хранения данных по работе
 */
public class DataActivity implements Serializable
{
	private static final long		serialVersionUID					= 6112467853756030083L;
	public static final String []	primaveraFields						= new String [] {
			"Id", "Type", "TotalFloat", "ObjectId", "Name", "ActualStartDate",
			"ActualFinishDate", "PercentComplete", "EarlyFinishDate",
			"BaselineStartDate", "BaselineFinishDate", "PrimaryResourceName",
			"PrimaryResourceObjectId", "PercentCompleteType",
			"ProjectObjectId", "ProjectName", "DataDate"				};

	public static final String		ACTIVITY_TYPE_TASK_DEPENDENT		= "Task Dependent";
	public static final String		ACTIVITY_TYPE_RESOURCE_DEPENDENT	= "Resource Dependent";
	public static final String		ACTIVITY_TYPE_LEVEL_OF_EFFORT		= "Level of Effort";
	public static final String		ACTIVITY_TYPE_START_MILESTONE		= "Start Milestone";
	public static final String		ACTIVITY_TYPE_FINISH_MILESTONE		= "Finish Milestone";
	public static final String		ACTIVITY_TYPE_WBS_SUMMARY			= "WBS Summary";

	/**
	 * Id задачи
	 */
	private Integer					id;

	/**
	 * Текстовое представление типа работы (ActivityType)
	 * 'Task Dependent', 'Resource Dependent', 'Level of Effort', 'Start Milestone', 'Finish Milestone', or 'WBS Summary'
	 */
	private String					type;

	/**
	 * Время, на которое работа может быть отложена, без задержки финальной даты проекта.
	 * Ед.измерения: единица времени, назначенная в проекте.
	 */
	private Integer					totalFloat;

	/**
	 * Короткий ID для идентификации работы внутри проекта
	 */
	private String					code;

	/**
	 * Наименование задачи
	 */
	private String					name;

	/**
	 * Процент выполнения работы
	 */
	private Integer					completePercent;

	/**
	 * Gets the activity percent complete type: 'Physical', 'Duration', or 'Units'.
	 */
	private String					completeType;

	/**
	 * Дата фактического старта работы
	 */
	private Date					actualStart;
	/**
	 * Дата фактического финиша работы
	 */
	private Date					actualFinish;

	/**
	 * Самая ранняя возможная дата завершения работы. Вычисляется планировщиком проекта на основании ограничений расписания, доступности ресурсов, сетевой
	 * логики.
	 */
	private Date					earlyFinish;

	/**
	 * Gets the current start date of the activity in the project baseline. Set to the planned start date until the activity is started, then set to the actual
	 * start date. This field may not be included in the where clause or order by clause specified when loading business objects.
	 */
	private Date					bsStart;

	/**
	 * Gets the current finish date of the activity in the project baseline. Set to the activity planned finish date while the activity is not started, the
	 * remaining finish date while the activity is in progress, and the actual finish date once the activity is completed. This field may not be included in the
	 * where clause or order by clause specified when loading business objects.
	 */
	private Date					bsFinish;
	/**
	 * Наименование связанного ресурса
	 */
	private String					resourceName;
	/**
	 * ID связанного ресурса
	 */
	private Integer					resourceId;

	private Integer					project;
	private String					projectName;
	private String					projectCode;
	/**
	 * Gets the current data date for the project. The project status is up to date as of the data date.
	 */
	private Date					currentDate;

	private boolean					hasNotes;

	public DataActivity (Activity a)
	{
		try
		{
			id = a.getObjectId ().toInteger ();
			type = a.getType ().toString ();
			totalFloat = a.getTotalFloat () != null ? a.getTotalFloat ().intValue ()
					: null;
			code = a.getId ();
			name = a.getName ();

			completePercent = (int) Math.round (a.getPercentComplete ().doubleValue () * 100D);
			completeType = a.getPercentCompleteType ().getValue ();

			earlyFinish = a.getEarlyFinishDate ();
			actualStart = a.getActualStartDate ();
			actualFinish = a.getActualFinishDate ();
			bsStart = a.getBaselineStartDate ();
			bsFinish = a.getBaselineFinishDate ();

			resourceName = a.getPrimaryResourceName ();

			resourceId = (a.getPrimaryResourceObjectId () != null) ? a.getPrimaryResourceObjectId ().toInteger ()
					: null;

			project = a.getProjectObjectId ().toInteger ();
			projectName = a.getProjectName ();

			currentDate = a.getDataDate ();

			hasNotes = a.loadActivityNotes (new String [] { "NotebookTopicObjectId" }, null, null).hasNext ();
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

	public Integer getId ()
	{
		return id;
	}

	public String getType ()
	{
		return type;
	}

	public Integer getTotalFloat ()
	{
		return totalFloat;
	}

	public String getCode ()
	{
		return code;
	}

	public String getName ()
	{
		return name;
	}

	public Integer getCompletePercent ()
	{
		return completePercent;
	}

	public String getCompleteType ()
	{
		return completeType;
	}

	public Date getActualStart ()
	{
		return actualStart;
	}

	public Date getActualFinish ()
	{
		return actualFinish;
	}

	public Date getEarlyFinish ()
	{
		return earlyFinish;
	}

	public Date getBsStart ()
	{
		return bsStart;
	}

	public Date getBsFinish ()
	{
		return bsFinish;
	}

	public String getResourceName ()
	{
		return resourceName;
	}

	public Integer getResourceId ()
	{
		return resourceId;
	}

	public Integer getProject ()
	{
		return project;
	}

	public String getProjectName ()
	{
		return projectName;
	}

	public String getProjectCode ()
	{
		return projectCode;
	}

	public Date getCurrentDate ()
	{
		return currentDate;
	}

	public boolean getHasNotes ()
	{
		return hasNotes;
	}

}
