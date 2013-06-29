package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;
import java.util.Date;

import com.primavera.integration.client.bo.BOIterator;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Project;
import com.primavera.integration.client.bo.object.ProjectCodeAssignment;
import com.primavera.integration.client.bo.object.UDFValue;

@SuppressWarnings ("serial")
public class DataProject implements Serializable
{
	// список полей проекта для загрузки из Primavera
	public static final String []	ProjectFields				= new String [] {
			"Id", "ObjectId", "Name", "OBSName", "PlannedStartDate",
			"DataDate", "MustFinishByDate", "SummaryBaselineStartDate",
			"SummaryBaselineFinishDate", "CodeValue$19", "UDFText$140",
			"UDFText$146"										};

	public static final String []	ProjectFieldsWithDuration	= new String [] {
			"Id", "ObjectId", "Name", "OBSName", "PlannedStartDate",
			"DataDate", "MustFinishByDate", "SummaryBaselineStartDate",
			"SummaryBaselineFinishDate", "CodeValue$19", "UDFText$140",
			"UDFText$146", "SummaryActualDuration",
			"SummaryAtCompletionDuration"						};

	public static final String []	ProjectFieldsForWBS			= new String [] {
			"Id", "ObjectId", "Name"							};

	private Integer					id;
	private String					name;
	private String					code;
	private String					obsName;
	private Date					plannedStart;
	private Date					currentDate;
	private Date					mfbDate;
	private Date					summaryStartDate;
	private Date					summaryFinishDate;
	private String					projectStatus;
	private Integer					durationPercentage;			// длительность (не указана в списке полей)
	private String					chiffre;						// шифр
	private String					chiffrePrD;					// шифр ПрД

	public DataProject (Project project)
	{
		try
		{
			id = project.getObjectId ().toInteger ();
			name = project.getName ();
			code = (String) project.getId ();
			obsName = project.getOBSName ();
			plannedStart = project.getPlannedStartDate ();
			currentDate = project.getDataDate ();
			mfbDate = project.getMustFinishByDate ();
			summaryStartDate = project.getSummaryBaselineStartDate ();
			summaryFinishDate = project.getSummaryBaselineFinishDate ();

			// PrimaveraConnector.logger.debug (new StringBuilder ("before loadProjectCodeAssignments, project ").append (id));
			BOIterator<ProjectCodeAssignment> bb = project.loadProjectCodeAssignments (null, null, null);
			// PrimaveraConnector.logger.debug (new StringBuilder ("before loadProjectCodeAssignments done, project ").append (id));
			if (bb.hasNext ())
			{
				ProjectCodeAssignment pca = (ProjectCodeAssignment) bb.next ();
				projectStatus = pca.getProjectCodeObjectId ().toString ();
				// PrimaveraConnector.logger.debug (new StringBuilder ("got projectStatus ").append (projectStatus));
			}
			// PrimaveraConnector.logger.debug (new StringBuilder ("before loadAllUDFValues, project ").append (id));
			BOIterator<UDFValue> b2 = project.loadAllUDFValues (null, null, null);
			if (b2.hasNext ())
			{
				UDFValue udfValue = (UDFValue) b2.next ();
				chiffre = udfValue.getProjectObjectId ().toString ();
				chiffrePrD = udfValue.getForeignObjectId ().toString ();
			}

			// PrimaveraConnector.logger.debug (new StringBuilder ("project finish: ").append (id));
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
		catch (NullPointerException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
		catch (Exception e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
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

	public String getCode ()
	{
		return code;
	}

	public void setCode (String code)
	{
		this.code = code;
	}

	public String getObsName ()
	{
		return obsName;
	}

	public void setObsName (String obsName)
	{
		this.obsName = obsName;
	}

	public Date getPlannedStart ()
	{
		return plannedStart;
	}

	public void setPlannedStart (Date plannedStart)
	{
		this.plannedStart = plannedStart;
	}

	public Date getCurrentDate ()
	{
		return currentDate;
	}

	public void setCurrentDate (Date currentDate)
	{
		this.currentDate = currentDate;
	}

	public Date getMfbDate ()
	{
		return mfbDate;
	}

	public void setMfbDate (Date mfbDate)
	{
		this.mfbDate = mfbDate;
	}

	public Date getSummaryStartDate ()
	{
		return summaryStartDate;
	}

	public void setSummaryStartDate (Date summaryStartDate)
	{
		this.summaryStartDate = summaryStartDate;
	}

	public Date getSummaryFinishDate ()
	{
		return summaryFinishDate;
	}

	public void setSummaryFinishDate (Date summaryFinishDate)
	{
		this.summaryFinishDate = summaryFinishDate;
	}

	public String getProjectStatus ()
	{
		return projectStatus;
	}

	public void setProjectStatus (String projectStatus)
	{
		this.projectStatus = projectStatus;
	}

	public Integer getDurationPercentage ()
	{
		return durationPercentage;
	}

	public void setDurationPercentage (Integer durationPercentage)
	{
		this.durationPercentage = durationPercentage;
	}

	public String getChiffre ()
	{
		return chiffre;
	}

	public void setChiffre (String value)
	{
		chiffre = value;
	}

	public String getChiffrePrD ()
	{
		return chiffrePrD;
	}

	public void setChiffrePrD (String value)
	{
		chiffrePrD = value;
	}

}
