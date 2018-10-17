package com.github.kilianB.mutable;

import java.io.Serializable;

/**
 * Mutable class wrapper for long values. Mutable classes are useful
 * in lambda expressions or anonymous classes which want to alter the content of
 * a variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class MutableLong implements Mutable<Long>, Comparable<MutableLong>, Serializable{

	private static final long serialVersionUID = 6846548022746719522L;
	
	private long field;
	
	/**
	 * Create a mutable Long with an initial value of 0L
	 */
	public MutableLong() {};
	
	/**
	 * Create a mutable Long.
	 * @param initialValue the initial value of the long
	 */
	public MutableLong(long initialValue) {
		this.field = initialValue;
	}
	
	@Override
	public int compareTo(MutableLong o) {
		return Long.compare(field,o.field);
	}

	@Override
	public Long getValue() {
		return Long.valueOf(field);
	}

	@Override
	public void setValue(Long newValue) {
		field = newValue;
	}
	
	/**
	 * @return the current value as long primitive
	 */
	public long longValue() {
		return field;
	}

	@Override
	public int hashCode() {
		return (int) (field ^ (field >>> 32));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableLong other = (MutableLong) obj;
		if (field != other.field)
			return false;
		return true;
	}
	
}
