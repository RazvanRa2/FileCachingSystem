package cachingSystem.classes;

import java.sql.Timestamp;
import dataStructures.classes.TimeAwareNode;
import dataStructures.classes.Pair;
import cachingSystem.interfaces.CacheStalePolicy;
/**
 * The TimeAwareCache offers the same functionality as the LRUCache, but also stores a timestamp for
 * each element. The timestamp is updated after each get / put operation for a key. This
 * functionality allows for time based cache stale policies (e.g. removing entries that are older
 * than 1 second).
 */
public class TimeAwareCache<K, V> extends LRUCache<K, V> {

    @Override
    public V get(K key) {
         /**
          * only difference from LRUCAche is one needs to check for stale entries
          * before doing a get operation
          */
        clearStaleEntries();

        V gottenValue = null;

        if (cacheMap.get(key) == null) {
            cacheListener.onMiss(key);
        } else {
            TimeAwareNode<Pair<K, V>> tempNode = cacheList.remove(cacheMap.get(key));
            cacheList.push(tempNode);

            gottenValue = cacheMap.get(key).getData().getValue();

            cacheListener.onHit(key);
        }
        return gottenValue;
    }

    /**
     * Get the timestamp associated with a key, or null if the key is not stored in the cache.
     *
     * @param key the key
     * @return the timestamp, or null
     */
    public Timestamp getTimestampOfKey(K key) {
        return cacheMap.get(key).getTimestamp();
    }

    /**
     * Set a cache stale policy that should remove all elements older than @millisToExpire
     * milliseconds. This is a convenience method for setting a time based policy for the cache.
     *
     * @param millisToExpire the expiration time, in milliseconds
     */
    public void setExpirePolicy(long millisToExpire) {
        setStalePolicy(new CacheStalePolicy<K, V>() { /*set the policy as described here */
            @Override
            public boolean shouldRemoveEldestEntry(Pair<K, V> entry) {
                if (entry != null) { /* if an entry is passed */
                    /* get its time, check if it's stale, return true if it is, false if not */
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    Timestamp maxExpireTime = new Timestamp(currentTime.getTime() - millisToExpire);
                    /* below line traslates as: return true if timestamp of key is not after max */
                    return !getTimestampOfKey(entry.getKey()).after(maxExpireTime);
                }
                return false;
            }
        });
    }
}
