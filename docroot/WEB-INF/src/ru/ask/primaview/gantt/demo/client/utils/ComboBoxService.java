package ru.ask.primaview.gantt.demo.client.utils;

import java.util.List;


import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class ComboBoxService<T extends IComboBoxItem> {

	public ComboBox<T> createComboBox(List<T> list){
		ListStore<T> store = new ListStore<T>(new ModelKeyProvider<T>() {

			@Override
			public String getKey(T item) {
				return String.valueOf(item.getValue());
			}
		});
		store.addAll(list);
		LabelProvider<T> labelProvider = new LabelProvider<T>() {

			@Override
			public String getLabel(T item) {
				return item.getName();
			}
		};

		ValueProvider<T, String> valueProvider = new ValueProvider<T, String>() {

			@Override
			public void setValue(T object, String value) {
				object.setName(value);
			}

			@Override
			public String getValue(T object) {
				return object.getName();
			}

			@Override
			public String getPath() {
				return null;
			}
		};

		ListView<T, String> listView = new ListView<T, String>(store, valueProvider);
		
		ComboBox<T> combo = new ComboBox<T>(store, labelProvider, listView);
		combo.setEditable(false);
		combo.setWidth(300);
		return combo;
	}
}
