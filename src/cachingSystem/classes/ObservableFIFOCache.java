package cachingSystem.classes;

import dataStructures.classes.Pair;
/**
 * Class that adapts the FIFOCache class to the ObservableCache abstract class.
 */
public class ObservableFIFOCache<K, V> extends ObservableCache<K, V> {
    /* using the black box FIFOCache to store data */
    private FIFOCache<K, V> fifoCache = new FIFOCache<K, V>();
    /**
     *  Get the value for a given key.
     *  @param key the key used to get a value
     *  @return the value cached for the given key
     */
    public V get(K key) {
        V gottenValue = fifoCache.get(key); /* try to get the value */
        if (gottenValue == null) { /* if there wasn't a value stored for that key*/
            cacheListener.onMiss(key); /* trigger onMiss events so that it is added */
            return gottenValue; /* immediately return null */
        }
        cacheListener.onHit(key); /* if there was a key, trigger onHit events*/
        return gottenValue; /* return the value found */
    }
    /**
     *  put a pair of (key, value) inside the cache.
     *  @param key the key added
     *  @param value the value added
     */
    public void put(K key, V value) {
        cacheListener.onPut(key, value); /* trigger on put events */
        fifoCache.put(key, value); /* actually put the key */
        clearStaleEntries(); /* clear the stale entries */
    }
    /**
     *  get the cache's size.
     *  @return the size of the cache
     */
    public int size() {
        return fifoCache.size();
    }
    /**
     *  get the cache's empty status.
     *  @return the cache's empty status
     */
    public boolean isEmpty() {
        return fifoCache.isEmpty();
    }
    /**
     *  remove an element from the cache.
     *  @param key the key for the element we want to remove
     *  @return the value of the element removed from cache
     */
    public V remove(K key) {
        return fifoCache.remove(key);
    }
    /**
     * clear all elements from the cache.
     */
    public void clearAll() {
        fifoCache.clearAll();
    }
    /**
     *  get the eldest entry from the cache.
     *  @return the eldest element in LRUCache
     */
    public Pair<K, V> getEldestEntry() {
        return fifoCache.getEldestEntry();
    }

}
