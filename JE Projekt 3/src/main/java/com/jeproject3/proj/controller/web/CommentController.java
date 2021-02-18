package com.jeproject3.proj.controller.web;


import com.jeproject3.proj.domain.*;
import com.jeproject3.proj.service.*;
import com.jeproject3.proj.domain.Comment;
import com.jeproject3.proj.service.CommentManager;
import com.jeproject3.proj.service.PostManager;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Controller("commentwebcontroller")
public class CommentController {
    private PostManager pm;
    private CommentManager cm;

    @Autowired
    public CommentController(PostManager pm, CommentManager cm)  {
        this.cm = cm;
        this.pm = pm;

    }

    @GetMapping("/comment/post/{idP}")
    public String commentForm(@PathVariable("idP") long idP, Model model){
        model.addAttribute("idOfPost",idP);
        model.addAttribute("comment", new Comment());
        return "commentForm";
    }

    @PostMapping("/comment/post/{idP}")
    public String processOrderComment(@PathVariable("idP") long idP, @Valid Comment comment, Errors errors, Model model)
    {
        if(errors.hasErrors()){
            model.addAttribute("idOfPost",idP);
            return "commentForm";
        }
        comment.setLinkedPost(pm.findById(idP));
        cm.addComment(comment);
        log.info("comment created: " + comment + idP);
        return "redirect:/post/" + idP;
    }

    @RequestMapping("/comment/delete/{idC}")
    public String deleteComment(@PathVariable("idC") long idC) {
        Comment toRemove = cm.findById(idC);
        long idP = toRemove.getLinkedPost().getId();
        toRemove.getLinkedPost().getCommentsLinked().remove(toRemove);
        cm.remove(idC);
        return "redirect:/post/" + idP;
    }

    @GetMapping("/comment/edit/{idC}")
    public String commentReplaceForm(@PathVariable("idC") long idC, Model model){
        Comment comment = cm.findById(idC);
        model.addAttribute("idOfPost",comment.getLinkedPost().getId());
        model.addAttribute("comment", comment);
        return "commentEdit";
    }


}
