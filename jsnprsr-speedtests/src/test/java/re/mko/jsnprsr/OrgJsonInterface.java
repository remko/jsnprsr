package re.mko.jsnprsr;

import java.io.Reader;

import org.json.JSONArray;

public class OrgJsonInterface implements ParserializerInterface {

	@Override
	public Object parse(String s) throws JSONException {
		return new JSONArray(s);
	}

	@Override
	public Object parse(Reader reader) throws JSONException {
		assert false;
		return null;
	}

	@Override
	public String serialize(Object value) throws JSONException {
		return value.toString();
	}

}
