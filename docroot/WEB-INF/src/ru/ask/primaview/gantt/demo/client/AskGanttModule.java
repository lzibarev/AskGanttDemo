package ru.ask.primaview.gantt.demo.client;

import java.util.ArrayList;
import java.util.List;

import ru.ask.primaview.gantt.demo.client.bacisgantt.PrimaveraGantt;
import ru.ask.primaview.gantt.demo.client.utils.ComboBoxItem;
import ru.ask.primaview.gantt.demo.client.utils.ComboBoxService;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;
import ru.ask.primaview.gantt.demo.shared.data.ProjectData;
import ru.ask.primaview.gantt.demo.shared.data.ScaleConstants;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
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
				showProjectList(list);
			}

			@Override
			public void onFailure(Throwable arg) {
				Container waitContainer = getWaitProjectListContainer("Проблема подключения к приме");
				RootPanel.get(GANTT_PARAMS).clear();
				RootPanel.get(GANTT_PARAMS).add(waitContainer);
			}
		});
	}

	private void showProjectList(List<ProjectData> list) {
		HorizontalLayoutContainer container = new HorizontalLayoutContainer();

		final ComboBox<ProjectData> combo = getProjectComboBox(list);

		ComboBox<ComboBoxItem> scale = getScaleContainer();

		TextButton button = getBuildGattButton(combo, scale);

		container.add(combo);
		container.add(scale);
		container.add(button);

		RootPanel.get(GANTT_PARAMS).add(container);
	}

	private ComboBox<ProjectData> getProjectComboBox(List<ProjectData> list) {
		ComboBoxService<ProjectData> service = new ComboBoxService<ProjectData>();
		ComboBox<ProjectData> combo = service.createComboBox(list);
		combo.setEditable(false);
		combo.setWidth(300);
		return combo;
	}

	private TextButton getBuildGattButton(final ComboBox<ProjectData> combo, final ComboBox<ComboBoxItem> scale) {
		TextButton button = new TextButton("Построить график");
		SelectHandler sh = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String projectIdStr = combo.getValue().getValue();
				String scaleStr = scale.getValue().getValue();
				final AutoProgressMessageBox box = new AutoProgressMessageBox("Запрос данных на сервере",
						"Это может занять некоторое время");
				box.setProgressText("...");
				box.auto();
				box.show();

				greetingService.getWbsDataList(projectIdStr, scaleStr, new AsyncCallback<GanttData>() {

					@Override
					public void onSuccess(GanttData data) {
						PrimaveraGantt gantt = new PrimaveraGantt(data);
						RootPanel.get(GANTT_PARAMS).clear();
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

	private ComboBox<ComboBoxItem> getScaleContainer() {
		List<ComboBoxItem> list = new ArrayList<ComboBoxItem>();
		list.add(new ComboBoxItem(ScaleConstants.MONTH, "По месяцам"));
		list.add(new ComboBoxItem(ScaleConstants.DAY, "По дням"));
		ComboBoxService<ComboBoxItem> service = new ComboBoxService<ComboBoxItem>();
		ComboBox<ComboBoxItem> combo = service.createComboBox(list);
		combo.setEditable(false);
		combo.setWidth(300);
		return combo;
	}

	private Container getWaitProjectListContainer(String name) {
		HorizontalLayoutContainer container = new HorizontalLayoutContainer();
		Label lable = new Label();
		lable.setText(name);
		lable.setWidth("300");
		container.add(lable);
		return container;
	}

}
