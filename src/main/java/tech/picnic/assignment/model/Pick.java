package tech.picnic.assignment.model;

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

}
