package tda.springframework.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tda.springframework.userservice.model.Task;
import tda.springframework.userservice.model.User;
import tda.springframework.userservice.model.UserTask;
import tda.springframework.userservice.repository.UserRepository;
import tda.springframework.userservice.service.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private UserRepository userRepository;
    private RestTemplate restTemplate;
    private TaskService taskService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository userRepository, RestTemplate restTemplate, TaskService taskService) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user){
        logger.info("New User {}", user);
        return userRepository.save(user);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getUser(@PathVariable Long userId){
        logger.info("User Id {}", userId);
        return userRepository.findById(userId);
    }

    @GetMapping("/{userId}/task")
    public ResponseEntity<?> userTasks(@PathVariable Long userId){
        Optional<User> user = userRepository.findById(userId);
        if(user!=null){
            logger.info("User Found {}", user);
            /*ResponseEntity<List<Task>> tasks = restTemplate.exchange("http://localhost:8083/v1/task/"+userId,
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){});*/
            ResponseEntity<List<Task>> tasks = taskService.userTasks(userId);
            return new ResponseEntity<UserTask>(new UserTask(user.get(), tasks.getBody()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}

