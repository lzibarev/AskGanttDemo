package ru.ask.primaview.gantt.demo.client;

import java.util.List;

import ru.ask.primaview.gantt.demo.client.bacisgantt.PrimaveraGantt;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;
import ru.ask.primaview.gantt.demo.shared.data.ProjectData;
import ru.ask.primaview.gantt.demo.shared.data.ScaleConstants;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class AskGanttModule implements EntryPoint {

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private static final String GANTT_PARAMS = "ganttParams";
	private static final String GANTT_DEMO = "ganttDemo";

	public void onModuleLoad() {
		Container waitContainer = getWaitProjectListContainer("Ожидание загрузки...");
		RootPanel.get(GANTT_PARAMS).add(waitContainer);

		getProjectList();
	}

	private void getProjectList() {
		greetingService.getProjectsList(new AsyncCallback<List<ProjectData>>() {

			@Override
			public void onSuccess(List<ProjectData> list) {
				fillProjectList(list);
			}

			@Override
			public void onFailure(Throwable arg) {
				Container waitContainer = getWaitProjectListContainer("Проблема подключения к приме");
				RootPanel.get(GANTT_PARAMS).clear();
				RootPanel.get(GANTT_PARAMS).add(waitContainer);
			}
		});
	}

	private void fillProjectList(List<ProjectData> list) {
		HorizontalLayoutContainer container = new HorizontalLayoutContainer();

		final ComboBox<ProjectData> combo = getProjectComboBox(list);


		container.add(combo);

		TextButton button = getBuildGattButton(combo);
		
		container.add(button);

		RootPanel.get(GANTT_PARAMS).add(container);
	}
	
	private ComboBox<ProjectData> getProjectComboBox(List<ProjectData> list){
		ListStore<ProjectData> store = new ListStore<ProjectData>(new ModelKeyProvider<ProjectData>() {

			@Override
			public String getKey(ProjectData item) {
				return String.valueOf(item.getValue());
			}
		});
		store.addAll(list);
		LabelProvider<ProjectData> labelProvider = new LabelProvider<ProjectData>() {

			@Override
			public String getLabel(ProjectData item) {
				return item.getName();
			}
		};

		ValueProvider<ProjectData, String> valueProvider = new ValueProvider<ProjectData, String>() {

			@Override
			public void setValue(ProjectData object, String value) {
				object.setName(value);
			}

			@Override
			public String getValue(ProjectData object) {
				return object.getName();
			}

			@Override
			public String getPath() {
				return null;
			}
		};

		ListView<ProjectData, String> listView = new ListView<ProjectData, String>(store, valueProvider);
		
		ComboBox<ProjectData> combo = new ComboBox<ProjectData>(store, labelProvider, listView);
		combo.setEditable(false);
		combo.setWidth(300);
		return combo;
	}

	private TextButton getBuildGattButton(final ComboBox<ProjectData> combo){
		TextButton button = new TextButton("Построить график");
		SelectHandler sh = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				int projectId = combo.getValue().getValue();
				final AutoProgressMessageBox box = new AutoProgressMessageBox("Запрос данных на сервере",
						"Это может занять некоторое время");
				box.setProgressText("...");
				box.auto();
				box.show();

				greetingService.getWbsDataList(projectId, ScaleConstants.MONTH, new AsyncCallback<GanttData>() {

					@Override
					public void onSuccess(GanttData data) {
						PrimaveraGantt gantt = new PrimaveraGantt(data);
						RootPanel.get(GANTT_DEMO).clear();
						RootPanel.get(GANTT_DEMO).add(gantt);
						box.hide();
					}

					@Override
					public void onFailure(Throwable arg0) {
						box.hide();
						AlertMessageBox alert = new AlertMessageBox("Ошибка",
								"Проблема при получении информации о проекте");
						alert.show();
					}
				});
			}
		};
		button.addSelectHandler(sh);
		return button;
	}
	
	private Container getWaitProjectListContainer(String name) {
		HorizontalLayoutContainer container = new HorizontalLayoutContainer();
		Label lable = new Label();
		lable.setText(name);
		container.add(lable);
		return container;
	}

}
