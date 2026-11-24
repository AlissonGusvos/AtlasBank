package com.atlasbk.Atlas.Extract;

import com.atlasbk.Atlas.Account.Account;
import com.atlasbk.Atlas.Account.Types.OpType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "extract")
@Getter
@Setter
public class Extract {

    @Id
    @Column(name = "ex_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "op_type")
    private OpType opType;

    @ManyToOne
    @JoinColumn(name = "orgn_acc_id")
    private Account orgnAccount;

    @ManyToOne
    @JoinColumn(name = "desti_acc_id")
    private Account recvAccount;

    @Column(name = "op_value")
    private BigDecimal value;

    @CreationTimestamp
    @Column(name = "release_date")
    private LocalDateTime releaseDate;
}
