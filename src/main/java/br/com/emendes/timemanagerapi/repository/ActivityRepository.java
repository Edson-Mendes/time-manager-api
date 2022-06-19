package br.com.emendes.timemanagerapi.repository;

import br.com.emendes.timemanagerapi.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
