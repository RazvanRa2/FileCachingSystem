package observerPattern.classes;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Comparator;
import observerPattern.interfaces.CacheListener;
import java.util.HashMap;
import java.util.Collections;
/**
 * The KeyStatsListener collects key-level stats for cache operations.
 *
 * @param <K>
 * @param <V>
 */
public class KeyStatsListener<K, V> implements CacheListener<K, V> {
    /**
     * Node is used to locally store data for each key.
     * @param K the type of key stored
     *
     */
    class Node {
        private K key;
        private int hits;
        private int misses;
        private int updates;

        Node(K newKey) {
            key = newKey;
            hits = 0;
            misses = 0;
            updates = 0;
        }
        /**
         * increment the number of hits.
         */
        void incHits() {
            this.hits++;
        }
        /**
         * increment the number of misses.
         */
        void incMisses() {
            this.misses++;
        }
        /**
         * increment the number of updates.
         */
        void incUpdates() {
            this.updates++;
        }

        int getUpdates() {
            return this.updates;
        }
        int getHits() {
            return this.hits;
        }
        int getMisses() {
            return this.misses;
        }

        K getKey() {
            return this.key;
        }
    }
    /**
     *  Inner Updats comparator class is used to sort nodes in descending order by no. of updates;
     */
    class UpdatesComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return n1.getUpdates() > n2.getUpdates()
            ? -1 : (n1.getUpdates() < n2.getUpdates() ? 1 : 0);
        }
    }
    /**
     *  Inner Misses comparator class is used to sort nodes in descending order by no. of updates;
     */
    class MissesComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return n1.getMisses() > n2.getMisses()
            ? -1 : (n1.getMisses() < n2.getMisses() ? 1 : 0);
        }
    }
    /**
     *  Inner Hits comparator class is used to sort nodes in descending order by no. of updates;
     */
    class HitsComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return n1.getHits() > n2.getHits()
            ? -1 : (n1.getHits() < n2.getHits() ? 1 : 0);
        }
    }
    /* i locally use a hashmap to store data because of the need of O(1) operations */
    private HashMap<K, Node> cacheMap = new HashMap<K, Node>();
    /**
     * update the number of hits for a key.
     *
     * @param key the key that was hit
     */
    public void onHit(K key) {

        Node node = cacheMap.get(key);
        if (node != null) { /* if the key was hit before */
            node.incHits(); /* increment it hard */
        } else { /* otherwise it's a virgin key */
            Node newNode = new Node(key); /* increment it gently */
            newNode.incHits();
            cacheMap.put(key, newNode);
        }
    }
    /**
     * update the number of misses for a key.
     *
     * @param key the key that was hit
     */
    public void onMiss(K key) {
        Node node = cacheMap.get(key);
        if (node != null) { /* if the key is stored */
            node.incMisses(); /* just do increment */
        } else {
            Node newNode = new Node(key); /*if it isn't store it and increment */
            newNode.incMisses();
            cacheMap.put(key, newNode);
        }
    }
    /**
     * update the number of misses for a key.
     *
     * @param key the key that was hit
     */
    public void onPut(K key, V value) {

        Node node = cacheMap.get(key); /* if the key was stored before */
        if (node != null) {
            node.incUpdates(); /* increment */
        } else {
            Node newNode = new Node(key); /* otherwise add it and increment */
            newNode.incUpdates();
            cacheMap.put(key, newNode);
        }
    }
    /**
     * Get the number of hits for a key.
     *
     * @param key the key
     * @return number of hits
     */
    public int getKeyHits(K key) {
        if (cacheMap.get(key) != null) {
            return cacheMap.get(key).getHits();
        }
        return 0;
    }

    /**
     * Get the number of misses for a key.
     *
     * @param key the key
     * @return number of misses
     */
    public int getKeyMisses(K key) {
        if (cacheMap.get(key) != null) {
            return cacheMap.get(key).getMisses();
        }
        return 0;
    }

    /**
     * Get the number of updates for a key.
     *
     * @param key the key
     * @return number of updates
     */
    public int getKeyUpdates(K key) {
        if (cacheMap.get(key) != null) {
            return cacheMap.get(key).getUpdates();
        }
        return 0;
    }

    /**
     * Get the top most hit keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopHitKeys(int top) {
        LinkedList<Node> cacheList = new LinkedList<Node>(); /* listify cache */
        Iterator it = cacheMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            cacheList.add((Node) pair.getValue());
        }
        top = top > cacheList.size() ? cacheList.size() : top; /* assert top */
        List<K> result = new LinkedList<K>();
        Collections.sort(cacheList, new HitsComparator()); /* sort the cache */
        int cnt = 0;
        for (Node node : cacheList) { /* after sorting, get top k values */
            if (cnt < top) {
                result.add(node.getKey());
                cnt++;
            } else {
                break;
            }
        }

        return result;
    }

    /**
     * Get the top most missed keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopMissedKeys(int top) {
        LinkedList<Node> cacheList = new LinkedList<Node>(); /* listify cache */
        Iterator it = cacheMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            cacheList.add((Node) pair.getValue());
        }
        top = top > cacheList.size() ? cacheList.size() : top; /* assert top */
        List<K> result = new LinkedList<K>();
        Collections.sort(cacheList, new MissesComparator()); /* sort the cache */
        int cnt = 0;
        for (Node node : cacheList) { /* after sorting, get top k values */
            if (cnt < top) {
                result.add(node.getKey());
                cnt++;
            } else {
                break;
            }
        }

        return result;
    }

    /**
     * Get the top most updated keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopUpdatedKeys(int top) {
        LinkedList<Node> cacheList = new LinkedList<Node>(); /* listify cache */
        Iterator it = cacheMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            cacheList.add((Node) pair.getValue());
        }
        top = top > cacheList.size() ? cacheList.size() : top; /* assert top */
        List<K> result = new LinkedList<K>();
        Collections.sort(cacheList, new UpdatesComparator()); /* sort the cache */
        int cnt = 0;
        for (Node node : cacheList) { /* after sorting, get top k values */
            if (cnt < top) {
                result.add(node.getKey());
                cnt++;
            } else {
                break;
            }
        }

        return result;
    }

}
