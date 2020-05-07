package pl.carrifyandroid.API.ApiModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TopUpWalletRequest {

    private int walletId;
    private int amount;

}
