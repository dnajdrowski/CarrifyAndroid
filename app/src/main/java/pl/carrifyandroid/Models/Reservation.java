package pl.carrifyandroid.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Reservation {
    private int id;
    private int state;
    private int canExtend;
    private String createdAt;
    private String finishedAt;
    private int carId;
    private int userId;
}
