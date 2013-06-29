package ru.ask.primaview.gantt.demo.server.prima.utility;

import java.io.Serializable;

public class DataKeyValuePair implements Serializable
{
	private static final long	serialVersionUID	= 7673146557529121907L;
	private String				_key;
	private String				_value;

	public DataKeyValuePair ()
	{
		set_key ("");
		set_value ("");
	}

	public DataKeyValuePair (String key, String val)
	{
		set_key (key);
		set_value (val);
	}

	public String get_key ()
	{
		return _key;
	}

	public void set_key (String _key)
	{
		this._key = _key;
	}

	public String get_value ()
	{
		return _value;
	}

	public void set_value (String _value)
	{
		this._value = _value;
	}
}
