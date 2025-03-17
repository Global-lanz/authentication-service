package lanz.global.authenticationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Table
@Entity(name = "user_group")
@Getter
@Setter
@NoArgsConstructor
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_group_id")
    private UUID userGroupId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "company_id")
    private UUID companyId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_group_rule", joinColumns = {@JoinColumn(name = "user_group_id")},
            inverseJoinColumns = {@JoinColumn(name = "rule_id")})
    private List<Rule> rules;


    public UserGroup(String name, String description, UUID companyId) {
        this.name = name;
        this.description = description;
        this.companyId = companyId;
    }
}
