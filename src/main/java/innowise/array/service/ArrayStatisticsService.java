package innowise.array.service;

import innowise.array.entity.IntArray;

import java.util.Optional;

public interface ArrayStatisticsService {
    Optional<Integer> findMax(IntArray array);

    Optional<Integer> findMin(IntArray array);

    Optional<Long> findSum(IntArray array);

    Optional<Double> findAverage(IntArray array);
}
