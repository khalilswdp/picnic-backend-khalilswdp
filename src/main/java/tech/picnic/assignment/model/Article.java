package tech.picnic.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private String id;
    private String name;
    private String temperature_zone;

    // method to change the name of the article to upper case
    public void makeNameUpperCase() {
        this.name = name.toUpperCase();
    }
}
