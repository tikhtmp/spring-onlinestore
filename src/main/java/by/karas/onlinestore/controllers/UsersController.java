package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.UserDAO;
import by.karas.onlinestore.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserDAO userDAO;

    public UsersController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping()
    public String index(@RequestParam(value = "filter", required = false, defaultValue = "") String filter, Model model) {
        model.addAttribute("filter", filter);

        if(filter.equals("")){
            model.addAttribute("users", userDAO.getAllUsers());
        } else model.addAttribute("users", userDAO.getUsers(filter));

        return "users/user_index";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user){
        return "users/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "users/new";

        userDAO.save(user);

        return "redirect:/users";
    }

    @GetMapping("/{login}/edit")
    public String edit(Model model, @PathVariable("login") String login) {
        model.addAttribute("user", userDAO.getUser(login));
        return "users/edit";
    }

    @PatchMapping("/{login}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, @PathVariable("login") String login) {
        if (bindingResult.hasErrors())
            return "users/edit";

        userDAO.update(login, user);
        return "redirect:/users";
    }

    @DeleteMapping("/{login}")
    public String delete(@PathVariable("login") String login) {
        userDAO.delete(login);
        return "redirect:/users";
    }
}
