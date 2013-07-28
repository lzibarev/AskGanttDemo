package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;
import java.util.Date;

import ru.ask.primaview.gantt.demo.server.prima.PrimaveraService;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.WBS;

public class DataWBS implements Serializable
{
	private static final long		serialVersionUID	= 1577763543824420020L;
	public static final String []	primaveraFields			= new String [] {
			"ObjectId", "OBSName", "Name", "SummaryActualStartDate",
			"SummaryActualFinishDate", "SummaryBaselineStartDate",
			"SummaryBaselineFinishDate"				};

	private Integer					id;
	private String					name;
	private String					obs;
	private Date					start;
	private Date					finish;
	private Date					bsStart;
	private Date					bsFinish;
	private int completePercent;

	private DataWBS []				children;
	private DataActivity []			activities;

	public DataWBS (WBS wbs)
	{
		try
		{
			id = wbs.getObjectId ().toInteger ();
			name = wbs.getName ();
			obs = wbs.getOBSName ();
			start = wbs.getSummaryActualStartDate ();
			finish = wbs.getSummaryActualFinishDate ();
			bsStart = wbs.getSummaryBaselineStartDate ();
			bsFinish = wbs.getSummaryBaselineFinishDate ();

			completePercent = (int) Math.round (wbs.getSummaryDurationPercentComplete().doubleValue () * 100D);

			children = new DataWBS [0];
			activities = new DataActivity [0];
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public DataWBS (WBS wbs, boolean recursively)
	{
		try
		{
			id = wbs.getObjectId ().toInteger ();
			name = wbs.getName ();
			obs = wbs.getOBSName ();
			start = wbs.getSummaryActualStartDate ();
			finish = wbs.getSummaryActualFinishDate ();
	
//			persentDone = wbs.getSummaryPerformancePercentCompleteByCost().intValue();
			bsStart = wbs.getSummaryBaselineStartDate ();
			bsFinish = wbs.getSummaryBaselineFinishDate ();

			if (!recursively)
			{
				children = new DataWBS [0];
				activities = new DataActivity [0];
				return;
			}

			children = PrimaveraService.GetWBSChildren (wbs, recursively);
			activities = PrimaveraService.GetActivitiesByWBS (wbs);
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public Integer getId ()
	{
		return id;
	}

	public String getName ()
	{
		return name;
	}

	public String getObs ()
	{
		return obs;
	}

	public Date getStart ()
	{
		return start;
	}

	public Date getFinish ()
	{
		return finish;
	}

	public Date getBsStart ()
	{
		return bsStart;
	}

	public Date getBsFinish ()
	{
		return bsFinish;
	}
	
	public int getCompletePercent(){
		return completePercent;
	}
	public void setCompletePercent(int value){
		completePercent = value;
	}

	public DataWBS [] getChildren ()
	{
		return children;
	}

	public DataActivity [] getActivities ()
	{
		return activities;
	}

	public void setChildren (DataWBS [] children)
	{
		this.children = children;
	}

	public void setActivities (DataActivity [] activities)
	{
		this.activities = activities;
	}

	public boolean hasChildren ()
	{
		return this.children.length != 0;
	}

	public boolean hasActivities ()
	{
		return this.activities.length != 0;
	}

}
