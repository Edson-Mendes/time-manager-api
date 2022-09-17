package br.com.emendes.timemanagerapi.repository;

import br.com.emendes.timemanagerapi.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

  Page<Activity> findByEnabled(Pageable pageable, boolean enabled);

}
