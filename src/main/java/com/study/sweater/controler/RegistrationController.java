package com.study.sweater.controler;

import com.study.sweater.domain.Role;
import com.study.sweater.domain.User;
import com.study.sweater.repos.UserRepo;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user,Map<String ,Object> model){

        User userFromDb = userRepo.findByUsername(user.getUsername());
        if(userFromDb!= null){
            model.put("message","User exists!");
            return "registration";

        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";

    }
}