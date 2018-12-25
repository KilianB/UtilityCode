package com.github.kilianB.datastructures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A bidirectional map allowing of O(1) for key and value lookups.
 * 
 * This ds uses 2 maps internally. To ensure consistent stage throughout both 
 * maps keys and vlaues are assumed to be unique. 
 * 
 * 
 * <table>
 * 		<tr>	<th>Input</th>	<th>Map0</th>	<th>Map1</th>	</tr>
 * 		<tr>	<td>0,2</td>	<td>0 -> 2</td>	<td>2 -> 0</td>		</tr>
 * 		<tr>	<td>3,4</td>	<td>3 -> 4</td>	<td>4 -> 3</td>		</tr>
 * 		<tr>	<td>1,2</td>	<td>1 -> 2</td>	<td>2 -> 1</td>		</tr>
 * </table>
 * After the third insert both maps are out of synch
 * 
 * In order to keep consistent st to the constraint keys and vlaues have to be unique!
 * 
 * Not ready yet ...
 * https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/BidiMap.html
 * @author Kilian
 *
 */
@Deprecated
public class BiMap<K,K1> implements Map<K,K1>{

	/**
	 * Map
	 */
	protected Map<K,K1> map = new HashMap<K,K1>();
	/**
	 * Reverse map
	 */
	protected Map<K1,K> rMap = new HashMap<K1,K>();
	
	
	@Override
	public int size() {
		assert map.size() == rMap.size();
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		assert map.isEmpty() == rMap.isEmpty();
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return rMap.containsKey(value);
	}

	@Override
	public K1 get(Object key) {
		return map.get(key);
	}

	@Override
	public K1 put(K key, K1 value) {
		
		//TODO
		map.put(key,value);
		
		if(rMap.containsKey(value)) {
			//This will ceate an error.
		}
		
		rMap.put(value,key);
		return value;
	}

	@Override
	public K1 remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends K1> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<K1> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<K, K1>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
