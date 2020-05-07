package pl.carrifyandroid.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private int id;
    private int amount;
    private int balance;
    private int operationType;
    private String createdAt;

}
