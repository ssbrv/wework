package cz.cvut.fit.sabirdan.wework.utils;

import java.util.HashMap;
import java.util.Map;

public class GreatestStaysMap<K, E extends Comparable<E>> {
    private final Map<K, E> map = new HashMap<>();
    public void put(K key, E element) {
        map.compute(key, (k, existingElement) -> {
            if (existingElement == null)
                return element;

            if (existingElement.compareTo(element) >= 0)
                return existingElement;

            return element;
        });
    }
    public E get(K key) {
        return map.get(key);
    }
    
    public Map<K, E> getAll() {
        return map;
    }
}
