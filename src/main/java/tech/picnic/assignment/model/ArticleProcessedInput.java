package tech.picnic.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleProcessedInput {
    private String article_name;
    private String timestamp;

    public ArticleProcessedInput(Pick pick) {
        this.article_name = pick.getArticle().getName();
        this.timestamp = pick.getTimestamp();
    }
}
