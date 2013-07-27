package ru.ask.primaview.gantt.demo.client.bacisgantt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ask.primaview.gantt.demo.client.dummydata.DemoData2;
import ru.ask.primaview.gantt.demo.client.dummydata.DemoData3;
import ru.ask.primaview.gantt.demo.client.dummydata.Dependency;
import ru.ask.primaview.gantt.demo.client.dummydata.DependencyProps;
import ru.ask.primaview.gantt.demo.client.dummydata.IDemoData;
import ru.ask.primaview.gantt.demo.client.dummydata.Task;
import ru.ask.primaview.gantt.demo.client.dummydata.TaskProps;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;
import ru.ask.primaview.gantt.demo.shared.data.ScaleConstants;

import com.gantt.client.Gantt;
import com.gantt.client.config.GanttConfig;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.scheduler.client.core.TimeResolution.Unit;
import com.scheduler.client.core.config.SchedulerConfig.ResizeHandle;
import com.scheduler.client.core.timeaxis.TimeAxisGenerator;
import com.scheduler.client.core.timeaxis.preconfig.DayTimeAxisGenerator;
import com.scheduler.client.core.timeaxis.preconfig.MonthTimeAxisGenerator;
import com.scheduler.client.core.timeaxis.preconfig.WeekTimeAxisGenerator;
import com.scheduler.client.core.timeaxis.preconfig.YearTimeAxisGenerator;
import com.scheduler.client.zone.Line;
import com.scheduler.client.zone.LineProperties;
import com.scheduler.client.zone.ZoneGeneratorInt;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;

public class PrimaveraGantt implements IsWidget {

	private static final boolean showComplite = false;

	public PrimaveraGantt(GanttData data) {
		this.ganttData = data;
	}

	private GanttData ganttData;

	public interface GanttExampleStyle extends CssResource {
		String todayLineMain();
	}

	public interface GanttExampleResources extends ClientBundle {
		@Source({ "Gantt.css" })
		GanttExampleStyle css();
	}

	private static final GanttExampleResources resources = GWT.create(GanttExampleResources.class);

	private Gantt<Task, Dependency> gantt;
	private static final TaskProps props = GWT.create(TaskProps.class);
	private static final DependencyProps depProps = GWT.create(DependencyProps.class);
	ListStore<Task> taskStore;

	private TreeStore<Task> dataTaskStore;
	private ListStore<Dependency> dataDepStore;

	@Override
	public Widget asWidget() {
		// resources
		IDemoData data;
		if (ganttData != null)
			data = new DemoData3(ganttData);
		else
			data = new DemoData2();
		setData(data);

		GanttConfig config = new GanttConfig();
		// ColumnModel for left static columns
		config.leftColumns = createStaticColumns();
		ArrayList<TimeAxisGenerator> headers = getTimeHeaders();
		config.timeHeaderConfig = headers;
		// Enable task resize
		config.resizeHandle = ResizeHandle.NONE;
		// Enable dependency DnD
		config.dependencyDnDEnabled = false;
		// Only EndToStart allowed
		// config.dependencyDnDTypes = DependencyDnDConstants.ENDtoSTART ;

		// Enable task DnD
		config.taskDnDEnabled = false;
		// Define "snap to" resolution
		config.timeResolutionUnit = Unit.DAY;
		config.timeResolutionIncrement = 1;
		// Define the DateFormat of the tooltips
		config.tipDateFormat = DateTimeFormat.getFormat("MMM d");
		// Disable time toolTip
		config.timeTipEnabled = false;
		// Defines if the timeAxis columns should be autosized to fit.
		config.fitColumns = false;
		// Define column width
		config.columnWidth = 40;
		// Enable task contextMenu
		config.taskContextMenuEnabled = false;
		// Enable dependency contextMenu
		config.dependencyContextMenuEnabled = false;
		//
		config.taskProperties = props;
		config.dependencyProperties = depProps;

		// Cascade Changes
		config.cascadeChanges = false;
		config.zoneGenerators = getZones();

		// Create the Gxt Scheduler
		gantt = new GanttPrim(dataTaskStore, dataDepStore, config);
		gantt.setLineStore(createLines());
		// развернуть все
		// gantt.getLeftGrid().addViewReadyHandler(new ViewReadyHandler() {
		// @Override
		// public void onViewReady(ViewReadyEvent event) {
		// ((TreeGrid<Task>) gantt.getLeftGrid()).expandAll();
		// }
		// });

		setStartEnd();

		ContentPanel cp = new ContentPanel();
		cp.setHeadingText("Диаграмма ганта");
		// cp.getHeader().setIcon(ExampleImages.INSTANCE.table());
		cp.setPixelSize(1000, 460);
		cp.getElement().setMargins(new Margins(5));

		VerticalLayoutContainer vc = new VerticalLayoutContainer();
		cp.setWidget(vc);
		vc.add(gantt, new VerticalLayoutData(1, 1));
		return cp;
	}
	
