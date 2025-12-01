package cl.samuel.barzarena.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RapperResponse {
    private Long id;
    private String name;
    private String realName;
    private String bio;
    private String origin;
    private String imageUrl;
    private Boolean active;
}

