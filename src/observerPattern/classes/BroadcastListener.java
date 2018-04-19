package observerPattern.classes;

import observerPattern.interfaces.CacheListener;
import java.util.LinkedList;
/**
 * The BroadcastListener broadcasts cache events to other listeners that have been added to it.
 */
public class BroadcastListener<K, V> implements CacheListener<K, V> {

    private LinkedList<CacheListener<K, V>> listenersList = new LinkedList<>();

    /**
     * Add a listener to the broadcast list.
     *
     * @param listener the listener
     */
    public void addListener(CacheListener<K, V> listener) {
        listenersList.add(listener);
    }
    /**
     * Trigger onHit events for all the listeners.
     *
     * @param key the key that was hit
     */
    public void onHit(K key) {
        for (CacheListener<K, V> cl : listenersList) {
            cl.onHit(key);
        }
    }
    /**
     * Trigger onMiss events for all the listeners.
     *
     * @param key the key that was missed
     */
    public void onMiss(K key) {
        for (CacheListener<K, V> cl : listenersList) {
            cl.onMiss(key);
        }
    }
    /**
     * Trigger onPut events for all the listeners.
     *
     * @param key the key that was put
     * @param value the value that was put
     */
    public void onPut(K key, V value) {
        for (CacheListener<K, V> cl : listenersList) {
            cl.onPut(key, value);
        }
    }
}
