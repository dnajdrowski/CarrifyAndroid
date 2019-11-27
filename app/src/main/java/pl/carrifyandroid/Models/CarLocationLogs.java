package pl.carrifyandroid.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CarLocationLogs {

    private Integer id;
    private Double latitude;
    private Double longitude;
    private String createdAt;

}
