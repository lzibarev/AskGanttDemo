package ru.ask.primaview.gantt.demo.server.prima;

/**
 * @author Sergey S. Bulanov <maxF212@gmail.com>
 * @copyright 2012-2013 Intellect-Partner, JSC
 * @version 0.1.0
 */

import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Level;

import ru.ask.primaview.gantt.demo.server.prima.utility.*;

import com.primavera.ServerException;
import com.primavera.common.value.ObjectId;
import com.primavera.integration.client.GlobalObjectManager;
import com.primavera.integration.client.bo.BOIterator;
import com.primavera.integration.client.bo.BusinessObject;
import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Activity;
import com.primavera.integration.client.bo.object.ActivityNote;
import com.primavera.integration.client.bo.object.ActivityStep;
import com.primavera.integration.client.bo.object.Document;
import com.primavera.integration.client.bo.object.DocumentCategory;
import com.primavera.integration.client.bo.object.DocumentStatusCode;
import com.primavera.integration.client.bo.object.OBS;
import com.primavera.integration.client.bo.object.Project;
import com.primavera.integration.client.bo.object.ProjectCodeAssignment;
import com.primavera.integration.client.bo.object.ProjectDocument;
import com.primavera.integration.client.bo.object.ProjectNote;
import com.primavera.integration.client.bo.object.ProjectPortfolio;
import com.primavera.integration.client.bo.object.Resource;
import com.primavera.integration.client.bo.object.UDFValue;
import com.primavera.integration.client.bo.object.WBS;
import com.primavera.integration.network.NetworkException;

/**
 * Класс предоставляет данные из Primavera
 * 
 */
public class PrimaveraService {
	public static PrimaveraConnector prima = null;

	/**
	 * Конструктор
	 */
	public PrimaveraService() {

		// Вытаскиваем себе экземпляр коннектора PrimaveraConnector
		if (prima == null) {
			try {
				prima = PrimaveraConnector.getInstance();
			} catch (Exception e) {
				PrimaveraConnector.writeLog(PrimaveraConnector.class.getName()
						+ " create failed.", e);
			}
		}
	}

