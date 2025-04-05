package lanz.global.authenticationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.UUID;


@Getter
@Setter
@Table
@Entity(name = "rule")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rule implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 2667303541350470736L;

    @Id
    @EqualsAndHashCode.Include
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
