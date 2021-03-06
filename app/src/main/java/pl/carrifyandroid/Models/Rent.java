package pl.carrifyandroid.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Rent {
    private int id;
    private int distance;
    private int amount;
    private String createdAt;
    private String endAt;
    private int userId;
    private Car car;
}
