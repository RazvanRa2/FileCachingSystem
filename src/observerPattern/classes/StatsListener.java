package observerPattern.classes;

import observerPattern.interfaces.CacheListener;

/**
 * The StatsListener collects hit / miss / update stats for a cache.
 *
 * @param <K>
 * @param <V>
 */
public class StatsListener<K, V> implements CacheListener<K, V> {
    private int hits;
    private int misses;
    private int updates;

    public StatsListener() {
        this.hits = 0;
        this.misses = 0;
        this.updates = 0;
    }
    /**
     * increment the number of total hits.
     */
    public void onHit(K key) {
        this.hits++;
    }
    /**
     * increment the number of total misses.
     */
    public void onMiss(K key) {
        this.misses++;
    }
    /**
     * increment the number of total updates.
     */
    public void onPut(K key, V value) {
        this.updates++;
    }
    /**
     * Get the number of hits for the cache.
     *
     * @return number of hits
     */
    public int getHits() {
        return this.hits;
    }

    /**
     * Get the number of misses for the cache.
     *
     * @return number of misses
     */
    public int getMisses() {
        return this.misses;
    }

    /**
     * Get the number of updates (put operations) for the cache.
     *
     * @return number of updates
     */
    public int getUpdates() {
        return this.updates;
    }
}
