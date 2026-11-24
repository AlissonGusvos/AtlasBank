package com.atlasbk.Atlas.Auth;

import com.atlasbk.Atlas.Account.Account;
import com.atlasbk.Atlas.Account.AccountRepository;
import com.atlasbk.Atlas.Auth.requests.UserLoginRequest;
import com.atlasbk.Atlas.Auth.requests.UserRegisterRequest;
import com.atlasbk.Atlas.Config.FuncConfiguration;
import com.atlasbk.Atlas.Config.SecurityConfig;
import com.atlasbk.Atlas.User.User;
import com.atlasbk.Atlas.User.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final FuncConfiguration funcConfiguration;

    public AuthService(UserRepository userRepository, SecurityConfig securityConfig, AuthenticationManager authenticationManager, AccountRepository accountRepository, FuncConfiguration funcConfiguration) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.funcConfiguration = funcConfiguration;
    }

    public ResponseEntity<?> registerUser(UserRegisterRequest request){

        if (userRepository.existsByCpf(request.cpf())){
            return ResponseEntity.status(409).body("Esse CPF já está cadastrado");
        }
        if (userRepository.existsByEmail(request.email())){
            return ResponseEntity.status(409).body("Esse E-Mail já está cadastrado");
        }

        User user = new User();

        Long userId = ThreadLocalRandom.current().nextLong(1000000, 9999999);
        while(userRepository.existsById(userId)){
            userId = ThreadLocalRandom.current().nextLong(1000000, 9999999);
        }

        user.setId(userId);
        user.setEmail(request.email());
        user.setHashPass(securityConfig.passwordEncoder().encode(request.password()));
        user.setCpf(request.cpf());
        user.setUsername(request.username());

        userRepository.save(user);

        // CRIAR CONTA
        Account account = new Account();

        Long accId = ThreadLocalRandom.current().nextLong(1000000, 9999999);
        while(accountRepository.existsById(accId)){
            accId = ThreadLocalRandom.current().nextLong(1000000, 9999999);
        }

        Integer accNum = ThreadLocalRandom.current().nextInt(10000, 99999);
        while(accountRepository.existsByAccNum(accNum)){
            accNum = ThreadLocalRandom.current().nextInt(10000, 99999);
        }

        account.setId(accId);
        account.setUserId(user);
        account.setAccPass(request.accPass());
        account.setAccNum(accNum);
        account.setBalance(BigDecimal.ZERO);

        accountRepository.save(account);

        return ResponseEntity.status(200).body("Usuário registrado");

    }

    public ResponseEntity<?> loginUser(UserLoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse){

        // VALIDAÇÃO
        Optional<User> optionalUser = userRepository.findByCpf(request.cpf());
        if (optionalUser.isEmpty()){
            return ResponseEntity.status(409).body("Senha/CPF incorreto(s)");
        }
        User user = optionalUser.get();
        if (!securityConfig.passwordEncoder().matches(request.password(), user.getHashPass())){
            return ResponseEntity.status(409).body("Senha/CPF incorreto(s)");
        }

        // CRIAR AUTENTICAÇÃO
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.cpf(),
                        request.password()
                )
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository()
                .saveContext(context, httpRequest, httpResponse);

        return ResponseEntity.status(200).body("Logado com sucesso");
    }
}
