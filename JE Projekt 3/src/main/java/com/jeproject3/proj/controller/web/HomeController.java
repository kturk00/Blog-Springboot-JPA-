package com.jeproject3.proj.controller.web;

import com.jeproject3.proj.domain.UserPersist;
import com.jeproject3.proj.service.UserManager;
import com.jeproject3.proj.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final StorageService storageService;

    private final PasswordEncoder passwordEncoder;

    private UserManager userManager;

    @Autowired
    public HomeController(StorageService storageService, UserManager userManager, PasswordEncoder passwordEncoder) {
        this.storageService = storageService;
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        return "home";
    }

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserPersist());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("user") UserPersist user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", "Failed");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");;
        if(bindingResult.hasErrors() || !user.getPassword().equals(user.getMatchingPassword())
                || userManager.isUsernameTaken(user.getUsername())){
            model.addAttribute("user", user);
            return "register";
        }
        redirectAttributes.addFlashAttribute("message", "Success");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMatchingPassword(passwordEncoder.encode(user.getMatchingPassword()));
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        System.out.println(user);
        userManager.addUser(user);
        return "redirect:/login";
    }


    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
