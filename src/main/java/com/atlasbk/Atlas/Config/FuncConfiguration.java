package com.atlasbk.Atlas.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class FuncConfiguration {

    @Bean
    public Integer generateAccountNumber(){
        return ThreadLocalRandom.current().nextInt(10000, 99999);
    }


    /*@Bean
    public Long generateId(){
        return ThreadLocalRandom.current().nextLong(1000000, 9999999);
    }*/

}
