package com.atlasbk.Atlas.Account;

import com.atlasbk.Atlas.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account {

    @Id
    @Column(name = "acc_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private BigDecimal balance;

    @Column(name = "acc_pass")
    private String accPass;

    @Column(name = "acc_num")
    private Integer accNum;
}
