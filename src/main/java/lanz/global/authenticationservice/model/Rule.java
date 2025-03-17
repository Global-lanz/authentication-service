package lanz.global.authenticationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;


@Getter
@Setter
@Table
@Entity(name = "rule")
@NoArgsConstructor
public class Rule implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rule_id")
    private UUID ruleId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Override
    public String getAuthority() {
        return String.format("ROLE_%s", name);
    }
}
