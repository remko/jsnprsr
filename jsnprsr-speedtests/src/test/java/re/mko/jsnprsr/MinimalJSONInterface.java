package re.mko.jsnprsr;

import java.io.IOException;
import java.io.Reader;

import com.eclipsesource.json.JsonValue;

public class MinimalJSONInterface implements ParserializerInterface {

	@Override
	public Object parse(String s) throws IOException {
		return JsonValue.readFrom(s);
	}

	@Override
	public Object parse(Reader r) throws IOException {
		return JsonValue.readFrom(r);
	}
	
	@Override
	public String serialize(Object value) throws IOException {
		return ((JsonValue) value).toString();
	}

}
