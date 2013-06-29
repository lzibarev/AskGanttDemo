/**
 * @author Sergey S. Bulanov <maxF212@gmail.com>
 * @copyright 2012 Intellect-Partner, JSC
 * @version 0.1.0
 */
package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;
import java.util.Date;

import ru.ask.primaview.gantt.demo.server.prima.PrimaveraService;

import com.primavera.integration.client.bo.BusinessObjectException;
import com.primavera.integration.client.bo.object.Document;

/**
 * Класс-обертка для работы с документами в Primavera.
 * Для работы с документами в Liferay используется DMStorageService () сервис доступа к Liferay Documents and Media Library.
 * В документе Primavera в поле referenceNumber хранится путь к файлу документа в хранилище, относительно корня хранилища:
 * /projectId/docs/categoryId/file.ext
 */
public class DataDocument implements Serializable
{
	/*
	 * Все поля Document:
	 * Author, ContentRepositoryDocumentInternalId, CreateDate, CreateUser, Deliverable, Description, DocumentCategoryName,
	 * DocumentCategoryObjectId, DocumentStatusCodeName, DocumentStatusCodeObjectId, DocumentType, GUID, IsBaseline,
	 * IsTemplate, LastUpdateDate, LastUpdateUser, ObjectId, ParentObjectId, PrivateLocation, ProjectId, ProjectObjectId,
	 * PublicLocation, ReferenceNumber, ResourceId, ResourceName, ResourceObjectId, RevisionDate, SequenceNumber,
	 * Title, Version
	 */

	private static final long			serialVersionUID				= 1496967646810232424L;
	public static final String []		primaveraAPIfields				= new String [] {
			"ObjectId", "Title", "CreateDate", "DocumentCategoryName",
			"DocumentCategoryObjectId", "DocumentStatusCodeName",
			"DocumentStatusCodeObjectId", "Author", "PublicLocation",
			"ReferenceNumber"											};
	public static final String []		primaveraProjectDocumentfields	= new String [] { "DocumentObjectId" };

	// типы документов
	public static final Integer			DT_ACTIVITY						= 1;
	public static final Integer			DT_PROJECT						= 2;

	protected Integer					id								= null;
	protected String					title							= null;
	protected String					location						= null;
	protected Date						created							= null;
	// protected Integer categoryId = 0;
	// protected String status = null;
	// protected Integer statusId = null;
	protected String					author							= null;

	protected String					referenceNumber					= null;
	protected Integer					documentType					= null;
	protected Integer					referenceActivity				= null;
	protected Integer					referenceProjectId				= null;

	protected DataDocumentCategory		documentCategory				= null;
	protected DataDocumentStatusCode	documentStatusCode				= null;

	/**
	 * Конструктор пустого DataDocument
	 * 
	 * @param documentType
	 *            тип документа (привязан к работе или к проекту)
	 * @throws Exception
	 */
	protected DataDocument (Integer documentType) throws Exception
	{
		if (!(documentType.equals (DT_ACTIVITY) || documentType.equals (DT_PROJECT)))
			throw new Exception ("DataDocument is invalid (" + documentType + ")");

		this.documentType = documentType;
	}

	public static DataDocument createProjectDocument (int projectId)
	{
		DataDocument dd = null;
		try
		{
			dd = new DataDocument (DT_PROJECT);
		}
		catch (Exception e)
		{}

		dd.referenceProjectId = projectId;

		return dd;
	}

	public static DataDocument createActivityDocument (int projectId, int activityId)
	{
		DataDocument dd = null;
		// Exception срабатывает, если указать неправильный тип, у нас же все в порядке с этим.
		try
		{
			dd = new DataDocument (DT_ACTIVITY);
			dd.referenceProjectId = projectId;
			dd.referenceActivity = activityId;
		}
		catch (Exception e)
		{}

		return dd;
	}

