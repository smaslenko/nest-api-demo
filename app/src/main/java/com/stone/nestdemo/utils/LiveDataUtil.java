package com.stone.nestdemo.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LiveDataUtil {

    /**
     * Merges two {@link LiveData} of {@link List} of sibling-typed elements
     * and maps resulting collection to the given output type (must be a super-type)
     *
     * @param source1 argument of {@link LiveData}
     * @param source2 argument of {@link LiveData}
     * @return {@link LiveData} of {@link List} of merged elements of super type
     */
    public static <N, K extends N, V extends N> LiveData<List<N>> mergeListsAndMap(LiveData<List<K>> source1, LiveData<List<V>> source2) {
        return Transformations.switchMap(source1, s1 -> {
            return
                Transformations.switchMap(source2, s2 -> {
                    List<N> merged = Stream.concat(s1.stream(), s2.stream()).collect(Collectors.toList());
                    MutableLiveData<List<N>> mergedData = new MutableLiveData<>();
                    mergedData.postValue(merged);
                    return mergedData;
                });

        });

    }
}
