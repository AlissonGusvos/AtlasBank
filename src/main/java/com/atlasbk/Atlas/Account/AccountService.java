package com.atlasbk.Atlas.Account;

import com.atlasbk.Atlas.Account.Types.OpType;
import com.atlasbk.Atlas.Account.request.DepositRequest;
import com.atlasbk.Atlas.Account.request.TransferRequest;
import com.atlasbk.Atlas.Account.request.WithdrawRequest;
import com.atlasbk.Atlas.Account.responses.OverviewResponse;
import com.atlasbk.Atlas.Extract.ExtractService;
import com.atlasbk.Atlas.User.User;
import com.atlasbk.Atlas.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ExtractService extractService;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, ExtractService extractService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.extractService = extractService;
    }

    // MAIN MENU
    public OverviewResponse overview(Authentication authentication){

        Optional<User> optionalUser = userRepository.findByCpf(authentication.getName());
        if (optionalUser.isEmpty()){
            new Exception("Conta não encontrada! Tente novamente mais tarde");
        }

        User user = optionalUser.get();

        Optional<Account> optionalAccount = accountRepository.findByUserId_Id(user.getId());
        BigDecimal accountBalance = optionalAccount.get().getBalance();


        return new OverviewResponse(user.getUsername(), accountBalance);
    }

    // DEPOSITO
    public ResponseEntity<?> deposit(DepositRequest request, Authentication authentication){

        //RESGATAR USUARIO E CONTA
        Optional<User> optionalUser = userRepository.findByCpf(authentication.getName());
        User user = optionalUser.get();

        Optional<Account> optionalAccount = accountRepository.findByUserId_Id(user.getId());
        Account account = optionalAccount.get();

        BigDecimal value = request.value();
        String accPass = request.accPass();

        // LOGICA DE DEPOSITO
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(400).body("Insira um valor válido");
        }

        if (!accPass.equals(account.getAccPass())){
            return ResponseEntity.status(400).body("Senha incorreta");
        }

        BigDecimal balance = account.getBalance();

        account.setBalance(balance.add(value));
        accountRepository.save(account);

        extractService.extract(OpType.DEPOSIT, account, value);

        return ResponseEntity.status(200).body("Depósito realizado");

    }

    // SAQUE
    public ResponseEntity<?> withdraw(WithdrawRequest request, Authentication authentication){

        //RESGATAR USUARIO E CONTA
        Optional<User> optionalUser = userRepository.findByCpf(authentication.getName());
        User user = optionalUser.get();

        Optional<Account> optionalAccount = accountRepository.findByUserId_Id(user.getId());
        Account account = optionalAccount.get();

        BigDecimal value = request.value();
        String accPass = request.accPass();

        // LOGICA DE SAQUE
        if (value.compareTo(account.getBalance()) > 0){
            return ResponseEntity.status(400).body("Saldo insuficiente");
        }

        if (!accPass.equals(account.getAccPass())){
            return ResponseEntity.status(400).body("Senha incorreta");
        }

        account.setBalance(account.getBalance().subtract(value));
        accountRepository.save(account);

        extractService.extract(OpType.SAKE, account, value);

        return ResponseEntity.status(200).body("Saque efetuado");
    }

    // TRANSFERENCIA
    public ResponseEntity<?> transfer(TransferRequest request, Authentication authentication){
        // RESGATAR CONTA PRINCIPAL
        Optional<User> optionalUser = userRepository.findByCpf(authentication.getName());
        User user = optionalUser.get();

        Optional<Account> optionalAccount = accountRepository.findByUserId_Id(user.getId());
        Account account = optionalAccount.get();

        BigDecimal value = request.value();
        String accPass = request.accPass();

        if (value.compareTo(BigDecimal.ZERO) <= 0){
            return ResponseEntity.status(400).body("Insira um valor válido");
        }

        if (!accPass.equals(account.getAccPass())){
            return ResponseEntity.status(400).body("Senha inválida");
        }

        // RESGATAR CONTA DESTINATARIA
        Optional<Account> optionalDestAccount = accountRepository.findAccountByAccNum(request.recvAccNum());
        Account recvAccount = optionalDestAccount.get();

        // INFORMACOES DAS CONTAS

        // Remetente
        BigDecimal princBalance = account.getBalance();

        // Destinatario
        BigDecimal recvBalance = recvAccount.getBalance();

        // LOGICA

        if (value.compareTo(princBalance) > 0){
            return ResponseEntity.status(400).body("Saldo Insuficiente");
        }

        recvAccount.setBalance(recvBalance.add(value));
        account.setBalance(princBalance.subtract(value));

        accountRepository.save(recvAccount);
        accountRepository.save(account);

        extractService.extract(OpType.TRANSFER, account, recvAccount, value);

        return ResponseEntity.status(200).body("Transferencia realizada");
    }
}
