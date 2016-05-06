package ehc.util;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of ICache, in which an element is removed from cache during
 * get() when its time in cache from the last insertion to this get() operation
 * exceeds the cache time expiration limit. In such cases the element is removed
 * from cache and null is returned to client.
 * 
 * <b>NOTE:</b> This implementation is not synchronized. If used by multiple
 * threads, it must be synchronized externally (usually this is preferred) or
 * wrapped using iqa.util.Cache.synchronizedCache(ICache).
 */
public class TimeExpirationCache<K, V> implements ICache<K, V> {

	protected class Slot {
		long time;
		K key;
		V value;

		void free() {
			time = 0;
			key = null;
			value = null;
		}
	}

	protected Map<K, Slot> map;
	protected long timeLimit = 3600000; // one hour

	/**
	 * TimeExpirationCache constructor comment.
	 */
	public TimeExpirationCache() {
		this(37, 3600000);
	}

	/**
	 * TimeExpirationCache constructor comment.
	 */
	public TimeExpirationCache(int capacity, long timeLimit) {
		map = new HashMap<K, Slot>(capacity);
		this.timeLimit = timeLimit;
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public boolean containsValue(V value) {
		return map.containsValue(value);
	}

	protected void free(Slot slot) {
		map.remove(slot.key);
		slot.free();
	}

	public V get(K key) {
		V value = null;
		Slot slot = map.get(key);
		if (slot != null) {
			if (System.currentTimeMillis() - slot.time > timeLimit)
				free(slot);
			else
				value = slot.value;
		}
		return value;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public V put(K key, V value) {
		V previous = null;
		Slot slot = map.get(key);
		if (slot != null) {
			previous = slot.value;
			slot.value = value;
			slot.time = System.currentTimeMillis();
		} else {
			slot = new Slot();
			slot.key = key;
			slot.value = value;
			slot.time = System.currentTimeMillis();
			map.put(key, slot);
		}
		return previous;
	}

	public V remove(K key) {
		V value = null;
		Slot slot = (Slot) map.get(key);
		if (slot != null) {
			value = slot.value;
			free(slot);
		}
		return value;
	}

	public int size() {
		return map.size();
	}

}
