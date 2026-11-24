package com.atlasbk.Atlas.Extract;

import com.atlasbk.Atlas.Account.Account;
import com.atlasbk.Atlas.Account.Types.OpType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExtractService {

    private ExtractRepository extractRepository;

    public ExtractService(ExtractRepository extractRepository) {
        this.extractRepository = extractRepository;
    }

    // EXTRATO TRANSFERENCIA
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

    // EXTRATO DEPOSITO E SAQUE
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

}