	public DataDocument (Document doc)
	{
		try
		{
			id = doc.getObjectId ().toInteger ();
			title = doc.getTitle ();
			location = doc.getPublicLocation ();
			created = doc.getCreateDate ();

			author = doc.getAuthor ();
			referenceNumber = doc.getReferenceNumber ();

			documentCategory = new DataDocumentCategory (doc.getDocumentCategoryObjectId ().toInteger (), doc.getDocumentCategoryName ());
			documentStatusCode = new DataDocumentStatusCode (doc.getDocumentStatusCodeObjectId ().toInteger (), doc.getDocumentStatusCodeName ());

		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public DataDocument (Document doc, Integer documentType,
			Integer referenceActivity)
	{
		try
		{
			id = doc.getObjectId ().toInteger ();
			title = doc.getTitle ();
			location = doc.getPublicLocation ();
			created = doc.getCreateDate ();

			author = doc.getAuthor ();
			referenceNumber = doc.getReferenceNumber ();

			this.documentType = documentType;
			referenceProjectId = doc.getProjectObjectId ().toInteger ();

			if (documentType == DataDocument.DT_ACTIVITY)
				this.referenceActivity = referenceActivity;

			documentCategory = new DataDocumentCategory (doc.getDocumentCategoryObjectId ().toInteger (), doc.getDocumentCategoryName ());
			documentStatusCode = new DataDocumentStatusCode (doc.getDocumentStatusCodeObjectId () != null ? doc.getDocumentStatusCodeObjectId ().toInteger ()
					: null, doc.getDocumentStatusCodeName ());
		}
		catch (BusinessObjectException e)
		{
			PrimaveraConnector.writeLog (this.getClass ().getName () + " create failed.", e);
		}
	}

	public DataDocument (DataDocument doc)
	{
		id = doc.id;
		title = doc.title;
		location = doc.location;
		created = doc.created;
		author = doc.author;
		referenceNumber = doc.referenceNumber;
		documentType = doc.documentType;
		referenceProjectId = doc.referenceProjectId;

		documentCategory = doc.documentCategory;
		documentStatusCode = doc.documentStatusCode;

		if (documentType == DataDocument.DT_ACTIVITY)
			referenceActivity = doc.referenceActivity;
	}

	public Integer getId ()
	{
		return id;
	}

	public void setId (Integer id)
	{
		this.id = id;
	}

	public String getTitle ()
	{
		return title;
	}

	public void setTitle (String title)
	{
		this.title = title;
	}

	public String getLocation ()
	{
		return location;
	}

	public void setLocation (String location)
	{
		this.location = location;
	}

	public Date getCreated ()
	{
		return created;
	}

	public void setCreated (Date created)
	{
		this.created = created;
	}

	public String getAuthor ()
	{
		return author;
	}

	public void setAuthor (String author)
	{
		this.author = author;
	}

	/**
	 * Получает FileEntryId, относящийся к Document and Media Library
	 */
	public String getReferenceNumber ()
	{
		return referenceNumber;
	}

	/**
	 * Сохраняет FileEntryId, относящийся к Document and Media Library
	 */
	public void setReferenceNumber (String referenceNumber)
	{
		this.referenceNumber = referenceNumber;
	}

	public Integer getDocumentType ()
	{
		return documentType;
	}

	public void setDocumentType (Integer documentType)
	{
		this.documentType = documentType;
	}

	public Integer getReferenceActivity ()
	{
		return referenceActivity;
	}

	public void setReferenceActivity (Integer referenceActivity)
	{
		this.referenceActivity = referenceActivity;
	}

	public Integer getReferenceProjectId ()
	{
		return referenceProjectId;
	}

	public void setReferenceProjectId (Integer referenceProjectId)
	{
		this.referenceProjectId = referenceProjectId;
	}

	public DataDocumentCategory getDocumentCategory ()
	{
		return documentCategory;
	}

	public DataDocumentStatusCode getDocumentStatusCode ()
	{
		return documentStatusCode;
	}

	public void setDocumentCategory (DataDocumentCategory documentCategory)
	{
		this.documentCategory = documentCategory;
	}

	public void setDocumentStatusCode (DataDocumentStatusCode documentStatusCode)
	{
		this.documentStatusCode = documentStatusCode;
	}

	/**
	 * Создает документ в Primavera
	 * 
	 * @return
	 *         DocumentId созданного документа
	 */
	@Deprecated
	public Integer createInPrimavera ()
	{
		PrimaveraService ps = new PrimaveraService ();
		if (documentType.equals (DT_ACTIVITY))
			return ps.createProjectDocument (referenceProjectId, referenceActivity, title, author, location, documentCategory.getId (), documentStatusCode.getId (), referenceNumber);
		if (documentType.equals (DT_PROJECT))
			return ps.createDocument (referenceProjectId, title, author, location, documentCategory.getId (), documentStatusCode.getId (), referenceNumber);

		return null;
	}

	/**
	 * Создает или сохраняет документ в Primavera
	 * 
	 * @return id созданного или сохраненного документа. Если возникают ошибки, возвращает null
	 */
	public Integer saveInPrimavera ()
	{
		// передаем "себя" в качеcтве DataDocument в PrimaveraService
		PrimaveraService ps = new PrimaveraService ();
		// при создании документа ему будет присвоен id.
		ps.saveDocument (this);

		return this.id;
	}
}
