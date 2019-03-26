package com.study.sweater.controler;

import com.study.sweater.domain.Message;
import com.study.sweater.domain.User;
import com.study.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String,Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String,Object> model){
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages",messages);
        return "main";

    }

    @PostMapping("/addMessage")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    )throws IOException {
        message.setAuthor(user);

        if(bindingResult.hasErrors()){

            Map<String, String> errorMap = ControllerUtil.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("message",message);

        } else {
            saveFile(message, file);
            model.addAttribute("message",null);
            messageRepo.save(message);
        }
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages",messages);
        return "main";
    }

    private void saveFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            message.setFilename(resultFilename);
        }
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter,Map<String,Object> model){

        Iterable<Message> messages;
        if(filter!= null&&!filter.isEmpty()) messages = messageRepo.findByTag(filter);
         else messages = messageRepo.findAll();

        model.put("messages",messages);
        return "main";
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @PathVariable("user") int message
            //@RequestParam(required = false) Message message
    ){
        Set<Message> messages = user.getMessages();

        model.addAttribute("userChannel",user);
        model.addAttribute("subscriptionsCount",user.getSubscriptions().size());
        model.addAttribute("subscribersCount",user.getSubscribers().size());
        model.addAttribute("isSubscriber",user.getSubscribers().contains(currentUser));

        model.addAttribute("messages",messages);
        model.addAttribute("message",messageRepo.findById(Long.valueOf(message)));
        model.addAttribute("isCurrentUser",currentUser.equals(user));

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam int message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Message message1 = messageRepo.findById(Long.valueOf(message));
        if (message1.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message1.setText(text);
            }

            if (!StringUtils.isEmpty(tag)) {
                message1.setTag(tag);
            }

            saveFile(message1, file);

            messageRepo.save(message1);
        }

        return "redirect:/user-messages/" + user+"?message=0";
    }


}
