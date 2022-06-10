package tech.picnic.assignment.model;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
}
