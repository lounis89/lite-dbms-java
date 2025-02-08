package bdda;

import java.util.ArrayList;

public class Record {
	private ArrayList<String> values = new ArrayList<>();

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

	public void addValue(String value) {
		values.add(value);
	}

	public ArrayList<String> getValues() {
		return values;
	}
}
