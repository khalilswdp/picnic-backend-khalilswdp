package tech.picnic.assignment.model;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickerProcessedInput {
    private transient String id;
    private String picker_name;
    private String active_since;
    private List<ArticleProcessedInput> picks;

    public static void toJson(OutputStream sink, List<PickerProcessedInput> pickerProcessedInputs) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(pickerProcessedInputs);
        sink.write(json.getBytes());
        sink.flush();
    }

    public PickerProcessedInput(Map.Entry<Picker, List<Pick>> entry) {
        this.id = entry.getKey().getId();
        this.picker_name = entry.getKey().getName();
        this.active_since = entry.getKey().getActive_since();
        this.picks = entry.getValue().stream().map(ArticleProcessedInput::new).collect(Collectors.toList());
    }


}
