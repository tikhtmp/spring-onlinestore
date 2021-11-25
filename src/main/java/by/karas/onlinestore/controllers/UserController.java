package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.CartDAO;
import by.karas.onlinestore.dao.ProductDAO;
import by.karas.onlinestore.dao.UserDAO;
import by.karas.onlinestore.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final CartDAO cartDAO;

    public UserController(ProductDAO productDAO, UserDAO userDAO, CartDAO cartDAO) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
    }

    @GetMapping()
    public String seeProducts(@RequestParam(value = "filter", required = false, defaultValue = "") String filter
            , Model model
            , Principal principal
            , Authentication authentication
    ) {

//        if (!authentication.isAuthenticated()) {
//            return "home/home_page";
//        }

        model.addAttribute("filter", filter);
        model.addAttribute("principalName", principal.getName());

        if(filter == null){
            model.addAttribute("products", productDAO.getAllProducts());
        } else model.addAttribute("products", productDAO.getProducts(filter));

        return "user/product_index";
    }
    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productDAO.getProduct(id));
        return "user/product_info";
    }

    @GetMapping("/{login}/cart")
    public String showCart(Model model, Principal principal) {
        String login = principal.getName();
        model.addAttribute("login", login);
//        model.addAttribute("products", cartDAO.getCartProducts(login));

//        model.addAttribute("cartProducts", productDAO.getCartProducts(cartDAO.getAllCartProducts()));
//        model.addAttribute("cartProducts", productDAO.getCartProducts(login));
        model.addAttribute("cart", cartDAO.getCartByUserLogin(login));
        return "user/cart";
    }

    @GetMapping("/addProductToCart/{id}")
    public String addProductToCart(@PathVariable("id") Long id, @PathVariable("quantity") Long quantity, Model model, Principal principal) {
        model.addAttribute("product", productDAO.getProduct(id));
        model.addAttribute("quantity", quantity);
        cartDAO.addProductToCart(principal.getName(), id, quantity);
        return "user/product_info";
    }

    @GetMapping("/{login}/edit")
    public String editUser(Model model, @PathVariable("login") String login) {
        model.addAttribute("user", userDAO.getUser(login));
//        System.out.println("user " + userDAO.getUser(login).getLogin());
        return "user/edit_user";
    }

    @PatchMapping
    public String update(@ModelAttribute("user") @Valid User user, Principal principal,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "user/edit_user";

        userDAO.update(principal.getName(), user);
        return "redirect:/user";
    }
}