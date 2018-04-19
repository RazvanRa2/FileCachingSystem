package cachingSystem.classes;

import dataStructures.classes.TaDoublyLinkedList;
import dataStructures.classes.TimeAwareNode;
import dataStructures.classes.Pair;
import java.util.HashMap;
/**
 * This cache is very similar to the FIFOCache, but guarantees O(1)
 * complexity
 * for the get, put and
 * remove operations.
 */
public class LRUCache<K, V> extends ObservableCache<K, V> {

    /**
     *  HashMap and DLL are both used in order to grant O(1) get/put operations.
     *  Their "nodes" point to the same memory area.
     */
    protected HashMap<K, TimeAwareNode<Pair<K, V>>> cacheMap =
    new HashMap<K, TimeAwareNode<Pair<K, V>>>();;
    protected TaDoublyLinkedList<Pair<K, V>> cacheList = new TaDoublyLinkedList<Pair<K, V>>();;

    /**
     * Get a value from cache using a key. If the key isn't found, onMiss events are triggered.
     * @param key the key used to get a value
     * @return the value that we got for that key
     */
    public V get(K key) {
        V gottenValue = null; /* create a refference to the value one wants to get */

        if (cacheMap.get(key) == null) { /* if there's no value in the cache for that key */
            cacheListener.onMiss(key); /* trigger onMiss events so that it is added */
        } else { /* if there is a value for that key */
            /* mark it as the latest used key */
            TimeAwareNode<Pair<K, V>> tempNode = cacheList.remove(cacheMap.get(key));
            cacheList.push(tempNode);
            /* get it's value */
            gottenValue = cacheMap.get(key).getData().getValue();
            /* trigger onHit events */
            cacheListener.onHit(key);
        }
        return gottenValue; /* return the gotten value */
    }
    /**
     *  Put a (key, value) pair in the cache memory.
     *  @param key the key put in the cache
     *  @param value the value put in the cache
     */

    public void put(K key, V value) {
        if (cacheMap.get(key) == null) { /*if there was no data in cache for that key*/
            /* add the (key, value) pair in memory as a new pair*/
            TimeAwareNode<Pair<K, V>> newNode = new TimeAwareNode(new Pair<K, V>(key, value));
            cacheMap.put(key, cacheList.push(newNode));
        } else { /* otherwise*/
            /* if for that key the same value existed*/
            if (value.equals(cacheMap.get(key).getData().getValue())) {
                /*only mark it as the latest used */
                TimeAwareNode<Pair<K, V>> tempNode = cacheList.remove(cacheMap.get(key));
                cacheList.push(tempNode);
            } else { /* otherwise set the new value, then mark it*/
                TimeAwareNode<Pair<K, V>> tempNode = cacheList.remove(cacheMap.get(key));
                Pair<K, V> tempPair = tempNode.getData();
                tempPair.setValue(value);

                cacheList.push(tempNode);
            }
        }
        /* after adding, trigger onPut events */
        cacheListener.onPut(key, value);
        /* and clear those entries that are stale */
        clearStaleEntries();
    }
    /**
     * get the size of the cache memory.
     * @return the cache's size
     */
    public int size() {
        return cacheList.size();
    }
    /**
     * return if the cache memory is empty or not.
     * @return the empty status of the cache
     */
    public boolean isEmpty() {
        return cacheList.isEmpty();
    }
    /**
     * remove an element from the memory using it's key.
     * @param key the key of the element that will be removed
     * @return the value for the removed key
     */
    public V remove(K key) {
        if (key != null) {  /* if there is a key to be removed */
            /* then remove it and return it's value */
            V returnValue = cacheMap.get(key).getData().getValue();
            cacheList.remove(cacheMap.get(key));
            cacheMap.remove(key);
            return returnValue;
        }
        return null; /* if there isn't, return null */
    }
    /**
     *  remove all elements from the cache.
     */
    public void clearAll() {
        cacheList.clearAll();
        cacheMap.clear();
    }
    /**
     *  return the oldest pair of (key,value) from cache.
     *  @return the oldest pair
     */
    public Pair<K, V> getEldestEntry() {
        if (isEmpty()) { /* if the list is empty, just return null */
            return null;
        }
        /* if it isn't, the last element in the list is the one least recently used */
        return cacheList.getLastData();
    }
}
