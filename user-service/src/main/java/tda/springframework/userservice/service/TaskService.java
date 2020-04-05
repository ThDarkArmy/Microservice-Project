package tda.springframework.userservice.service;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tda.springframework.userservice.model.Task;

import java.util.List;

//@FeignClient(name = "task-service", url = "http://localhost:8083/")
@FeignClient(name = "task-service")
//@RibbonClient(name = "task-service")
@Service
public interface TaskService {
    @RequestMapping("/v1/task/{userId}")
    ResponseEntity<List<Task>> userTasks(@PathVariable Long userId);
}

