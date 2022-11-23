package com.uber.rocket.pagination;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Pagination {
    public List<?> paginate(List<?> items, int size, int page) {
        if (size >= items.size()) {
            return items;
        }
        int toIndex = getToIndex(items, size, page);
        int fromIndex = getFromIndex(items, size, page, toIndex);
        return items.subList(fromIndex, toIndex);
    }

    private static int getFromIndex(List<?> items, int size, int page, int toIndex) {
        int fromIndex = (page - 1) * size;
        if (fromIndex >= items.size()) {
            fromIndex = toIndex - size;
        }
        return fromIndex;
    }

    private static int getToIndex(List<?> items, int size, int page) {
        int toIndex = page * size;
        if (toIndex >= items.size()) {
            toIndex = items.size();
        }
        return toIndex;
    }
}