	private Project GetProject(int projectId, String[] fields) {
		BOIterator<Project> boi = null;

		try {
			boi = prima.getSession().getGlobalObjectManager()
					.loadProjects(fields, "ObjectId=" + projectId, null);
			BusinessObject[] projectsBO = boi.getAll();

			if (projectsBO == null || projectsBO.length == 0)
				return null;

			return (Project) projectsBO[0];
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetProject BusinessObjectException ",
					e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetProject ServerException ", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetProject NetworkException ", e);
		}
		return null;
	}

	/**
	 * Получает документ Primavera Загружаются поля, определенные в
	 * DataDocument.primaveraAPIfields
	 * 
	 * @param documentId
	 * @return null, если документ не найден
	 */
	public Document getRawDocument(int documentId) {
		try {
			return Document.load(prima.getSession(),
					DataDocument.primaveraAPIfields, new ObjectId(documentId));
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetProject BusinessObjectException ",
					e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetProject ServerException ", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetProject NetworkException ", e);
		}

		return null;
	}

	/**
	 * Проверяет существование категории документа.
	 * 
	 * @param dataDocumentCategory
	 * @return null, если не найдена
	 */
	private DocumentCategory getRawDocumentCategory(
			DataDocumentCategory dataDocumentCategory) {
		DocumentCategory dc = null;
		try {
			dc = DocumentCategory.load(prima.getSession(),
					DataDocumentCategory.primaveraAPIfields, new ObjectId(
							dataDocumentCategory.getId()));
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"getRawDocumentCategory BusinessObjectException ", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog(
					"getRawDocumentCategory ServerException ", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog(
					"getRawDocumentCategory NetworkException ", e);
		}
		return dc;
	}

	/**
	 * Проверяет существование статуса документа.
	 * 
	 * @param dataDocumentCategory
	 * @return null, если не найдена
	 */
	private DocumentStatusCode getRawDocumentStatusCode(
			DataDocumentStatusCode dataDocumentStatusCode) {
		DocumentStatusCode dsc = null;
		try {
			dsc = DocumentStatusCode.load(prima.getSession(),
					DataDocumentStatusCode.primaveraAPIfields, new ObjectId(
							dataDocumentStatusCode.getId()));
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"getRawDocumentStatusCode BusinessObjectException ", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog(
					"getRawDocumentStatusCode ServerException ", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog(
					"getRawDocumentStatusCode NetworkException ", e);
		}
		return dsc;
	}

	/**
	 * Получает список проектов портфеля
	 * 
	 * @param portfolioId
	 *            id портфеля
	 * @param where
	 *            условия отбора проектов
	 * @param order
	 *            порядок сортировки
	 * @return массив DataProject[]
	 */
	public DataProject[] GetProjectsByPortfolio(int portfolioId, String where,
			String order) {
		DataProject[] portfolioProjects = null;
		// ArrayList<DataProject> res = new ArrayList<DataProject> ();
		try {
			// prima.writeLog ("GetProjectsByPortfolio load project portfolio",
			// Level.DEBUG);
			ProjectPortfolio pp = ProjectPortfolio.load(prima.getSession(),
					new String[] { "ObjectId" }, new ObjectId(portfolioId));
			if (pp == null) {
				prima.writeLog(new StringBuilder(
						"GetProjectsByPortfolio portfolio ")
						.append(portfolioId).append(" object was null"),
						Level.WARN);
				return portfolioProjects;
			}

			// prima.writeLog
			// ("GetProjectsByPortfolio portfolio loaded, load projects",
			// Level.DEBUG);
			Project eps[] = (Project[]) pp.loadProjects(
					DataProject.ProjectFields, where, order).getAll();

			if (eps != null) {
				// prima.writeLog
				// ("GetProjectsByPortfolio projects loaded to Project[]",
				// Level.DEBUG);
				int count = eps.length;
				portfolioProjects = new DataProject[count];

				for (int i = 0; i < count; i++)
					portfolioProjects[i] = new DataProject(eps[i]);

				prima.writeLog(
						"GetProjectsByPortfolio projects transformed to DataProject[]",
						Level.DEBUG);
			}
		} catch (ServerException e) {
			PrimaveraConnector.writeLog(
					"GetProjectsByPortfolio ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog(
					"GetProjectsByPortfolio NetworkException", e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"GetProjectsByPortfolio BusinessObjectException", e);
		}

		return portfolioProjects;
	}

	/**
	 * Получает список портфелей проектов
	 * 
	 * @param where
	 *            условие отбора портфелей
	 * @param orderBy
	 *            порядок сортировки
	 * @return массив DataProjectPortfolio[]
	 */
	public DataProjectPortfolio[] GetProjectPortfolios(String where,
			String orderBy) {
		DataProjectPortfolio[] portfolios = null;

		// prima.writeLog ("GetProjectPortfolios load project portfolio",
		// Level.DEBUG);

		GlobalObjectManager gom = prima.getSession().getGlobalObjectManager();
		// prima.writeLog ("GetProjectPortfolios got GlobalObjectManager",
		// Level.DEBUG);
		try {
			BOIterator<ProjectPortfolio> boi = gom.loadProjectPortfolios(
					DataProjectPortfolio.primaveraFields, where, orderBy);
			// prima.writeLog ("GetProjectPortfolios got GlobalObjectManager",
			// Level.DEBUG);
			BusinessObject[] projectPortfoliosBO = boi.getAll();
			// prima.writeLog (new StringBuilder
			// ("GetProjectPortfolios got projectPortfoliosBO, length=").append
			// (projectPortfoliosBO.length), Level.DEBUG);
			portfolios = new DataProjectPortfolio[projectPortfoliosBO.length];
			for (int i = 0; i < projectPortfoliosBO.length; i++)
				portfolios[i] = new DataProjectPortfolio(
						(ProjectPortfolio) projectPortfoliosBO[i]);
			// prima.writeLog
			// ("GetProjectPortfolios all portfolios converted to DataProjectPortfolio, end.",
			// Level.DEBUG);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"GetProjectPortfolios BusinessObjectException", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetProjectPortfolios ServerException",
					e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog(
					"GetProjectPortfolios NetworkException", e);
		}

		return portfolios;
	}

	/**
	 * Загружает свойства проекта по его Id
	 * 
	 * @param projectId
	 * @return
	 */
	public DataProject GetProject(int projectId) {
		DataProject result = null;

		try {
			Project project = Project.load(prima.getSession(),
					DataProject.ProjectFieldsWithDuration, new ObjectId(
							projectId));
			if (project == null)
				return null;

			result = new DataProject(project);
			Integer durationPercentage = 0;
			if (project.getSummaryActualDuration() != null
					&& project.getSummaryAtCompletionDuration() != null)
				durationPercentage = (int) Math
						.round((project.getSummaryActualDuration()
								.doubleValue() / project
								.getSummaryAtCompletionDuration().doubleValue()) * 100D);

			result.setDurationPercentage(durationPercentage);
		} catch (BusinessObjectException e) {
			PrimaveraConnector
					.writeLog("GetProject BusinessObjectException", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetProject ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetProject NetworkException", e);
		}

		return result;
	}

	/**
	 * Получает список пользовательских полей проекта с их значениями
	 * 
	 * @param projectId
	 * @param where
	 * @param order
	 * @return
	 */
	public DataUDF[] GetUDF(int projectId) {
		DataUDF[] result = null;
		Project project = null;
		try {
			project = GetProject(projectId, DataProject.ProjectFields);
			if (project == null)
				return null;

			BOIterator<UDFValue> boiUDF = project.loadProjectLevelUDFValues(
					DataUDF.primaveraFields, null, null);
			UDFValue UDFvalues[] = (UDFValue[]) boiUDF.getAll();
			result = new DataUDF[UDFvalues.length];
			for (int i = 0; i < UDFvalues.length; i++)
				result[i] = new DataUDF(UDFvalues[i]);
		} catch (BusinessObjectException e) {
			PrimaveraConnector
					.writeLog("GetProject BusinessObjectException", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetProject ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetProject NetworkException", e);
		}
		return result;
	}

	public DataProjectCodeAssignment[] GetProjectCodeTypes(int projectId) {
		ArrayList<DataProjectCodeAssignment> results = new ArrayList<DataProjectCodeAssignment>();

		try {
			Project project = GetProject(projectId,
					DataProjectCodeAssignment.projectFields);
			if (project == null)
				return null;

			BOIterator<ProjectCodeAssignment> boiPCA = project
					.loadProjectCodeAssignments(
							DataProjectCodeAssignment.primaveraFields, null,
							null);
			while (boiPCA.hasNext())
				results.add(new DataProjectCodeAssignment(
						(ProjectCodeAssignment) boiPCA.next()));
		} catch (BusinessObjectException e) {
			PrimaveraConnector
					.writeLog("GetProject BusinessObjectException", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetProject ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetProject NetworkException", e);
		}

		return (DataProjectCodeAssignment[]) results
				.toArray(new DataProjectCodeAssignment[0]);
	}

	/**
	 * Получить заметки проекта
	 * 
	 * @param pid
	 *            Id проекта
	 * @return массив заметок проекта
	 */
	public DataNote[] GetProjectNotes(int projectId) {
		ArrayList<DataNote> results = new ArrayList<DataNote>();
		Project p = GetProject(projectId, new String[0]);
		if (p == null)
			return null;
		try {
			BOIterator<ProjectNote> boi = p.loadProjectLevelNotes(
					DataNote.primaveraFields, null, null);
			while (boi.hasNext())
				results.add(new DataNote((ProjectNote) boi.next()));
		} catch (BusinessObjectException e) {

			PrimaveraConnector
					.writeLog("GetProject BusinessObjectException", e);
			e.printStackTrace();
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetProject ServerException", e);
			e.printStackTrace();
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetProject NetworkException", e);
			e.printStackTrace();
		}

		return results.toArray(new DataNote[results.size()]);
	}

	public Resource[] GetProjectTeamResources(int projectId) {
		prima.writeLog("GetProjectTeamResources start ", Level.DEBUG);
		try {
			Project project = Project.load(prima.getSession(), new String[0],
					new ObjectId(projectId));
			prima.writeLog("GetProjectTeamResources project loaded ",
					Level.DEBUG);
			if (project == null) {
				prima.writeLog("GetProjectTeamResources project was null ",
						Level.ERROR);
				return null;
			}
			Resource[] resources = (Resource[]) project.loadAllResources(
					DataResource.primaveraFields, null, null).getAll();
			prima.writeLog(
					"GetProjectTeamResources loadAllResources OK. lenght="
							+ resources.length, Level.DEBUG);
			return resources;
		} catch (ServerException e) {
			prima.writeLog("GetProjectTeamResources ServerException "
					+ e.getStackTrace());
			e.printStackTrace();
		} catch (NetworkException e) {
			prima.writeLog("GetProjectTeamResources NetworkException "
					+ e.getStackTrace());
			e.printStackTrace();
		} catch (BusinessObjectException e) {
			prima.writeLog("GetProjectTeamResources BusinessObjectException "
					+ e.getStackTrace());
			e.printStackTrace();
		}
		return null;
	}

	public DataResource[] GetProjectTeam(int projectId) {
		try {
			Project project = Project.load(prima.getSession(), new String[0],
					new ObjectId(projectId));
			if (project == null)
				return null;

			Resource[] resources = (Resource[]) project.loadAllResources(
					DataResource.primaveraFields, null, null).getAll();
			DataResource[] result = new DataResource[resources.length];
			for (int i = 0; i < resources.length; i++)
				result[i] = new DataResource(resources[i]);

			return result;
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetProjectTeam ServerException", e);
			e.printStackTrace();
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetProjectTeam NetworkException", e);
			e.printStackTrace();
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"GetProjectTeam BusinessObjectException", e);
			e.printStackTrace();
		}
		return null;
	}

	public DataActivity GetActivity(int activityId) {
		try {
			Activity result = Activity.load(prima.getSession(),
					DataActivity.primaveraFields, new ObjectId(activityId));
			if (result == null)
				return null;

			return new DataActivity(result);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"GetActivitiesByWBS BusinessObjectException", e);
			e.printStackTrace();
		} catch (ServerException e) {
			PrimaveraConnector
					.writeLog("GetActivitiesByWBS ServerException", e);
			e.printStackTrace();
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetActivitiesByWBS NetworkException",
					e);
			e.printStackTrace();
		}
		return null;
	}

	public static DataActivity[] GetActivitiesByWBS(WBS wbs) {
		// ArrayList<DataActivity> result = new ArrayList<DataActivity> ();
		DataActivity[] result;
		try {
			/*
			 * BOIterator<Activity> boiActivities = wbs.loadActivities
			 * (DataActivity.ActivityFields,
			 * PrimaveraServiceConfig.wbsActivityWhere,
			 * PrimaveraServiceConfig.wbsActivityOrder); while
			 * (boiActivities.hasNext ()) result.add (new DataActivity
			 * (boiActivities.next ())); return result.toArray (new DataActivity
			 * [result.size ()]);
			 */

			Activity[] activities = wbs.loadActivities(
					DataActivity.primaveraFields,
					PrimaveraServiceUtils.wbsActivityWhere,
					PrimaveraServiceUtils.wbsActivityOrder).getAll();
			result = new DataActivity[activities.length];
			for (int i = 0; i < activities.length; i++)
				result[i] = new DataActivity(activities[i]);

			return result;
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"GetActivitiesByWBS BusinessObjectException", e);
		} catch (ServerException e) {
			PrimaveraConnector
					.writeLog("GetActivitiesByWBS ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetActivitiesByWBS NetworkException",
					e);
		}

		// return result.toArray (new DataActivity [0]);
		return null;
	}

	public static DataWBS[] GetWBSChildren(WBS wbs, boolean recursively) {
		DataWBS[] result;
		try {
			// wbs.loadWBSChildren возвращает BOIterator<WBS>, у которого
			// getAll() отдает массив
			// WBS [] children = wbs.loadWBSChildren (DataWBS.wbsFields,
			// "not (UDFInteger$240=1)", "Code asc").getAll ();
			/*
			 * BOIterator<WBS> boiWBS = wbs.loadWBSChildren (DataWBS.wbsFields,
			 * PrimaveraServiceConfig.wbsWhere,
			 * PrimaveraServiceConfig.wbsOrder); while (boiWBS.hasNext ())
			 * result.add (new DataWBS (boiWBS.next (), recursively)); return
			 * result.toArray (new DataWBS [result.size ()]);
			 */
			WBS[] children = wbs.loadWBSChildren(DataWBS.primaveraFields,
					PrimaveraServiceUtils.wbsWhere,
					PrimaveraServiceUtils.wbsOrder).getAll();
			result = new DataWBS[children.length];
			for (int i = 0; i < children.length; i++)
				result[i] = new DataWBS(children[i], recursively);

			return result;
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"GetWBSChildren BusinessObjectException", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetWBSChildren ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetWBSChildren NetworkException", e);
		}
		return null;
	}

	public DataWBS[] GetWBS(int projectId) {
		// ArrayList<DataWBS> result = new ArrayList<DataWBS> ();
		DataWBS[] result;
		try {
			prima.writeLog("GetWBS load project start");
			Project project;
			project = Project.load(prima.getSession(),
					DataProject.ProjectFieldsForWBS, new ObjectId(projectId));
			if (project == null)
				return null;

			// wbs.loadWBSChildren возвращает BOIterator<WBS>, у которого
			// getAll() отдатет массив
			/*
			 * BOIterator<WBS> boiWBS = project.loadWBSChildren
			 * (DataWBS.wbsFields, PrimaveraServiceConfig.wbsWhere,
			 * PrimaveraServiceConfig.wbsOrder); while (boiWBS.hasNext ())
			 * result.add (new DataWBS (boiWBS.next (), true));
			 */

			WBS[] wbsArray = project.loadWBSChildren(DataWBS.primaveraFields,
					PrimaveraServiceUtils.wbsWhere,
					PrimaveraServiceUtils.wbsOrder).getAll();
			result = new DataWBS[wbsArray.length];
			for (int i = 0; i < wbsArray.length; i++)
				result[i] = new DataWBS(wbsArray[i], true);

			prima.writeLog("GetWBS top level WBS convert END");

			// return result.toArray (new DataWBS [result.size ()]);
			return result;
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetWBS BusinessObjectException", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetWBS ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetWBS NetworkException", e);
		}

		return null;
	}

	public DataNote[] GetActivityNotes(int activityId) {
		ArrayList<DataNote> result = new ArrayList<DataNote>();
		try {
			Activity activity = Activity.load(prima.getSession(),
					new String[0], new ObjectId(activityId));
			if (activity == null) {
				prima.writeLog(new StringBuilder("activity ")
						.append(activityId).append(" not found"), Level.ERROR);
				return null;
			}
			BOIterator<ActivityNote> boiActivityNotes = activity
					.loadActivityNotes(DataNote.primaveraFields, null, null);
			while (boiActivityNotes.hasNext())
				result.add(new DataNote(boiActivityNotes.next()));

			return result.toArray(new DataNote[result.size()]);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetActivityNotes ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetActivityNotes NetworkException", e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"GetActivityNotes BusinessObjectException", e);
		}

		return null;
	}

	public DataActivityStep[] GetActivitySteps(int activityId) {
		DataActivityStep[] result;
		try {
			Activity activity = Activity.load(prima.getSession(),
					new String[0], new ObjectId(activityId));
			if (activity == null) {
				prima.writeLog(new StringBuilder("activity ")
						.append(activityId).append(" not found"), Level.ERROR);
				return null;
			}

			ActivityStep[] steps = activity.loadActivitySteps(
					DataActivityStep.primaveraFields, null, null).getAll();
			if (steps.length == 0)
				return null;

			result = new DataActivityStep[steps.length];
			for (int i = 0; i < steps.length; i++)
				result[i] = new DataActivityStep(steps[i]);

			return result;
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetActivityNotes ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetActivityNotes NetworkException", e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"GetActivityNotes BusinessObjectException", e);
		}
		return null;
	}

	public static String formatNoteContent(Date date, String author, String text) {
		return String.format(PrimaveraServiceUtils.notesTableRowTemplate,
				PrimaveraServiceUtils.dateFormatDDdMMdYYYY(date), author, text);
	}

	/**
	 * Добавляет в заметку новый текст
	 * 
	 * @param note
	 *            данные заметки проекта или работы
	 * @param author
	 *            автор нового текста
	 * @param noteContent
	 *            новый текст
	 * @return
	 */
	public boolean AppendNote(DataNote note, String author, String noteContent) {
		prima.writeLog("AppendNote start");

		if (note == null) {
			prima.writeLog("AppendNote note is null", Level.ERROR);
			return false;
		}

		String noteCurrentContent = note.getNote();
		if (noteCurrentContent == null)
			noteCurrentContent = "";
		prima.writeLog("AppendNote noteCurrentContent is: "
				+ noteCurrentContent);
		prima.writeLog("AppendNote starts with <table: "
				+ noteCurrentContent.startsWith("<table"));
		prima.writeLog("AppendNote ends with </table>: "
				+ noteCurrentContent.endsWith("</table>"));

		if (noteCurrentContent.startsWith("<table")
				&& noteCurrentContent.endsWith("</table>"))
			// если таблица есть, то отрежем с конца '</table>'
			noteCurrentContent = noteCurrentContent.substring(0,
					noteCurrentContent.length() - 8);
		else
			// если таблицы нет, то добавим в начале <table> и ряд из старых
			// данных
			noteCurrentContent = "<table>"
					+ String.format(
							PrimaveraServiceUtils.notesTableRowSimpleTemplate,
							noteCurrentContent);

		// допишем новый ряд и вернем обратно '</table>'
		noteCurrentContent = noteCurrentContent
				+ formatNoteContent(new Date(), author, noteContent)
				+ "</table>";
		prima.writeLog("AppendNote note content modified to: "
				+ noteCurrentContent);
		if (note.getType() == DataNote.NOTE_TYPE_ACTIVITY)
			return createActivityNote(note.getParentId(), note.getTopicId(),
					noteCurrentContent);

		if (note.getType() == DataNote.NOTE_TYPE_PROJECT)
			return createProjectNote(note.getParentId(), note.getTopicId(),
					noteCurrentContent);

		return false;
	}

	/**
	 * Сохраняет заметку к работе
	 * 
	 * @param activityId
	 * @param noteId
	 * @param noteContent
	 * @return
	 */
	public boolean createActivityNote(int activityId, int noteId,
			String noteContent) {
		prima.writeLog(new StringBuilder("SaveActivityNote start. activity ")
				.append(activityId));
		Activity activity;
		try {
			activity = Activity.load(prima.getSession(), new String[0],
					new ObjectId(activityId));
			if (activity == null) {
				prima.writeLog(new StringBuilder("SaveActivityNote activity ")
						.append(activityId).append(" not found"), Level.ERROR);
				return false;
			}

			// ActivityNote note = ActivityNote.load (prima.getSession (),
			// DataNote.noteFields, new ObjectId (noteId));
			BOIterator<ActivityNote> boiActivities = activity
					.loadActivityNotes(DataNote.primaveraFields,
							(new StringBuilder("NotebookTopicObjectId="))
									.append(noteId).toString(), null);
			/*
			 * if (note == null) { prima.writeLog (new StringBuilder
			 * ("SaveActivityNote activityNote ").append (noteId).append
			 * (" not found"), Level.ERROR); return false; }
			 */
			if (!boiActivities.hasNext()) {
				prima.writeLog(new StringBuilder(
						"SaveActivityNote activityNote ").append(noteId)
						.append(" not found"), Level.ERROR);
				return false;
			}
			ActivityNote note = boiActivities.next();
			note.setNote(noteContent);
			note.update();

			return true;
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("SaveActivityNote ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("SaveActivityNote NetworkException", e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"SaveActivityNote BusinessObjectException", e);
		}
		return false;
	}

	/**
	 * Сохраняет заметку к проекту
	 * 
	 * @param projectId
	 * @param noteId
	 * @param noteContent
	 * @return
	 */
	public boolean createProjectNote(int projectId, int noteId,
			String noteContent) {
		try {
			Project project = Project.load(prima.getSession(), new String[0],
					new ObjectId(projectId));
			if (project == null) {
				prima.writeLog(new StringBuilder("SaveProjectNote project ")
						.append(projectId).append(" not found"), Level.ERROR);
				return false;
			}

			ProjectNote note = ProjectNote.load(prima.getSession(),
					DataNote.primaveraFields, new ObjectId(noteId));
			if (note == null) {
				prima.writeLog(new StringBuilder("SaveProjectNote note ")
						.append(noteId).append(" not found"), Level.ERROR);
				return false;
			}

			note.setNote(noteContent);
			note.update();
			return true;
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("SaveProjectNote ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("SaveProjectNote NetworkException", e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"SaveProjectNote BusinessObjectException", e);
		}

		return false;
	}

	/**
	 * Сохраняет документ, привязанный к работе
	 * 
	 * @param projectId
	 * @param activityId
	 * @param title
	 * @param author
	 * @param filePath
	 *            URL файла
	 * @param category
	 * @param status
	 * @return
	 */
	@Deprecated
	public Integer createProjectDocument(int projectId, int activityId,
			String title, String author, String fileURL, int category,
			int status, String referenceNumber) {
		Project project;
		try {
			project = Project.load(prima.getSession(),
					DataProject.ProjectFields, new ObjectId(projectId));
			if (project == null) {
				prima.writeLog(
						new StringBuilder(
								"SaveProjectDocument failed, project >")
								.append(projectId).append(
										"< not found, is null"), Level.FATAL);
				return null;
			}

			Document doc = new Document(prima.getSession());
			doc.setAuthor(author);
			doc.setTitle(title);
			doc.setDocumentCategoryObjectId(new ObjectId(category));
			doc.setDocumentStatusCodeObjectId(new ObjectId(status));
			doc.setProjectObjectId(project.getObjectId());
			doc.setPublicLocation(fileURL);

			ObjectId documentId = doc.create();
			ProjectDocument projectDocument = new ProjectDocument(
					prima.getSession());
			projectDocument.setActivityObjectId(new ObjectId(activityId));
			projectDocument.setDocumentObjectId(documentId);
			projectDocument.setProjectObjectId(project.getObjectId());
			ObjectId projectDocumentObjectId = projectDocument.create();

			return projectDocumentObjectId != null ? projectDocumentObjectId
					.toInteger() : null;
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("SaveProjectDocument ServerException",
					e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("SaveProjectDocument NetworkException",
					e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"SaveProjectDocument BusinessObjectException", e);
		}

		return null;
	}

	/*
	 * public static DataActivityStep [] GetActivitySteps (int activityId) {
	 * Activity activity = Activity.load (prima.getSession (), new String [0],
	 * new ObjectId (activityId)); if (activity == null) { prima.writeLog (new
	 * StringBuilder ("activity ").append (activityId).append (" not found"),
	 * Level.ERROR); return null; } ActivityStep [] steps =
	 * activity.loadActivitySteps (DataActivityStep.activityStepFields, null,
	 * null).getAll (); DataActivityStep [] result = new DataActivityStep
	 * [steps.length]; for (int i = 0; i < steps.length; i++) result [i] = new
	 * DataActivityStep (steps [i]); return result; }
	 */

	/**
	 * Отдает массив
	 * 
	 * @return
	 */
	public ArrayList<DataOBS> GetOBS(Integer obsId, Integer level) {
		ArrayList<DataOBS> result = new ArrayList<DataOBS>();
		if (level == null)
			level = 0;

		GlobalObjectManager gom = prima.getSession().getGlobalObjectManager();
		// prima.writeLog ("GetOBS GlobalObjectManager created on level " +
		// level);
		String whereClause = (obsId == null || obsId == 0) ? "ParentObjectId is null"
				: ("ParentObjectId=" + obsId.toString());
		// prima.writeLog ("where: " + whereClause);
		try {
			OBS tmpOBS = null;
			BOIterator<OBS> boiOBS = gom.loadOBS(DataOBS.primaveraFields,
					whereClause, null);
			if (boiOBS == null) {
				prima.writeLog("BOIterator was NULL", Level.ERROR);
				return result;
			}
			// prima.writeLog ("GetOBS before while");
			while (boiOBS.hasNext()) {
				tmpOBS = boiOBS.next();
				;
				if (tmpOBS == null) {
					prima.writeLog("GetOBS tmpObs is null", Level.ERROR);
					continue;
				}
				// prima.writeLog ("GetOBS tmpObs " + tmpOBS.getObjectId
				// ().toInteger ());

				result.add(new DataOBS(tmpOBS, level));
				if (tmpOBS != null) {
					// prima.writeLog ("GetOBS ask for children of " +
					// tmpOBS.getObjectId ().toInteger ());
					ArrayList<DataOBS> children = GetOBS(tmpOBS.getObjectId()
							.toInteger(), level + 1);

					result.addAll(children);
				}
			}
			// prima.writeLog ("GetOBS after while");
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetOBS ServerException", e);
			e.printStackTrace();
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetOBS NetworkException", e);
			e.printStackTrace();
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetOBS BusinessObjectException", e);
			e.printStackTrace();
		}
		return result;
	}

	public DataOBS GetOBS(Integer obsId) {
		if (obsId == null || obsId == 0)
			return null;

		try {
			return new DataOBS(OBS.load(prima.getSession(),
					DataOBS.primaveraFields, new ObjectId(obsId)));
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetOBS ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetOBS NetworkException", e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetOBS BusinessObjectException", e);
		}
		return null;
	}

	public ArrayList<DataResource> GetResources() {
		ArrayList<DataResource> result = new ArrayList<DataResource>();
		Resource tmpResource = null;
		GlobalObjectManager gom = prima.getSession().getGlobalObjectManager();
		// prima.writeLog ("GetResources GlobalObjectManager created ");
		try {
			BOIterator<Resource> boiResources = gom.loadResources(
					DataResource.primaveraFields,
					PrimaveraServiceUtils.resourcesWhere,
					PrimaveraServiceUtils.resourcesOrder);

			if (boiResources == null) {
				prima.writeLog("GetResources BOIterator was NULL", Level.ERROR);
				return result;
			}
			// prima.writeLog ("GetResources before while");
			while (boiResources.hasNext()) {
				tmpResource = boiResources.next();
				// prima.writeLog ("GetResources tmpResource " +
				// tmpResource.getObjectId ().toInteger ());
				result.add(new DataResource(tmpResource));
			}

		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetOBS ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetOBS NetworkException", e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetOBS BusinessObjectException", e);
		}
		return result;
	}

	public DataResource GetResource(Integer resourceId) {
		if (resourceId == null || resourceId == 0)
			return null;

		try {
			Resource res = Resource.load(prima.getSession(),
					DataResource.primaveraFields, new ObjectId(resourceId));
			return new DataResource(res);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetResource ServerException ", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetResource NetworkException ", e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetResource BusinessObjectException ",
					e);
		}
		return null;
	}

	public ArrayList<DataDocumentStatusCode> GetDocumentStatusCodes() {
		ArrayList<DataDocumentStatusCode> result = new ArrayList<DataDocumentStatusCode>();

		try {
			BOIterator<DocumentStatusCode> boiStatusCodes = prima
					.getSession()
					.getGlobalObjectManager()
					.loadDocumentStatusCodes(
							DataDocumentStatusCode.primaveraAPIfields, null,
							null);
			while (boiStatusCodes.hasNext())
				result.add(new DataDocumentStatusCode(boiStatusCodes.next()));
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetDocumentStatusCodes failed.", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetDocumentStatusCodes failed.", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetDocumentStatusCodes failed.", e);
		}

		return result;
	}

	public ArrayList<DataDocumentCategory> getDocumentCategories() {
		ArrayList<DataDocumentCategory> result = new ArrayList<DataDocumentCategory>();

		BOIterator<DocumentCategory> boiCategories;
		try {
			boiCategories = prima
					.getSession()
					.getGlobalObjectManager()
					.loadDocumentCategories(
							DataDocumentCategory.primaveraAPIfields, null,
							PrimaveraServiceUtils.dcOrder);
			while (boiCategories.hasNext())
				result.add(new DataDocumentCategory(boiCategories.next()));
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetDataDocumentCategories failed.", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetDataDocumentCategories failed.", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetDataDocumentCategories failed.", e);
		}
		return result;
	}

	public ArrayList<DataDocument> getDocuments(int projectId) {
		if (PrimaveraConnector.logger.isDebugEnabled())
			prima.writeLog("getDocuments start", Level.DEBUG);
		ArrayList<DataDocument> result = new ArrayList<DataDocument>();

		Project p = GetProject(projectId, new String[0]);
		if (p == null) {
			if (PrimaveraConnector.logger.isDebugEnabled())
				prima.writeLog(
						"getDocuments project is null, return empty ArrayList<DataDocument>",
						Level.DEBUG);
			return result;
		}

		try {
			BOIterator<Document> boiDocuments = p.loadDocuments(
					DataDocument.primaveraAPIfields, null, null);
			if (PrimaveraConnector.logger.isDebugEnabled())
				prima.writeLog("getDocuments documents loaded", Level.DEBUG);

			while (boiDocuments.hasNext())
				result.add(new DataDocument(boiDocuments.next(),
						DataDocument.DT_PROJECT, projectId));

			if (PrimaveraConnector.logger.isDebugEnabled())
				prima.writeLog(
						"getDocuments documents converted to ArrayList<DataDocument>, total: "
								+ result.size(), Level.DEBUG);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetDocuments failed.", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetDocuments failed.", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetDocuments failed.", e);
		}

		return result;
	}

	public ArrayList<DataDocument> GetActivityDocuments(int activityId) {
		ArrayList<DataDocument> result = new ArrayList<DataDocument>();

		try {
			Activity a = Activity.load(prima.getSession(), new String[0],
					new ObjectId(activityId));
			BOIterator<ProjectDocument> boiProjectDocuments = a
					.loadProjectDocuments(
							DataDocument.primaveraProjectDocumentfields, null,
							null);
			while (boiProjectDocuments.hasNext()) {
				ProjectDocument pd = boiProjectDocuments.next();
				Document d = Document.load(prima.getSession(),
						DataDocument.primaveraAPIfields,
						pd.getDocumentObjectId());
				result.add(new DataDocument(d, DataDocument.DT_ACTIVITY,
						activityId));
			}
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("GetActivityDocuments failed.", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("GetActivityDocuments failed.", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("GetActivityDocuments failed.", e);
		}
		return result;
	}

	/**
	 * Сохраняет документ, привязанный к проекту
	 * 
	 * @param projectId
	 * @param title
	 * @param author
	 * @param fileURL
	 *            URL файла
	 * @param category
	 * @param statusCode
	 * @param referenceNumber
	 * 
	 * @return Id документа, если он сохранен, или 0
	 */
	@Deprecated
	public Integer createDocument(int projectId, String title, String author,
			String fileURL, int category, int statusCode, String referenceNumber) {
		Project p = GetProject(projectId, new String[0]);
		if (p == null) {
			prima.writeLog(new StringBuilder("SaveDocument failed, project >")
					.append(projectId).append("< not found, is null"),
					Level.FATAL);
			return null;
		}

		try {
			Document d = new Document(prima.getSession());
			// привяжем документ к проекту
			d.setProjectObjectId(p.getObjectId());

			// заполним свойства документа
			d.setAuthor(author);
			d.setTitle(title);
			d.setDocumentCategoryObjectId(new ObjectId(category));
			d.setDocumentStatusCodeObjectId(new ObjectId(statusCode));
			d.setPublicLocation(fileURL);
			d.setReferenceNumber(referenceNumber);

			ObjectId documentObjectId = d.create();

			return documentObjectId != null ? documentObjectId.toInteger() : 0;
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("SaveDocument failed.", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("SaveDocument failed.", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("SaveDocument failed.", e);
		}

		return null;
	}

	/**
	 * Сохраняет документ, привязанный к проекту
	 * 
	 * @param doc
	 *            DataDocument, на основе которого создается документ в
	 *            Primavera
	 */
	protected Document createDocument(DataDocument doc) {
		if (PrimaveraConnector.logger.isInfoEnabled())
			prima.writeLog("createDocument start");
		Project p = GetProject(doc.getReferenceProjectId(), new String[0]);
		if (p == null) {
			prima.writeLog(
					new StringBuilder("createDocument failed, project >")
							.append(doc.getReferenceProjectId()).append(
									"< not found, is null"), Level.FATAL);
			return null;
		}

		try {
			Document d = new Document(prima.getSession());
			// привяжем документ к проекту
			d.setProjectObjectId(p.getObjectId());
			if (PrimaveraConnector.logger.isInfoEnabled())
				prima.writeLog("createDocument doc-instance created, is bound to project "
						+ doc.getReferenceProjectId());

			// заполним свойства документа
			d.setAuthor(doc.getAuthor());
			d.setTitle(doc.getTitle());
			d.setDocumentCategoryObjectId(new ObjectId(doc
					.getDocumentCategory().getId()));
			d.setDocumentStatusCodeObjectId(new ObjectId(doc
					.getDocumentStatusCode().getId()));
			d.setPublicLocation(doc.getLocation());
			d.setReferenceNumber(doc.getReferenceNumber());

			ObjectId documentObjectId = d.create();
			if (PrimaveraConnector.logger.isInfoEnabled())
				prima.writeLog("createDocument doc created in Primavera, docId "
						+ documentObjectId.toString());
			// установить id в новом документе
			if (documentObjectId != null) {
				doc.setId(documentObjectId.toInteger());
				return d;
			}
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog("createDocument failed.", e);
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("createDocument failed.", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("createDocument failed.", e);
		}

		return null;
	}

	/**
	 * Сохраняет документ, привязанный к задаче
	 * 
	 * @param doc
	 *            DataDocument, на основе которого создается документ в
	 *            Primavera
	 * @return
	 */
	protected Document createProjectDocument(DataDocument doc) {
		if (PrimaveraConnector.logger.isInfoEnabled())
			prima.writeLog("createProjectDocument start");
		Project project;
		try {
			project = Project.load(prima.getSession(),
					DataProject.ProjectFields,
					new ObjectId(doc.getReferenceProjectId()));
			if (project == null) {
				prima.writeLog(
						new StringBuilder(
								"SaveProjectDocument failed, project >")
								.append(doc.getReferenceProjectId()).append(
										"< not found, is null"), Level.FATAL);
				return null;
			}

			Document document = new Document(prima.getSession());
			document.setAuthor(doc.getAuthor());
			document.setTitle(doc.getTitle());
			document.setDocumentCategoryObjectId(new ObjectId(doc
					.getDocumentCategory().getId()));
			document.setDocumentStatusCodeObjectId(new ObjectId(doc
					.getDocumentStatusCode().getId()));
			document.setProjectObjectId(project.getObjectId());
			document.setPublicLocation(doc.getLocation());

			ObjectId documentId = document.create();
			ProjectDocument projectDocument = new ProjectDocument(
					prima.getSession());
			projectDocument.setActivityObjectId(new ObjectId(doc
					.getReferenceActivity()));
			projectDocument.setDocumentObjectId(documentId);
			projectDocument.setProjectObjectId(project.getObjectId());

			ObjectId projectDocumentObjectId = projectDocument.create();

			// установить id в новом документе
			if (projectDocumentObjectId != null) {
				doc.setId(documentId.toInteger());
				return document;
			}
			return null;
		} catch (ServerException e) {
			PrimaveraConnector.writeLog(
					"createProjectDocument ServerException", e);
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog(
					"createProjectDocument NetworkException", e);
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"createProjectDocument BusinessObjectException", e);
		}

		return null;
	}

	/**
	 * Удалить документ
	 * 
	 * @param docId
	 * @param author
	 * @return false при ошибке
	 */
	public boolean deleteDocument(int docId, String author) {
		try {
			if (PrimaveraConnector.logger.isDebugEnabled())
				prima.writeLog("DeleteDocument " + docId);
			ObjectId documentObjId = new ObjectId(docId);
			Document document = Document.load(prima.getSession(),
					Document.getAllFields(), documentObjId);

			if (PrimaveraConnector.logger.isDebugEnabled())
				prima.writeLog("DeleteDocument doc found " + docId);
			String docAuthor = document.getAuthor();

			if (!author.equals(docAuthor)) {
				prima.writeLog("Tried to delete document " + docId
						+ ". Document is NOT deleted, author mismatch.\n\t"
						+ docAuthor + "<>" + author, Level.ERROR);
				return false;
			}
			document.delete();
			return true;
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"DeleteDocument BusinessObjectException ", e);
			return false;
		} catch (ServerException e) {
			PrimaveraConnector.writeLog("DeleteDocument ServerException ", e);
			return false;
		} catch (NetworkException e) {
			PrimaveraConnector.writeLog("DeleteDocument NetworkException ", e);
			return false;
		} catch (Exception e) {
			PrimaveraConnector.writeLog("DeleteDocument NetworkException ", e);
			return false;
		}
	}

	/**
	 * Удалить документ
	 * 
	 * @param dataDoc
	 *            - документ
	 * @return false при ошибке
	 */
	public boolean deleteDocument(DataDocument dataDoc) {
		return deleteDocument(dataDoc.getId(), dataDoc.getAuthor());
	}

	/**
	 * Сохраняет сущестующий документ, либо создает новый. При создании нового
	 * документа изменится его поле id, в него будет записан id созданного
	 * документа
	 * 
	 * @param dataDoc
	 *            - документ
	 * @return false при ошибке
	 */
	public boolean saveDocument(DataDocument dataDoc) {
		Integer docId = dataDoc.getId();
		Document document = null;
		// если документ в Primavera еще не создан
		if (docId == null) {
			prima.writeLog("saveDocument docId is null, have to create new document");
			// надо создать документ нужного типа
			// сохранить его и уйти
			if (dataDoc.getDocumentType().equals(DataDocument.DT_ACTIVITY))
				document = createProjectDocument(dataDoc);

			if (dataDoc.getDocumentType().equals(DataDocument.DT_PROJECT))
				document = createDocument(dataDoc);

			prima.writeLog("saveDocument docId is null, document created");
			prima.writeLog("saveDocument docId is null, document "
					+ (document != null) + ", dataDoc.getId() = "
					+ dataDoc.getId());
			return document != null;
		}

		// продолжаем работу для существующего документа
		try {
			if (PrimaveraConnector.logger.isDebugEnabled())
				prima.writeLog("saveDocument " + docId);

			document = getRawDocument(docId);
			if (document == null) {
				prima.writeLog("saveDocument failed. Document not found: "
						+ docId);
				return false;
			}

			if (PrimaveraConnector.logger.isDebugEnabled())
				prima.writeLog("saveDocument doc found " + docId);

			String pvDocAuthor = document.getAuthor();

			if (!dataDoc.getAuthor().equals(pvDocAuthor)) {
				prima.writeLog("Tried to save document " + docId
						+ ". Document is NOT saved, author mismatch.\n\t"
						+ pvDocAuthor + "<>" + dataDoc.getAuthor(), Level.ERROR);
				return false;
			}

			// изменяем значения в полях документа Primavera
			if (!dataDoc.getLocation().equals(document.getPublicLocation())) {
				document.setPublicLocation(dataDoc.getLocation());
				if (PrimaveraConnector.logger.isInfoEnabled())
					prima.writeLog("saveDocument location changed to "
							+ dataDoc.getLocation());
			}

			// DocumentCategory
			if (!document.getDocumentCategoryObjectId().toInteger()
					.equals(dataDoc.getDocumentCategory().getId())) {
				DocumentCategory dc = getRawDocumentCategory(dataDoc
						.getDocumentCategory());
				if (PrimaveraConnector.logger.isDebugEnabled())
					prima.writeLog(
							"saveDocument try to change DocumentCategory to "
									+ dataDoc.getDocumentCategory().getName(),
							Level.DEBUG);
				try {
					document.setDocumentCategoryObjectId(dc.getObjectId());
					if (PrimaveraConnector.logger.isInfoEnabled())
						prima.writeLog("saveDocument DocumentCategory changed");
				} catch (NullPointerException e) {
					PrimaveraConnector.writeLog(
							"saveDocument NullPointerException ", e);
				}
			}

			// DocumentStatusCode
			ObjectId scObjectId = document.getDocumentStatusCodeObjectId();
			if (dataDoc.getDocumentStatusCode().getId() != null
					&& (scObjectId == null || (scObjectId != null && !scObjectId
							.toInteger().equals(
									dataDoc.getDocumentStatusCode().getId())))) {
				DocumentStatusCode dsc = getRawDocumentStatusCode(dataDoc
						.getDocumentStatusCode());
				if (PrimaveraConnector.logger.isDebugEnabled())
					prima.writeLog(
							"saveDocument try to change DocumentStatusCode to "
									+ dataDoc.getDocumentStatusCode().getName(),
							Level.DEBUG);
				try {
					document.setDocumentStatusCodeObjectId(dsc.getObjectId());
					if (PrimaveraConnector.logger.isInfoEnabled())
						prima.writeLog("saveDocument DocumentStatusCode changed");
				} catch (NullPointerException e) {
					PrimaveraConnector.writeLog(
							"DeleteDocument NullPointerException ", e);
				}
			}

			if (!dataDoc.getTitle().equals(document.getTitle())) {
				document.setTitle(dataDoc.getTitle());
				if (PrimaveraConnector.logger.isInfoEnabled())
					prima.writeLog("saveDocument Title changed to "
							+ dataDoc.getTitle());
			}

			document.update();
			if (PrimaveraConnector.logger.isDebugEnabled())
				prima.writeLog("saveDocument end", Level.DEBUG);

			return true;
		} catch (BusinessObjectException e) {
			PrimaveraConnector.writeLog(
					"saveDocument BusinessObjectException ", e);
			return false;
		} catch (Exception e) {
			PrimaveraConnector.writeLog("saveDocument NetworkException ", e);
			return false;
		}
	}
}
