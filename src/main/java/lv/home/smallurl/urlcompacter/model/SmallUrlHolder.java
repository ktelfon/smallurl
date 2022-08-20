package lv.home.smallurl.urlcompacter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallUrlHolder {

    @NotBlank(message = "original link is mandatory")
    private String original;

}
