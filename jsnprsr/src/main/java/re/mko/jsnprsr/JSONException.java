/*
 * Copyright (c) 2013 Remko Tronçon
 * Licensed under the MIT license.
 * See COPYING file for more information.
 */

package re.mko.jsnprsr;

@SuppressWarnings("serial")
public final class JSONException extends Exception {
	public JSONException(Throwable e) {
		super(e);
	}

	public JSONException(String s, Throwable t) {
		super(s, t);
	}

	public JSONException(String s) {
		super(s);
	}
}
