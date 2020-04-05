package tda.springframework.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.springframework.microservice.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTaskByUserId(Long userId);
}
