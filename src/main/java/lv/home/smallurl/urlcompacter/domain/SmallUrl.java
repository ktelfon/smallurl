package lv.home.smallurl.urlcompacter.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "smallurl")
public class SmallUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "url_id")
    private Long id;

    @NotEmpty
    private String original;

    @NotEmpty
    private String compressed;

   @Min(1)
    private int count;
}
