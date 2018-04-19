package cachingSystem.classes;

import cachingSystem.interfaces.Cache;
import cachingSystem.interfaces.CacheStalePolicy;
import observerPattern.interfaces.CacheListener;
import dataStructures.classes.Pair;
import observerPattern.classes.BroadcastListener;
/**
 * Abstract class that adds support for listeners and stale element policies to the Cache
 * interface.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class ObservableCache<K, V> implements Cache<K, V> {
    protected CacheListener<K, V> cacheListener = new BroadcastListener<K, V>();
    protected CacheStalePolicy<K, V> stalePolicy;

    /**
     * Set a policy for removing stale elements from the cache.
     *
     * @param stalePolicy the stale policy that is set
     */
    public void setStalePolicy(CacheStalePolicy<K, V> stalePolicy) {
        this.stalePolicy = stalePolicy;
    }

    /**
     * Set a listener for the cache.
     *
     * @param cacheListener the listener that is set
     */
    public void setCacheListener(CacheListener<K, V> cacheListener) {
        this.cacheListener = cacheListener;
    }

    /**
     * Remove all those elements from cache that are stale according to the policy.
     */
    public void clearStaleEntries() {
        if (stalePolicy != null) { /*if there is a stale policy */
            Pair<K, V> eldest = getEldestEntry(); /*get the eldest key*/
            if (stalePolicy.shouldRemoveEldestEntry(eldest)) { /* and remove it if it is stale */
                remove(eldest.getKey());
            }
        }
    }

}
