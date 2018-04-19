package dataStructures.classes;
import java.sql.Timestamp;

/**
 * A node that stores data and a timestamp for when it was created.
 * @param V the type of data stored inside this node
 */
public class TimeAwareNode<V> {
    private V data;
    protected TimeAwareNode prev;
    protected TimeAwareNode next;
    private Timestamp timestamp;

    public TimeAwareNode(V newData) {
        this.data = newData;
        prev = null;
        next = null;
        timestamp = new Timestamp(System.currentTimeMillis());
    }
    /**
     * return the data inside this node.
     * @return data in the node
     */
    public V getData() {
        return data;
    }
    /**
     * return the timestamp of this node.
     * @return node's timestamp
     */
    public Timestamp getTimestamp() {
        return this.timestamp;
    }
}
