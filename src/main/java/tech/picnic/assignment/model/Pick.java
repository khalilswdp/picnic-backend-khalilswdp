package tech.picnic.assignment.model;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pick {
    private String id;
    private String timestamp;
    private Picker picker;
    private Article article;
    private int quantity;

    public static Pick fromJson(String line) throws Exception {
        Gson gson = new Gson();
        Pick pick = gson.fromJson(line, Pick.class);
        return pick;
    }
}
