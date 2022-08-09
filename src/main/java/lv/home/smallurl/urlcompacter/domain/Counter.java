package lv.home.smallurl.urlcompacter.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "counter")
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "count")
    private SmallUrl smallUrl;

    private long count;

    public void increment(){
        count++;
    }
}
