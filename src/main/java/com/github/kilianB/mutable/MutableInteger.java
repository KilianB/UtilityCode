package com.github.kilianB.mutable;

import java.io.Serializable;

/**
 * Mutable class wrapper for integer values. Mutable classes are useful
 * in lambda expressions or anonymous classes which want to alter the content of
 * a variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class MutableInteger implements Mutable<Integer>, Comparable<MutableInteger>, Serializable{

	private static final long serialVersionUID = 6846548022746719522L;
	
	private int field;
	
	/**
	 * Create a mutable Integer with an initial value of 0
	 */
	public MutableInteger() {};
	
	/**
	 * Create a mutable Integer.
	 * @param initialValue the initial value of the integer
	 */
	public MutableInteger(int initialValue) {
		this.field = initialValue;
	}
	
	@Override
	public int compareTo(MutableInteger o) {
		return Integer.compare(field,o.field);
	}

	@Override
	public Integer getValue() {
		return Integer.valueOf(field);
	}

	@Override
	public void setValue(Integer newValue) {
		field = newValue;
	}
	
	/**
	 * @return the current value as int primitive
	 */
	public int intValue() {
		return field;
	}

	@Override
	public int hashCode() {
		return field;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableInteger other = (MutableInteger) obj;
		if (field != other.field)
			return false;
		return true;
	}
	
	

}
