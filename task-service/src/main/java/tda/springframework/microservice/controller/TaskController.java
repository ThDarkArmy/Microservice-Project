package tda.springframework.microservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tda.springframework.microservice.model.Category;
import tda.springframework.microservice.model.Task;
import tda.springframework.microservice.repository.CategoryRepository;
import tda.springframework.microservice.repository.TaskRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("v1/task")
public class TaskController {
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;

    public TaskController(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Task> addTask(@RequestBody Task task){
        logger.info("Task {} ", task);
        Set<Category> categories = task.getCategories();
        Set<Category> taskCategories = new HashSet<>();

        categories.stream().forEach(category -> {
            Category existingCategory = categoryRepository.findByName(category.getName());
            if(existingCategory == null){
                existingCategory = categoryRepository.save(category);
                taskCategories.add(existingCategory);
            }
            existingCategory.setTasks(new HashSet<>());
            taskCategories.add(existingCategory);
        });

        task.setCategories(taskCategories);
        Task savedTask = taskRepository.save(task);

        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        return new ResponseEntity<>(taskRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Task>> getTaskByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(taskRepository.findTaskByUserId(userId), HttpStatus.OK);
    }

}