	private List<ZoneGeneratorInt> getZones(){
		// Add zones to weekends
		ArrayList<ZoneGeneratorInt> zoneGenerators = new ArrayList<ZoneGeneratorInt>();
//		zoneGenerators.add(new WeekendZoneGenerator()); // Create a zone every
		return zoneGenerators;
	}
	
	// Creating the store containing the lines
    private ListStore<Line> createLines() {
		StyleInjectorHelper.ensureInjected(resources.css(), true);

        LineProperties lineProps = GWT.create(LineProperties.class);
        ListStore<Line> store = new ListStore<Line>(lineProps.key());
        // new Line(Date date,String tooltipText,String customCssStyle)
        String customCssStyle = resources.css().todayLineMain();
        Line line = new Line(new Date(), new Date().toString(), customCssStyle);
        store.add(line);
        return store;
    }
	
	private static class GanttPrim extends Gantt<Task, Dependency>{

		public GanttPrim(TreeStore<Task> taskStore, ListStore<Dependency> dependecyStore, GanttConfig config) {
			super(taskStore, dependecyStore, config);
		}
	
		@Override
		public Date getTaskEndDate(Task task) {
			return super.getTaskEndDate(task);
		}
		
	}
	
	private void setStartEnd(){
		DateWrapper dwStart = new DateWrapper(ganttData.getDateStart()).clearTime();
		DateWrapper dwFinish = new DateWrapper(ganttData.getDateFinish()).clearTime();
		int delta = 4;
		// Set start and end date.
		String scale = ganttData.getScale();
		if (scale.equals(ScaleConstants.DAY)) {
			gantt.setStartEnd(dwStart.addDays(-delta).asDate(), dwFinish.addDays(delta).asDate());		
		} else if (scale.equals(ScaleConstants.MONTH)) {
			gantt.setStartEnd(dwStart.addMonths(-delta).asDate(), dwFinish.addMonths(delta).asDate());		
		}
	}

	private ArrayList<TimeAxisGenerator> getTimeHeaders() {
		ArrayList<TimeAxisGenerator> headers = new ArrayList<TimeAxisGenerator>();
		String scale = ganttData.getScale();
		if (scale.equals(ScaleConstants.DAY)) {
			headers.add(new WeekTimeAxisGenerator("MMM d"));
			headers.add(new DayTimeAxisGenerator("EEE"));
		} else if (scale.equals(ScaleConstants.MONTH)) {
			headers.add(new YearTimeAxisGenerator("yyyy"));
			headers.add(new MonthTimeAxisGenerator("MMM"));
		}
		return headers;
	}

	private void setData(IDemoData data) {
		dataTaskStore = new TreeStore<Task>(props.key());
		Task root = data.getTasks();
		for (Task base : root.getChildren()) {
			dataTaskStore.add(base);
			if (base.hasChildren()) {
				processFolder(dataTaskStore, base);
			}
		}

		dataDepStore = new ListStore<Dependency>(depProps.key());
		dataDepStore.addAll(data.getDependencies());
	}

	// Creates the static columns
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ColumnModel<Task> createStaticColumns() {
		List<ColumnConfig<Task, ?>> configs = new ArrayList<ColumnConfig<Task, ?>>();

		ColumnConfig<Task, ?> column = new ColumnConfig<Task, String>(props.name());
		column.setHeader("Работа");
		column.setWidth(160);
		column.setSortable(true);
		column.setResizable(true);
		configs.add(column);

		ColumnConfig<Task, Date> column2 = new ColumnConfig<Task, Date>(props.startDateTime());
		column2.setHeader("Начало");
		column2.setWidth(90);
		column2.setSortable(true);
		column2.setResizable(true);
		column2.setCell(new DateCell(DateTimeFormat.getFormat("yyyy-MM-dd")));
		configs.add(column2);

		ColumnConfig<Task, Integer> column3 = new ColumnConfig<Task, Integer>(props.duration());
		column3.setHeader("Продолж.");
		column3.setWidth(70);
		column3.setSortable(true);
		column3.setResizable(true);
		configs.add(column3);

		if (showComplite) {
			ColumnConfig<Task, Integer> column4 = new ColumnConfig<Task, Integer>(props.percentDone());
			column4.setHeader("Вып. %");
			column4.setWidth(60);
			column4.setSortable(true);
			column4.setResizable(true);
			configs.add(column4);
		}

		ColumnModel cm = new ColumnModel(configs);
		cm.addHeaderGroup(0, 0, new HeaderGroupConfig("Описание работ", 1, configs.size()));

		return cm;
	}

	private void processFolder(TreeStore<Task> store, Task folder) {
		for (Task child : folder.getChildren()) {
			store.add(folder, child);
			if (child.hasChildren()) {
				processFolder(store, child);
			}
		}
	}

}
