package gr.ds.unipi.wi.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public final class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int maxEntries;

    private LRUCache(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    public static LRUCache newLRUCache(int maxEntries) {
        return new LRUCache(maxEntries);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {

        return size() > getMaxEntries();
    }

    private int getMaxEntries() {
        return maxEntries;
    }

}
