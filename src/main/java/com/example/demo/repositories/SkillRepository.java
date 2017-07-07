package com.example.demo;

import com.example.demo.Skills;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by student on 6/28/17.
 */
public interface SkillRepository extends CrudRepository<Skills, Integer> {
    @Query
    public List<Skills> findTop20ByEmail(String email);
    @Query
    public List<Skills> findBySkill(String skill);
    @Query
    public List<Skills> findAllBySkill(String skill);
    List<Skills> findAllByEmail(String email);
}
