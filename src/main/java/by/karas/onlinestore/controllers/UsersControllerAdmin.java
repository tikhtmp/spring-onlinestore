package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.CartDAO;
import by.karas.onlinestore.dao.ProductDAO;
import by.karas.onlinestore.dao.UserDAO;
import by.karas.onlinestore.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("admins/{admin_id}/users")
public class UsersControllerAdmin {
    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final CartDAO cartDAO;
    private final BCryptPasswordEncoder encoder;

    public UsersControllerAdmin(ProductDAO productDAO, UserDAO userDAO, CartDAO cartDAO, BCryptPasswordEncoder encoder, BCryptPasswordEncoder encoder1) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
        this.encoder = encoder1;
    }

    private Long getPrincipalId(Principal principal) {
        return userDAO.getUserByLogin(principal.getName()).getId();
    }

    @ModelAttribute("login")
    public String getAdminLogin(Principal principal) {
        return userDAO.getUserById(getPrincipalId(principal)).getLogin();
    }

    @ModelAttribute("admin_id")
    public String getAdminId(Principal principal) {
        return getPrincipalId(principal).toString();
    }

    @GetMapping()
    public String index(
            @RequestParam(value = "filter", required = false, defaultValue = "") String filter
            , Model model) {

        model.addAttribute("filter", filter);

        if (filter == null) {
            model.addAttribute("users", userDAO.getAllUsers());
        } else model.addAttribute("users", userDAO.getUsers(filter));

        return "admin/users/user_index";
    }

    @GetMapping("/{user_id}")
    public String show(@PathVariable("user_id") Long userId, Model model) {
        model.addAttribute("user", userDAO.getUserById(userId));
        return "admin/users/user_info";
    }

    @GetMapping("/{user_id}/update")
    public String updateUser(
            @PathVariable("user_id") Long userId
            , Model model) {
        model.addAttribute("user", userDAO.getUserById(userId));

        return "admin/users/edit_user";
    }

    @PatchMapping("/{user_id}")
    public String updateUser(
            @ModelAttribute("user") @Valid User user
            , BindingResult bindingResult
            , @PathVariable("user_id") Long userId
            , Model model) {


        model.addAttribute("user_id", userId);
        user.setId(userId);

        if (bindingResult.hasErrors())
            return "admin/users/edit_user";

        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.update(userId, user);

        return "redirect:/admins/{admin_id}/users";
    }

    @DeleteMapping("/{user_id}")
    public String deleteUser(@PathVariable("user_id") Long userId, Principal principal) {

        Long principalId = getPrincipalId(principal);
        String url = (userId == principalId)? "redirect:/logout" : "redirect:/admins/{admin_id}/users";
        userDAO.delete(userId);

        return url;
    }
}
