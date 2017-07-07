package com.example.demo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.Job;

import java.util.List;

/**
 * Created by student on 7/6/17.
 */
public interface JobRepository extends CrudRepository<Job, Long> {
    //@Query(value = "select job.title, job.skills, job.salary from job,user,skills where skills.email=?0 and job.skills=skills.skill;",nativeQuery = true)
    public List<Job> findAllBySkills(String username);

}
