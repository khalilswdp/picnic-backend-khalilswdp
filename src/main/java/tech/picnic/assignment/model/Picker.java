package tech.picnic.assignment.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picker {
    private String id;
    private String name;
    private String activeSince;
}
