package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.ProductDAO;
import by.karas.onlinestore.dao.Role;
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
@RequestMapping("/home")
public class HomeController {
    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final BCryptPasswordEncoder encoder;

    public HomeController(ProductDAO productDAO, UserDAO userDAO, BCryptPasswordEncoder encoder) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.encoder = encoder;
    }

    @GetMapping("/products")
    public String home(@RequestParam(value = "filter", required = false, defaultValue = "") String filter
            , Model model) {

        model.addAttribute("filter", filter);

        if(filter == null){
            model.addAttribute("products", productDAO.getAllProducts());
        } else model.addAttribute("products", productDAO.getProducts(filter));

        return "home/home_page";
    }

    @GetMapping("/users/new")
    public String createUser(@ModelAttribute("user") User user) {
        return "home/add_user";
    }

    @PostMapping("/users/new")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "home/add_user";

        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.save(user);

        return "redirect:/home/products";
    }

    @GetMapping("/products/{id}")
    public String seeProductDetails(@PathVariable("id") Long id, Model model){
        model.addAttribute("product", productDAO.getProduct(id));
        return "home/product_info";
    }

    @GetMapping("/auth")
    public String authorisation(Principal principal) {
        Long userId = userDAO.getUserByLogin(principal.getName()).getId();
        User currentUser = userDAO.getUserById(userId);

        if(currentUser.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/admins/" + userId + "/products";

        return "redirect:/users/" + userId + "/products";
    }

}
