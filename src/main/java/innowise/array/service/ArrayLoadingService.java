package innowise.array.service;

import innowise.array.entity.IntArray;

import java.nio.file.Path;
import java.util.List;

public interface ArrayLoadingService {
    List<IntArray> load(Path path);
}
