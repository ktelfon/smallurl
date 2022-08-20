package lv.home.smallurl.urlcompacter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UrlStats {
    private String link;
    private String original;
    private long rank;
    private long count;
}
