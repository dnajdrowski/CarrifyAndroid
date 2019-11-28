package pl.carrifyandroid.API.ApiModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class NewRentRequest {
    private int carId;
    private int userId;
}
