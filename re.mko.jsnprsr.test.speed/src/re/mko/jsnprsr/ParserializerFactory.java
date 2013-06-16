package re.mko.jsnprsr;

public final class ParserializerFactory {
	private ParserializerFactory() {
	}
	
	public static ParserializerInterface create(String name) {
		if ("jsnprsr".equals(name)) {
			return new JsnPrsrInterface();
		}
		else if ("minimal-json".equals(name)) {
			return new MinimalJSONInterface();
		}
		else if ("org.json".equals(name)) {
			return new OrgJsonInterface();
		}
		else {
			throw new RuntimeException("Illegal parse library: " + name);
		}
	}
}
