package dataStructures.classes;
/**
 *  Time Aware DLL that uses head and tail nodes to rapidly access data.
 *  BONUS: It stores the size of the DLL.
 *  I know, #features.
 *  @param V the data stored in a timeaware node
 */
public class TaDoublyLinkedList<V> {

    private TimeAwareNode<V> head;
    private TimeAwareNode<V> tail;
    private int size;

    public TaDoublyLinkedList() {
        size = 0; /* indian youtuber voice: ok, now the size is zero because there are no nodes */
        head = null;
        tail = null;
    }
    /**
     *  Push method adds nodes to the begining of the list.
     *  @param newNode the node we wish to insert.
     *  @return new head
     */
    public TimeAwareNode<V> push(TimeAwareNode<V> newNode) {
        newNode.next = head; /* transforming the new node so it looks like a head node */
        newNode.prev = null;

        if (head != null) { /* if the list isn't empty*/
            head.prev = newNode; /* make head.prev point to the new node */
            newNode.prev = null; /* make new node look like a head */
            newNode.next = head;

            head = newNode; /* make the new node be the head */
        } else { /* if the list is empty */
            newNode.next = null; /* then the new node is the only node */
            newNode.prev = null;
            head = newNode;
            tail = newNode;
        }

        size++;
        return head; /* i wish my girl would return head too */
    }

    /**
     *  Remove method removes nodes from the list.
     *  @param newNode the node we wish to remove.
     *  @return the node that was removed
     */
    public TimeAwareNode<V> remove(TimeAwareNode<V> node) {
        if (node.prev == null) { /* if removing the first node */
            head = node.next;
        } else { /* General case (Ted Mosby voice: "GENERAL CASE!") */
            node.prev.next = node.next;
        }
        if (node.next == null) { /* if removing the last node */
            tail = node.prev;
        } else { /* General case */
            node.next.prev = node.prev;
        }
        size--;
        return node;
    }
    /**
     *  Get the empty status of the list.
     *  @return the empty status
     */
    public boolean isEmpty() {
        return size == 0;
    }
    /**
     *  Get the size of the list.
     *  @return the dll's size
     */
    public int size() {
        return size;
    }
    /**
     *  Remove all elements from the list.
     */
    public void clearAll() {
        while (head != null) {
            head = head.next;
        }
        if (tail != null) {
            tail = tail.next;
        }
    }
    /**
     *  Get the data inside the tail node.
     *  @return tail data
     */
    public V getLastData() {
        return tail.getData();
    }

}
