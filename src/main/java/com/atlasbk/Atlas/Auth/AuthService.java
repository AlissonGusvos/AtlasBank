package com.atlasbk.Atlas.Auth;

import com.atlasbk.Atlas.Auth.requests.UserLoginRequest;
import com.atlasbk.Atlas.Auth.requests.UserRegisterRequest;
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

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, SecurityConfig securityConfig, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> registerUser(UserRegisterRequest request){

        if (userRepository.existsByCpf(request.cpf())){
            return ResponseEntity.status(409).body("Esse CPF já está cadastrado");
        }
        if (userRepository.existsByEmail(request.email())){
            return ResponseEntity.status(409).body("Esse E-Mail já está cadastrado");
        }

        User user = new User();

        Long id = securityConfig.generateId();
        while(userRepository.existsById(id)){
            id = securityConfig.generateId();
        }

        user.setId(id);
        user.setEmail(request.email());
        user.setHashPass(securityConfig.passwordEncoder().encode(request.password()));
        user.setCpf(request.cpf());
        user.setUsername(request.username());

        userRepository.save(user);

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
