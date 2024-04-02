package dev.danvega.runnerz.run;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface RunRepository extends ListCrudRepository<Run, Integer> {
    List<Run> findAllByLocation(String location);
}
