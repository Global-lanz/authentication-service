package lanz.global.authenticationservice.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "currency_id")
    private UUID currencyId;

    @Column(name = "name")
    private String name;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "code")
    private String code;

}
