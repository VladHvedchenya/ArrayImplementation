package innowise.array.application;

import innowise.array.entity.IntArray;
import innowise.array.exception.ArrayFileReadException;
import innowise.array.exception.InvalidArrayException;
import innowise.array.factory.ArrayFactory;
import innowise.array.factory.IntArrayFactory;
import innowise.array.parser.ArrayLineParser;
import innowise.array.parser.impl.ArrayLineParserImpl;
import innowise.array.reader.ArrayFileReader;
import innowise.array.reader.impl.ArrayFileReaderImpl;
import innowise.array.service.ArrayLoadingService;
import innowise.array.service.ArraySortingService;
import innowise.array.service.ArrayStatisticsService;
import innowise.array.service.impl.ArrayLoadingServiceImpl;
import innowise.array.service.impl.ArraySortingServiceImpl;
import innowise.array.service.impl.ArrayStatisticsImpl;
import innowise.array.validation.ArrayLineValidation;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArrayApplication {
    private static final String DEFAULT_INPUT = "data/arrays.txt";

    private final Logger logger = LogManager.getLogger(ArrayApplication.class);

    private final ArrayLoadingService loadingService;
    private final ArrayStatisticsService statisticsService;
    private final ArraySortingService sortingService;
    private final ArrayFactory factory;

    public ArrayApplication(
            ArrayLoadingService loadingService,
            ArrayStatisticsService statisticsService,
            ArraySortingService sortingService,
            ArrayFactory factory) {
        if (loadingService == null
                || statisticsService == null
                || sortingService == null
                || factory == null) {
            throw new InvalidArrayException("Application dependencies must not be null");
        }

        this.loadingService = loadingService;
        this.statisticsService = statisticsService;
        this.sortingService = sortingService;
        this.factory = factory;
    }

    public static void main(String[] args) {
        ArrayFileReader reader = new ArrayFileReaderImpl();
        ArrayLineValidation validation = new ArrayLineValidation();
        ArrayLineParser parser = new ArrayLineParserImpl(validation);
        ArrayFactory factory = new IntArrayFactory();
        ArrayLoadingService loadingService = new ArrayLoadingServiceImpl(reader, parser, factory);
        ArrayStatisticsService statisticsService = new ArrayStatisticsImpl();
        ArraySortingService sortingService = new ArraySortingServiceImpl();
        ArrayApplication application =
                new ArrayApplication(loadingService, statisticsService, sortingService, factory);
        int status = application.run(args);

        if (status != 0) {
            System.exit(status);
        }
    }

    public int run(String[] args) {
        if (args == null || args.length > 1) {
            logger.error("Usage: ArrayApplication [relative-input-path]");
            return 2;
        }

        String input = args.length == 0 ? DEFAULT_INPUT : args[0];

        if (input == null || input.isBlank()) {
            logger.error("Input file path must not be blank");
            return 2;
        }

        try {
            Path path = Path.of(input);
            List<IntArray> arrays = loadingService.load(path);
            int number = 0;

            for (IntArray array : arrays) {
                number++;
                logResults(number, array);
            }

            return 0;
        } catch (ArrayFileReadException | InvalidArrayException | InvalidPathException exception) {
            String reason = exception.getMessage();
            logger.error("Application failed: {}", reason, exception);
            return 1;
        }
    }

    private void logResults(int number, IntArray array) {
        int[] original = array.getArray();
        String originalText = Arrays.toString(original);
        Optional<Integer> min = statisticsService.findMin(array);
        Optional<Integer> max = statisticsService.findMax(array);
        Optional<Long> sum = statisticsService.findSum(array);
        Optional<Double> average = statisticsService.findAverage(array);
        Integer minValue = min.orElse(null);
        Integer maxValue = max.orElse(null);
        Long sumValue = sum.orElse(null);
        Double averageValue = average.orElse(null);
        logger.info(
                "Array {}: {} | min={}, max={}, sum={}, average={}",
                number,
                originalText,
                minValue,
                maxValue,
                sumValue,
                averageValue);

        IntArray bubbleArray = factory.createArray(original);
        sortingService.useBubbleSort(bubbleArray);
        int[] bubbleValues = bubbleArray.getArray();
        String bubbleText = Arrays.toString(bubbleValues);
        logger.info("Array {} bubble sort: {}", number, bubbleText);

        IntArray mergeArray = factory.createArray(original);
        sortingService.useMergeSort(mergeArray);
        int[] mergeValues = mergeArray.getArray();
        String mergeText = Arrays.toString(mergeValues);
        logger.info("Array {} merge sort: {}", number, mergeText);
    }
}
