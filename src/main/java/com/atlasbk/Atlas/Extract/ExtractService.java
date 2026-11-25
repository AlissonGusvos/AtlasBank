package com.atlasbk.Atlas.Extract;

import com.atlasbk.Atlas.Account.Account;
import com.atlasbk.Atlas.Account.AccountRepository;
import com.atlasbk.Atlas.Account.Types.OpType;
import com.atlasbk.Atlas.Account.responses.ExtractResponse;
import com.atlasbk.Atlas.User.User;
import com.atlasbk.Atlas.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExtractService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private ExtractRepository extractRepository;

    public ExtractService(ExtractRepository extractRepository, UserRepository userRepository, AccountRepository accountRepository) {
        this.extractRepository = extractRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    // SALVAR EXTRATO TRANSFERENCIA
    public void extract(OpType opType, Account mainAccount, Account recvAccount, BigDecimal value){
        Extract extract = new Extract();

        Long exId = ThreadLocalRandom.current().nextLong(1000000, 9999999);
        while(extractRepository.existsById(exId)){
            exId = ThreadLocalRandom.current().nextLong(1000000, 9999999);
        }

        extract.setId(exId);

        extract.setOpType(opType);
        extract.setOrgnAccount(mainAccount);
        extract.setRecvAccount(recvAccount);
        extract.setValue(value);

        extractRepository.save(extract);
    }

    // SALVAR EXTRATO DEPOSITO E SAQUE
    public void extract(OpType opType, Account mainAccount, BigDecimal value){
        Extract extract = new Extract();

        Long exId = ThreadLocalRandom.current().nextLong(1000000, 9999999);
        while(extractRepository.existsById(exId)){
            exId = ThreadLocalRandom.current().nextLong(1000000, 9999999);
        }

        extract.setId(exId);

        extract.setOpType(opType);
        extract.setOrgnAccount(mainAccount);
        extract.setValue(value);

        extractRepository.save(extract);
    }


    // EXIBIR EXTRATO
    public List<ExtractResponse> extractResponse(Authentication authentication){

        // RESGATAR USUÁRIO
        Optional<User> optionalUser = userRepository.findByCpf(authentication.getName());
        User user = optionalUser.get();

        Optional<Account> optionalAccount = accountRepository.findByUserId_Id(user.getId());
        Account account = optionalAccount.get();

        // RESGATAR EXTRATO
        List<Extract> extracts = extractRepository.findDistinctByOrgnAccount_Id(account.getId());

        return extracts.stream()
                .map(extract -> toResponse(extract, account)).toList();
    }

    private ExtractResponse toResponse(Extract extract, Account account){
        boolean isOrigin = extract.getOrgnAccount().getId().equals(account.getId());

        boolean isTransfer = extract.getRecvAccount() != null;

        String direction = null;
        if (extract.getOpType() == OpType.DEPOSIT){
            direction = "ENTRADA";
        }
        else {
            direction = "SAÍDA";
        }

        String transferNameReceiver = null;

        // RESGATAR NOME
        if (isTransfer) {
            Account otherAccount = isOrigin ? extract.getRecvAccount() : extract.getOrgnAccount();

            if (otherAccount.getUserId() != null) {
                transferNameReceiver = otherAccount.getUserId().getUsername();
            }
        }

        return new ExtractResponse(
                extract.getOpType(),
                extract.getValue(),
                extract.getReleaseDate(),
                transferNameReceiver,
                direction
        );
    }
}
