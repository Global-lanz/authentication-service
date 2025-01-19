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
@Table
@Entity(name = "rule")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rule_id")
    private UUID ruleId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
