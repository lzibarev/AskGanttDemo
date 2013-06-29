package ru.ask.primaview.gantt.demo.server.prima;

import java.text.SimpleDateFormat;
import java.util.Date;

// TODO : вынести эти константы в файл настроек
public final class PrimaveraServiceUtils
{
	// Портфели проектов
	public static String			portfolioListWhere				= null;
	public static String			portfolioListOrder				= "Name Asc";

	// Проекты портфеля "Список проектов"
	public static int				projectsListPortfolio			= 159;
	public static String			projectsListWhere				= null;
	public static String			projectsListOrder				= "Name asc";

	public static String			wbsActivityWhere				= "not (UDFInteger$221=1)";
	public static String			wbsActivityOrder				= "Id asc";

	public static String			resourcesWhere					= "ResourceType='RT_Labor'";
	public static String			resourcesOrder					= "Name asc";

	public static String			wbsWhere						= "not (UDFInteger$240=1)";
	public static String			wbsOrder						= "Code asc";

	// категории документов
	public static String			dcOrder							= "Name asc";

	public static String			notesTableRowTemplate			= "<tr><td>\r\n\t<h2>Добавлено: <b>%s</b> Автор: <b>%s</b></h2>\r\n\t<p>%s</p>\r\n</td></tr>\r\n";
	public static String			notesTableRowSimpleTemplate		= "<tr><td>\r\n\t%s\r\n</td></tr>\r\n";
	public static String			notesTableConversionTemplate	= "<table>%s</table>";

	/**
	 * Формат даты 25.01.2012.
	 */
	public static SimpleDateFormat	dateFormatDDdMMdYYYY			= new SimpleDateFormat ("dd.MM.yyyy");

	/**
	 * Форматирует дату date в формат 25.01.2012
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormatDDdMMdYYYY (Date date)
	{
		if (date == null)
			return null;

		return dateFormatDDdMMdYYYY.format (date);
	}

	/**
	 * Форматирует дату date в формат 25.01.2012. Если date==null, возвращает пустую строку. efn - empty for null
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormatDDdMMdYYYYefn (Date date)
	{
		if (date == null)
			return "";

		return dateFormatDDdMMdYYYY.format (date);
	}
}
