package com.web.community.repository;

import com.web.community.domain.Board;
import com.web.community.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
public interface BoardRepository extends JpaRepository<Board, Long> {

    public Board findByUser(User user);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    <S extends Board> S save(S entity);
}
