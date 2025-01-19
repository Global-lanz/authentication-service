package lanz.global.authenticationservice.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Table
@Entity(name = "user_group")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_group_id")
    private UUID userGroupId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(joinColumns = {@JoinColumn(name = "user_group_id")},
            inverseJoinColumns = {@JoinColumn(name = "rule_id")})
    private List<Rule> rules;
}
