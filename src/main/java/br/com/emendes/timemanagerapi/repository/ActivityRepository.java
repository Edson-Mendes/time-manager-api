package br.com.emendes.timemanagerapi.repository;

import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

  Page<Activity> findByUserAndStatusIsNot(Pageable pageable, User user, Status status);

  Optional<Activity> findByIdAndUser(long id, User user);
}
