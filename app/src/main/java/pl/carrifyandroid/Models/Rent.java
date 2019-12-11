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
    private int amount;
    private Car car;
    private String createdAt;
    private int distance;
    private String endAt;
    private int id;
    private int userId;
}
