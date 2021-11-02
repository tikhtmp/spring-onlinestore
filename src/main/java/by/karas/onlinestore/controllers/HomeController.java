package by.karas.onlinestore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String index() throws InterruptedException {
        System.out.println("HELLO!");
        wait(2000);
        return "redirect:/products/index";
    }

}
