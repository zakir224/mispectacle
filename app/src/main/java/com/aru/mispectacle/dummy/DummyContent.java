package com.aru.mispectacle.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<Integer, DummyItem> ITEM_MAP = new HashMap<Integer, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem(1,  "Photo 1"));
        addItem(new DummyItem(2,  "Photo 2"));
        addItem(new DummyItem(3,  "Photo 3"));
        addItem(new DummyItem(4,  "Photo 4"));
        addItem(new DummyItem(5,  "Photo 5"));
        addItem(new DummyItem(6,  "Photo 6"));
        addItem(new DummyItem(7,  "Photo 7"));
        addItem(new DummyItem(8, "Photo 8"));
        addItem(new DummyItem(9, "Photo 9"));
        addItem(new DummyItem(10, "Photo 10"));
        addItem(new DummyItem(11, "Photo 11"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public int id;
        public String content;

        public DummyItem(int id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
