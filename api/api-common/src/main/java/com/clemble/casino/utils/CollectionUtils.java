package com.clemble.casino.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtils {

    public static <T> Set<T> immutableSet(T element) {
        Set<T> immutableSet = new HashSet<>();
        immutableSet.add(element);
        return immutableSet(immutableSet);
    }

    public static <T> Set<T> immutableSet(Collection<? extends T> set) {
        return Collections.<T> unmodifiableSet(set instanceof Set ? (Set<? extends T>) set : new HashSet<>(set));
    }

    public static <T> List<T> immutableList(Collection<? extends T> list) {
        return Collections.<T> unmodifiableList(list instanceof List ? (List<? extends T>) list : new ArrayList<>(list));
    }

    @SafeVarargs
    public static <T> List<T> immutableList(T ... list) {
        return immutableList(Arrays.asList(list));
    }

}
