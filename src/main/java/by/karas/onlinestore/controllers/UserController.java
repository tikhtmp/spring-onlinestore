package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.CartDAO;
import by.karas.onlinestore.dao.ProductDAO;
import by.karas.onlinestore.dao.UserDAO;
import by.karas.onlinestore.models.CartRecord;
import by.karas.onlinestore.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {
    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final CartDAO cartDAO;

    public UserController(ProductDAO productDAO, UserDAO userDAO, CartDAO cartDAO) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
    }

    @GetMapping("/products")
    public String seeProducts(Principal principal) {
        Long userId = userDAO.getUserByLogin(principal.getName()).getId();
        return "redirect:/users/" + userId + "/products";
    }

    @GetMapping("/{userId}/products")
    public String seeProducts(@PathVariable("userId") Long userId, @RequestParam(value = "filter", required = false, defaultValue = "") String filter
            , Model model
            , Principal principal
            , Authentication authentication
    ) {

//        if (!authentication.isAuthenticated()) {
//            return "home/home_page";
//        }

        model.addAttribute("filter", filter);
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("userId", userId);

        if (filter == null) {
            model.addAttribute("products", productDAO.getAllProducts());
        } else model.addAttribute("products", productDAO.getProducts(filter));

        return "user/product_index";
    }

    @GetMapping("/{user_id}/edit")
    public String editUser(Model model, @PathVariable("user_id") Long userId) {
        model.addAttribute("user", userDAO.getUserById(userId));
        return "user/edit_user";
    }

    @GetMapping("/{user_id}/cart")
    public String showCart(@PathVariable("user_id") Long userId, Model model, Principal principal) {
        model.addAttribute("login", principal.getName());
        model.addAttribute("cart", cartDAO.getCartByUserId(userId));
        model.addAttribute("userId", userId);
        return "user/cart";
    }

    @GetMapping("/products/{product_id}")
    public String showProductDetails(@PathVariable("product_id") Long product_id, Model model, Principal principal) {
        model.addAttribute("product", productDAO.getProduct(product_id));
        model.addAttribute("userId", userDAO.getUserByLogin(principal.getName()).getId());
//        model.addAttribute("quantity", cartDAO.getQuantityFromCart(principal.getName(), id));
        return "user/product_info";
    }


    @GetMapping("/{user_id}/products/{product_id}")
    public String addProductToCart(
            @PathVariable("user_id") Long user_id
            , @PathVariable("product_id") Long product_id
            , Model model
            , Principal principal) {

        model.addAttribute("product", productDAO.getProduct(product_id));
        model.addAttribute("userId", user_id);
        model.addAttribute("productId", product_id);

        Long quantity = cartDAO.getQuantityFromCart(user_id, product_id);
        if (quantity == 0) quantity++;
        model.addAttribute("quantity", quantity);

        return "user/addProductToCart";
    }

    @PostMapping()
    public String createCartRecord(
            Principal principal
            , @RequestParam("quantity") Long quantity
            , @RequestParam("product_id") Long product_id
            , Model model
//            , BindingResult bindingResult
    ) {

        Long user_id = userDAO.getUserByLogin(principal.getName()).getId();
        model.addAttribute("quantity", quantity);
        CartRecord newCartRecord = new CartRecord(user_id, product_id, quantity);
        cartDAO.save(newCartRecord);

        return "redirect:/users/" + user_id + "/cart";
    }

    @DeleteMapping("/{product_id}")
    public String deleteProductFromCart(@PathVariable("product_id") Long productId, Principal principal){

        Long userId = userDAO.getUserByLogin(principal.getName()).getId();
        cartDAO.delete(userId, productId);

        return "redirect:/users/" + userId + "/cart";
    }
//    @PatchMapping("/addProductToCart/{id}")
//    public String updateCartRecord(@ModelAttribute("cartRecord") @Valid CartRecord cartRecord, Principal principal,
//                                   BindingResult bindingResult) {
//        return "redirect:/users/cart";

//    }

//    @PatchMapping("/{user_id}")
//    public String update(@ModelAttribute("user") @Valid User user
//            , Principal principal
//            , @PathVariable("user_id") Long userId
//            , BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors())
//            return "user/edit_user";
//
//        userDAO.update(principal.getName(), user);
//        return "redirect:/users/" + userId + "/products";
//    }
}