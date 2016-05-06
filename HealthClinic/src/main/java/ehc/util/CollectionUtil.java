package ehc.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

public class CollectionUtil {

	public static <T> Vector<T> toVector(Enumeration<T> enumeration) {
		return (Vector<T>) copyElements(enumeration, new Vector<T>());
	}


	/**
	 * @Deprecated See {@link Collections#list(Enumeration<T>)}
	 */
	public static <T> List<T> toList(Enumeration<T> enumeration) {
		return (List<T>) copyElements(enumeration, new ArrayList<T>());
	}


	public static <T> Set<T> toSet(Enumeration<T> enumeration) {
		return (Set<T>) copyElements(enumeration, new HashSet<T>());
	}


	public static <T> Collection<T> copyElements(Enumeration<T> source, Collection<T> destination) {
		while (source != null && source.hasMoreElements()) {
			destination.add(source.nextElement());
		}
		return destination;
	}

	public static class EmptyEnumeration<T> implements Enumeration<T> {
		public boolean hasMoreElements() {
			return false;
		}


		public T nextElement() {
			throw new NoSuchElementException();
		}
	};

	public static final Enumeration<Object> EMPTY_ENUMERATION = new EmptyEnumeration<Object>();


	@SuppressWarnings("unchecked")
	public static <T> Enumeration<T> emptyEnumeration() {
		return (Enumeration<T>) EMPTY_ENUMERATION;
	}


	@SuppressWarnings("unchecked")
	public static <T> Enumeration<T> cast(Enumeration<?> e) {
		return (Enumeration<T>) e;
	}


	public static <T> List<T> newList(T... elements) {
		List<T> l = new ArrayList<T>();
		for (T element : elements)
			l.add(element);
		return l;
	}


	public static <T> List<T> newList(T element) {
		List<T> l = new ArrayList<T>();
		l.add(element);
		return l;
	}


	public static <T> List<T> newList() {
		return new ArrayList<T>();
	}


	public static <K, V> Map<K, V> newMap() {
		return new HashMap<K, V>();
	}


	public static <T> Set<T> copy(Set<T> set) {
		return new HashSet<T>(set);
	}


	public static <T> List<T> copy(List<T> list) {
		return new ArrayList<T>(list);
	}


	public static <T> List<T> concat(List<T> list1, List<T> list2) {
		List<T> result = new ArrayList<T>();
		result.addAll(list1);
		result.addAll(list2);
		return result;
	}

	public static <T> List<T>[] split(List<T> list, T element) {
		if (!list.contains(element))
			throw new IllegalArgumentException("Separator element not in list.");
		List<T> pre = new ArrayList<T>();
		List<T> post = new ArrayList<T>();
		int index = list.indexOf(element);
		if (index > 0)
			pre.addAll(list.subList(0, index));
		if (index < list.size()-1)
			post.addAll(list.subList(index+1, list.size()));
		return new List[] {pre, post};
	}
	
	public static <T> T last(Collection<T> collection) {
		T last = null;
		for (T item : collection)
			last = item;
		return last;
	}

	// Could do a concat(List<T>... lists) but then the compiler is unhappy with mixing generics and varargs.
	public static <T> List<T> concat(List<T> list1, List<T> list2, List<T> list3) {
		List<T> result = new ArrayList<T>();
		result.addAll(list1);
		result.addAll(list2);
		result.addAll(list3);
		return result;
	}
	
	public static <T> T[] concat(Class<T> clazz, T[] ... params) {
	    int size = 0;
	    for (T[] array : params) {
	      size += array.length;
	    }

	    T[] result = (T[]) Array.newInstance(clazz, size);
	    
	    int j = 0;
	    for (T[] array : params) {
	      for (T s : array) {
	        result[j++] = s;
	      }
	    }
	    return result;
	  }


	public static <T> Iterable<T> reverse(List<T> list) {
		return new ReverseIterable<T>(list);
	}


	public static <T> ListIterator<T> reverseIterator(List<T> list) {
		return new ReverseListIterator<T>(list);
	}

	public static class ReverseIterable<T> implements Iterable<T> {

		private List<T> list;


		public ReverseIterable(List<T> list) {
			this.list = list;
		}



		public Iterator<T> iterator() {
			return new ReverseListIterator<T>(list);
		}
	}

	public static class ReverseListIterator<T> implements ListIterator<T> {

		private List<T> list;
		private ListIterator<T> listIterator;


		public ReverseListIterator(List<T> list) {
			this.list = list;
			this.listIterator = list.listIterator(list.size());
		}



		public boolean hasNext() {
			return listIterator.hasPrevious();
		}



		public T next() {
			return listIterator.previous();
		}


	
		public boolean hasPrevious() {
			return listIterator.hasNext();
		}


		
		public T previous() {
			return listIterator.next();
		}


		
		public int nextIndex() {
			return list.size() - listIterator.previousIndex() - 1;
		}


		
		public int previousIndex() {
			return list.size() - listIterator.nextIndex() - 1;
		}



		public void remove() {
			listIterator.remove();
		}


		
		public void set(T e) {
			listIterator.set(e);
		}


		
		public void add(T e) {
			listIterator.add(e);
		}
	}


	public static List<Integer> asList(final int[] is) {
		return new AbstractList<Integer>() {
			@Override
			public Integer get(int i) {
				return is[i];
			}


			@Override
			public int size() {
				return is.length;
			}
		};
	}

}
