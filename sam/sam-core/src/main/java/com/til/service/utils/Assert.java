/**
 * 
 */
package com.til.service.utils;

/**
 * @author Umesh.Hingle
 * 
 */
public class Assert {
	static final public void notFalse(boolean b)
			throws IllegalArgumentException {
		if (b == false)
			throw new IllegalArgumentException("boolean expression false");
	}

	static final public void notNull(Object obj)
			throws IllegalArgumentException {
		if (obj == null)
			throw new IllegalArgumentException("null argument");
	}

	static final public void notFalse(boolean b, String s)
			throws IllegalArgumentException {
		if (b == false)
			throw new IllegalArgumentException(s);
	}

	static final public void notNull(Object obj, String s)
			throws IllegalArgumentException {
		if (obj == null)
			throw new IllegalArgumentException(s);
	}
}
