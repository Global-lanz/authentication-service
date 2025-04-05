package lanz.global.authenticationservice.repository;

import lanz.global.authenticationservice.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {

    List<UserGroup> findAllByCompanyId(UUID companyId);
    List<UserGroup> findAllByCompanyIdAndUserGroupIdIn(UUID companyId, Set<UUID> userGroupIds);
}
