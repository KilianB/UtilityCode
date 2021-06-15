package com.github.kilianB.mutable;

/**
 * * Mutable class wrapper for objects. Mutable classes are useful in lambda
 * expressions or anonymous classes which want to alter the content of a
 * variable but are limited to final or effective final variables.
 * 
 * * Be aware that the hashcode changes if the value is updated resulting in the
 * object not being retrievable in hash collections.
 * 
 * @author Kilian
 * @since 1.5.10 com.github.kilianB
 */
public class MutableObject<T> implements Mutable<T> {

	private T reference;

	public MutableObject() {

	}

	public MutableObject(T initialValue) {
		reference = initialValue;
	}

	@Override
	public T getValue() {
		return reference;
	}

	@Override
	public void setValue(T newValue) {
		reference = newValue;
	}

	@Override
	public int hashCode() {
		return reference.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableObject other = (MutableObject) obj;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

}
