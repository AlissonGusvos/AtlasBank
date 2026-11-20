package com.atlasbk.Atlas.Account;

import com.atlasbk.Atlas.Account.request.DepositRequest;
import com.atlasbk.Atlas.Account.responses.OverviewResponse;
import com.atlasbk.Atlas.User.User;
import com.atlasbk.Atlas.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // MAIN MENU
    public OverviewResponse overview(Authentication authentication){

        Optional<User> optionalUser = userRepository.findByCpf(authentication.getName());
        if (optionalUser.isEmpty()){
            new Exception("Conta não encontrada! Tente novamente mais tarde");
        }

        User user = optionalUser.get();

        Optional<Account> optionalAccount = accountRepository.findByUserId_Id(user.getId());
        double accountBalance = optionalAccount.get().getBalance();


        return new OverviewResponse(user.getUsername(), accountBalance);
    }

    // DEPOSITO
    public ResponseEntity<?> deposit(DepositRequest request, Authentication authentication){

        Optional<User> optionalUser = userRepository.findByCpf(authentication.getName());
        User user = optionalUser.get();

        Optional<Account> optionalAccount = accountRepository.findByUserId_Id(user.getId());
        Account account = optionalAccount.get();

        double value = request.value();
        String accPass = request.accPass();

        if (value <= 0) {
            return ResponseEntity.status(400).body("Insira um valor válido");
        }

        if (!accPass.equals(account.getAccPass())){
            return ResponseEntity.status(400).body("Senha incorreta");
        }

        double balance = account.getBalance();

        account.setBalance(balance+value);
        accountRepository.save(account);

        return ResponseEntity.status(200).body("Depósito feito");

    }
}
