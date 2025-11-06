import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {

    private final int capacity;
    private final Map<K, LRUNode<K, V>> cache;

    private LRUNode<K, V> head; // pop from the head
    private LRUNode<K, V> tail; // put at the tail

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
    }

    public void put(K key, V value) {
        // if empty
        if (cache.isEmpty()) {
            LRUNode<K, V> lruNode = new LRUNode<>(key, value);
            head = lruNode;
            tail = lruNode;
            cache.put(key, lruNode);
            return;
        } else {
            if (cache.containsKey(key)) {
                LRUNode<K, V> lruNode = cache.get(key);
                lruNode.value = value;
                lruNode.detachNode();
                addToTail(lruNode);
            } else {
                LRUNode<K, V> node = new LRUNode<>(key, value);
                addToTail(node);
                cache.put(key, node);
            }
            if (cache.size() > capacity) {
                removeHead();
            }
        }
    }

    private LRUNode<K, V> removeHead() {
        if (head != null) {
            cache.remove(head.key);
            LRUNode<K, V> nextHead = head.next;
            nextHead.previous = null;
            head = nextHead;
        }
        return null;
    }

    private void addToTail(LRUNode<K, V> node) {
        if (tail != null) {
            node.previous = tail;
            tail.next = node;
        }
        tail = node;
    }

    public V get(K key) {
        if (cache.containsKey(key)) {
            LRUNode<K, V> entry = cache.get(key);
            if (head == entry) {
                head = entry.next;
            }
            entry.detachNode();
            addToTail(entry);
            return entry.value;
        }
        return null;
    }

    public int size() {
        return cache.size();
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public static class LRUNode<K, V> {
        K key;
        V value;
        LRUNode<K, V> previous;
        LRUNode<K, V> next;

        public LRUNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public void detachNode() {
            if (previous != null) previous.next = this.next;
            if (next != null) next.previous = this.previous;
            this.previous = null;
            this.next = null;
        }
    }

}
