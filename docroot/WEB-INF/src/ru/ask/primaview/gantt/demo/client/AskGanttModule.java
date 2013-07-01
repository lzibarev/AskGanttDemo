package ru.ask.primaview.gantt.demo.client;

import ru.ask.primaview.gantt.demo.client.bacisgantt.PrimaveraGantt;
import ru.ask.primaview.gantt.demo.shared.data.GanttData;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AskGanttModule implements EntryPoint {

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public void onModuleLoad() {
		Container idInput = getInputIdContainer();
		RootPanel.get("ganttParams").add(idInput);
	}

	private Container getInputIdContainer() {
		HorizontalLayoutContainer container = new HorizontalLayoutContainer();
		final TextField idText = new TextField();
		idText.setText("561");
		container.add(new FieldLabel(idText, "Введите ид проекта"));
		final CheckBox offlineCheckBox = new CheckBox();
		offlineCheckBox.setBoxLabel("offline");
		offlineCheckBox.setValue(true);
		container.add(offlineCheckBox);
		TextButton button = new TextButton("Построить график");
		SelectHandler sh = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				String projectIdStr = idText.getText();
				boolean offline = offlineCheckBox.getValue();
				greetingService.getWbsDataList(projectIdStr, offline, new AsyncCallback<GanttData>() {

					@Override
					public void onSuccess(GanttData data) {
						PrimaveraGantt gantt = new PrimaveraGantt(data);
						RootPanel.get("ganttDemo").clear();
						RootPanel.get("ganttDemo").add(gantt);
					}

					@Override
					public void onFailure(Throwable arg0) {
						AlertMessageBox alert = new AlertMessageBox("Ошибка",
								"Проблема при получении информации о проекте");
						alert.show();
					}
				});
			}
		};
		button.addSelectHandler(sh);
		container.add(button);
		return container;
	}

}
