package com.example.demo.controllers;

import com.example.demo.models.Job;
import com.example.demo.Person;
import com.example.demo.EduRepository;
import com.example.demo.Education;
import com.example.demo.SkillRepository;
import com.example.demo.Skills;
import com.example.demo.Work;
import com.example.demo.WorkRepository;
import com.example.demo.models.User;
import com.example.demo.repositories.JobRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.UserValidator;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Iterator;

/**
 * Created by student on 6/28/17.
 */
@Controller
public class HomeController {
    @Autowired
    EduRepository eduRepository;
    @Autowired
    WorkRepository workRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserService userService;
    @GetMapping ("/")
    public String mapSeeker()
    {

        return "index";
    }
    @RequestMapping ("/education")
    public String getResume(Model model)
    {
        model.addAttribute(new Education());
        return "education";
    }
    @PostMapping ("/education")
    public String postEducation(Model model, @ModelAttribute Education education, Principal principal) {
        String username = principal.getName();
        education.setEmail(username);
        eduRepository.save(education);
        model.addAttribute(new Education());
        return "education";
    }
    @RequestMapping("/work")
    public String getWork(Model model)
    {
        model.addAttribute(new Work());
        return "work";
    }
    @PostMapping ("/work")
    public String postWork(@ModelAttribute Work work, Model model, Principal principal)
    {
        work.setEmail(principal.getName());
        workRepository.save(work);
        model.addAttribute(new Work());
        return "work";
    }
    @RequestMapping("/skills")
    public String getSkills(Model model)
    {
        model.addAttribute(new Skills());
        return "skills";
    }
    @PostMapping("/skills")
    public String postSkills(@ModelAttribute Skills skills, Model model, Principal principal)
    {
        skills.setEmail(principal.getName());
        skillRepository.save(skills);
        model.addAttribute(new Skills());
        return "skills";
    }
    @GetMapping("/generateresume")
    public String generateResume(Model model, Principal principal,@ModelAttribute User user, @ModelAttribute Person person)
    {
        model.addAttribute(new Person());
        person.setEmail(principal.getName());
        model.addAttribute("email",principal.getName());
        Iterable<Education> educationList = eduRepository.findTop10ByEmail(principal.getName());
        model.addAttribute("educationList", educationList);
        Iterable<Work> workList = workRepository.findTop10ByEmail(principal.getName());
        model.addAttribute("workList", workList);
        Iterable<Skills> skillList = skillRepository.findTop20ByEmail(principal.getName());
        model.addAttribute("skillList", skillList);
        return "generateresume";
    }
    @GetMapping("/seekersearch")
    public String getSeekersearch(Model model)
    {
        model.addAttribute(new User());
        return "seekersearch";
    }
    @PostMapping("/seekersearch")
    public String postSeekersearch(@ModelAttribute User user, Model model, Principal principal){
        String userna = user.getUsername();
        //Iterable<User> userList=userRepository.findAllByUsername(userna);
        User us = userRepository.findByUsername(userna);
        //model.addAttribute("userList",userList);
        model.addAttribute("userList",us);
        /*
        Iterator<User> test = userList.iterator();
        while (test.hasNext())
        {
            System.out.println(test.next().getFirstName());
        }
        */
        return "seekerresult";
    }
    @GetMapping("/recreuitUserSearch")
    public String getUserSearch(Model model)
    {
        model.addAttribute(new User());
        return "recreuitUserSearch";
    }
    @PostMapping("/recreuitUserSearch")
    public String postUserSearch(User user, Model model){
        String userna = user.getUsername();
        User us = userRepository.findByUsername(userna);
        //model.addAttribute("userList",userList);
        model.addAttribute("userList",us);
        return "userresult";
    }
    @GetMapping("/recruitSchoolSearch")
    public String getSchoolSearch(Model model, Education education)
    {
        model.addAttribute("searchobject",new Education());
        return "recruitSchoolSearch";
    }
    @PostMapping("/recruitSchoolSearch")
    public String postSchoolSearch(Education education, Model model){
        String userna = education.getSchool();
        Iterable<Education> schoolList=eduRepository.findAllBySchool(userna);

        model.addAttribute("schoolList",schoolList);

        return "schoolresult";
    }
    @GetMapping("/recruitCompanySearch")
    public String getCompanySearch(Model model)
    {
        model.addAttribute(new Work());
        return "recruitCompanySearch";
    }
    @PostMapping("/recruitCompanySearch")
    public String postCompanySearch(Work work, Model model) {
        String userna = work.getCompany();
        Iterable<Work> companyList = workRepository.findByCompany(userna);
        model.addAttribute("companyList", companyList);
        return "companysearch";
    }
    @GetMapping("/recruitSkillSearch")
    public String getSkillSearch(Model model)
    {
        model.addAttribute(new Skills());
        return "recruitSkillSearch";
    }
    @PostMapping("/recruitSkillSearch")
    public String postSkillSearch(Skills skills, Model model) {
        String userna = skills.getSkill();
        Iterable<Skills> skillList = skillRepository.findBySkill(userna);
        model.addAttribute("skillList", skillList);
        return "skillsearch";
    }
    @RequestMapping("/recruiterjobpost")
    public String mapJob(Model model)
    {
        model.addAttribute(new Job());
        return "recruiterjobpost";
    }
    @PostMapping("/recruiterjobpost")
    public String postJob(Job job, Model model, Principal principal)
    {
        job.setRecruiter(principal.getName());
        jobRepository.save(job);
        model.addAttribute(new Job());
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        model.addAttribute("user", user);
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "index";
    }
    public UserValidator getUserValidator() {
        return userValidator;
    }
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }


}
