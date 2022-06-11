package tech.picnic.assignment.model;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickerProcessedInput {
    private transient String id;
    private String picker_name;
    private String active_since;
    private List<ArticleProcessedInput> picks;

    public PickerProcessedInput(Map.Entry<Picker, List<Pick>> entry) {
        this.id = entry.getKey().getId();
        this.picker_name = entry.getKey().getName();
        this.active_since = entry.getKey().getActive_since();
        this.picks = entry.getValue().stream().map(ArticleProcessedInput::new).collect(Collectors.toList());
    }

    public static void toJson(OutputStream sink, List<PickerProcessedInput> pickerProcessedInputs) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(pickerProcessedInputs);
        sink.write(json.getBytes());
        sink.flush();
    }

    public static List<PickerProcessedInput> getPickerProcessedInputs(List<Pick> picks) {
        List<String> temperatureZones = List.of("ambient");
        Predicate<Pick> customFilter = pick -> temperatureZones.contains(pick.getArticle().getTemperature_zone());
        Consumer<Pick> callToConsumersForDesiredTransformations = pick -> pick.getArticle().makeNameUpperCase();
        Comparator<PickerProcessedInput> sortResultBy = Comparator.comparing(PickerProcessedInput::getActive_since).thenComparing(PickerProcessedInput::getId);

        return getPickerProcessedInputs(picks, customFilter, callToConsumersForDesiredTransformations, sortResultBy);
    }

    private static List<PickerProcessedInput> getPickerProcessedInputs(
            List<Pick> picks,
            Predicate<Pick> picksFilter,
            Consumer<Pick> picksTransformation,
            Comparator<PickerProcessedInput> sortBy) {
        return picks.stream()
                .filter(picksFilter)
                .peek(picksTransformation)
                .collect(Collectors.groupingBy(Pick::getPicker))
                .entrySet().stream()
                .map(PickerProcessedInput::new)
                .sorted(sortBy)
                .collect(Collectors.toList());
    }

}
