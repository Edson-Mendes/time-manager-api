package br.com.emendes.timemanagerapi.repository;

import br.com.emendes.timemanagerapi.model.Interval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalRepository extends JpaRepository<Interval, Long> {

}
