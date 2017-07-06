package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.Job;
/**
 * Created by student on 7/6/17.
 */
public interface JobRepository extends CrudRepository<Job, Long> {

}
