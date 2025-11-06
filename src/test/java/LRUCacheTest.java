import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LRUCacheTest {
    private static final int CAPACITY = 3;
    private Map<String, String> cache;

    @BeforeEach
    public void setUpCache() {
        cache = new LinkedHashMap<>(10, 1, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return this.size() > CAPACITY;
            }
        };
    }

    @Test
    public void testLRUCache() {
        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");
        assertEquals(3, cache.size());
        assertFalse(cache.containsKey("1"));
        assertTrue(cache.containsKey("2"));
        assertTrue(cache.containsKey("3"));
        assertTrue(cache.containsKey("4"));

        cache.get("2");

        cache.put("5", "5");
        assertTrue(cache.containsKey("2"));
        assertFalse(cache.containsKey("3"));
        assertTrue(cache.containsKey("4"));
        assertTrue(cache.containsKey("5"));

        cache.entrySet().forEach(System.out::println);
    }

    @Test
    public void testLRUCache1() {
        Cache cache1 = new Cache(3);
        cache1.put("1", "1");
        cache1.put("2", "2");
        cache1.put("3", "3");
        cache1.put("4", "4");
        assertEquals(3, cache1.size());
        assertFalse(cache1.containsKey("1"));
        assertTrue(cache1.containsKey("2"));
        assertTrue(cache1.containsKey("3"));
        assertTrue(cache1.containsKey("4"));

        cache1.get("2");

        cache1.put("5", "5");
        assertTrue(cache1.containsKey("2"));
        assertFalse(cache1.containsKey("3"));
        assertTrue(cache1.containsKey("4"));
        assertTrue(cache1.containsKey("5"));
    }

    @Test
    public void testLRUCache2() {
        LRUCache<String, String> cache1 = new LRUCache<>(3);
        cache1.put("1", "1");
        cache1.put("2", "2");
        cache1.put("3", "3");
        cache1.put("4", "4");
        assertEquals(3, cache1.size());
        assertFalse(cache1.containsKey("1"));
        assertTrue(cache1.containsKey("2"));
        assertTrue(cache1.containsKey("3"));
        assertTrue(cache1.containsKey("4"));

        cache1.get("2");

        cache1.put("5", "5");
        assertTrue(cache1.containsKey("2"));
        assertFalse(cache1.containsKey("3"));
        assertTrue(cache1.containsKey("4"));
        assertTrue(cache1.containsKey("5"));

    }

    public static class Cache {
        private final Map<String, CacheEntry> cache;
        private final LinkedList<CacheEntry> list;
        private final int capacity;

        private Cache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.list = new LinkedList<>();
        }

        public String get(String key) {
            if (cache.containsKey(key)) {
                // update queue
                CacheEntry entry = cache.get(key);
                // we can do better here
                list.remove(entry);
                list.addLast(entry);
                return entry.value;
            } else {
                return null;
            }
        }

        public void put(String key, String value) {
            if (cache.size() >= capacity) {
                // evict
                CacheEntry entryToPop = list.poll();
                if (entryToPop != null) {
                    cache.remove(entryToPop.key);
                }
            }
            // put
            CacheEntry cacheEntry = new CacheEntry(key, value);
            cache.put(key, cacheEntry);
            list.addLast(cacheEntry);
        }

        public int size() {
            return cache.size();
        }


        public boolean containsKey(String key) {
            return cache.containsKey(key);
        }
    }

    private static class CacheEntry {
        String key;
        String value;

        public CacheEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
