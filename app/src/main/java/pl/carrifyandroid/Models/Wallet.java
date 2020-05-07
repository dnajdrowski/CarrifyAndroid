package pl.carrifyandroid.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    private int id;
    private int amount;
    private String lastTransaction;
    private int user;

}
