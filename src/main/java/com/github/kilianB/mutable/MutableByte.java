package com.github.kilianB.mutable;

import java.io.Serializable;

/**
 * Mutable class wrapper for byte values. Mutable classes are useful
 * in lambda expressions or anonymous classes which want to alter the content of
 * a variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class MutableByte implements Mutable<Byte>, Comparable<MutableByte>, Serializable  {
	
	private static final long serialVersionUID = -1973847474259823325L;
	
	private byte field;
	
	/**
	 * Create a mutable Boolean with an initial value of 0
	 */
	public MutableByte() {};
	
	/**
	 * Create a mutable Boolean 
	 * @param initialValue the intial value of the byte 
	 */
	public MutableByte(byte initialValue) {
		this.field = initialValue;
	}
	
	@Override
	public int compareTo(MutableByte o) {
		return Byte.compare(field,o.field);
	}

	@Override
	public Byte getValue() {
		return Byte.valueOf(field);
	}

	@Override
	public void setValue(Byte newValue) {
		this.field = newValue;
	}
	
	/**
	 * @return the current value as byte primitive
	 */
	public byte byteValue() {
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
		MutableByte other = (MutableByte) obj;
		if (field != other.field)
			return false;
		return true;
	}
	
	

}
