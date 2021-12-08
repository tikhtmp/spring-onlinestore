package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.CartDAO;
import by.karas.onlinestore.dao.ProductDAO;
import by.karas.onlinestore.dao.UserDAO;
import by.karas.onlinestore.models.CartRecord;
import by.karas.onlinestore.models.Product;
import by.karas.onlinestore.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder encoder;


    public UserController(ProductDAO productDAO, UserDAO userDAO, CartDAO cartDAO, BCryptPasswordEncoder encoder) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
        this.encoder = encoder;
    }

    private Long getPrincipalId(Principal principal) {
        return userDAO.getUserByLogin(principal.getName()).getId();
    }

    @GetMapping("/products")
    public String seeProducts(Principal principal) {
        return "redirect:/users/" + getPrincipalId(principal) + "/products";
    }

    @GetMapping("/{user_id}/products")
    public String seeProducts(
              @PathVariable("user_id") Long userId
            , @RequestParam(value = "filter", required = false, defaultValue = "") String filter
            , Model model) {

        model.addAttribute("login", userDAO.getUserById(userId).getLogin());
        model.addAttribute("filter", filter);
        model.addAttribute("user_id", userId);

        if (filter == null) {
            model.addAttribute("products", productDAO.getAllProducts());
        } else model.addAttribute("products", productDAO.getProducts(filter));

        return "user/product_index";
    }

    @GetMapping("/products/{product_id}")
    public String showProductDetails(@PathVariable("product_id") Long product_id, Model model, Principal principal) {
        model.addAttribute("product", productDAO.getProduct(product_id));
        model.addAttribute("userId", getPrincipalId(principal));
        return "user/product_info";
    }

    @GetMapping("/cart/products/{product_id}/edit")
    public String showCartProductDetails(@PathVariable("product_id") Long product_id, Model model, Principal principal) {
        model.addAttribute("product", productDAO.getProduct(product_id));
        model.addAttribute("userId", getPrincipalId(principal));
        return "user/edit_cart_record";
    }

    @GetMapping("/{user_id}/cart/products")
    public String showCart(@PathVariable("user_id") Long userId, Model model) {
        model.addAttribute("cart", cartDAO.getCartByUserId(userId));
        model.addAttribute("userId", userId);
        return "user/cart";
    }


    @GetMapping("/{user_id}/cart/products/{product_id}")
    public String addProductToCart(
            @ModelAttribute CartRecord cartRecord
            , @PathVariable("user_id") Long userId
            , @PathVariable("product_id") Long productId
            , Model model) {

        Product product = productDAO.getProduct(productId);
        model.addAttribute("userId", userId);
        model.addAttribute("productId", productId);
        model.addAttribute("productName", product.getName());
        model.addAttribute("productDescription", product.getShortDescription());
        model.addAttribute("productPrice", product.getPrice());
        model.addAttribute("cartRecord", cartRecord);

        return "user/addProductToCart";
    }

    @PostMapping("/{user_id}/cart/products/{product_id}")
    public String createCartRecord(
            @ModelAttribute("cartRecord")  @Valid CartRecord cartRecord
            , BindingResult bindingResult
            , @PathVariable("product_id") Long productId
            , @PathVariable("user_id") Long userId
            , Model model) {

        model.addAttribute("userId", userId);
        model.addAttribute("productId", productId);

        if (bindingResult.hasErrors()) {
            return "user/addProductToCart";
        }

        cartRecord.setUser_id(userId);
        cartRecord.setProduct_id(productId);
        cartDAO.save(cartRecord);

        return "redirect:/users/" + userId + "/cart/products";
    }



    @DeleteMapping("/{user_id}/cart/products/{product_id}")
    public String deleteProductFromCart(@PathVariable("product_id") Long productId, Principal principal) {
        Long userId = getPrincipalId(principal);
        cartDAO.delete(userId, productId);

        return "redirect:/users/" + userId + "/cart/products";
    }

    @GetMapping("/{user_id}/cart/products/{product_id}/edit")
    public String editCartRecord(
              @PathVariable("product_id") Long productId
            , @PathVariable("user_id") Long userId
            , Model model) {

        CartRecord cartRecord = cartDAO.getCartRecord(userId, productId);
        Product product = productDAO.getProduct(productId);

        model.addAttribute("userId", userId);
        model.addAttribute("productId", productId);
        model.addAttribute("productName", product.getName());
        model.addAttribute("productDescription", product.getShortDescription());
        model.addAttribute("productPrice", product.getPrice());
        model.addAttribute("cartRecord", cartRecord);
        return "user/edit_cart_record";
    }

    @PatchMapping("/{user_id}/cart/products/{product_id}")
    public String updateCartRecord(
              @ModelAttribute("cartRecord")  @Valid CartRecord cartRecord
            , BindingResult bindingResult
            , @PathVariable("product_id") Long productId
            , @PathVariable("user_id") Long userId
            , Model model) {

        model.addAttribute("userId", userId);
        model.addAttribute("productId", productId);
        cartRecord.setUser_id(userId);
        cartRecord.setProduct_id(productId);

        if (bindingResult.hasErrors()) {
            return "user/edit_cart_record";
        }

        cartDAO.update(cartRecord, cartRecord.getQuantity());

        return "redirect:/users/" + userId + "/cart/products";
    }


    @GetMapping("/{user_id}/edit")
    public String editUser(Model model, @PathVariable("user_id") Long userId) {
        model.addAttribute("user", userDAO.getUserById(userId));
        model.addAttribute("userId", userId);
        return "user/edit_user";
    }

    @PatchMapping("/{user_id}")
    public String updateUser(@ModelAttribute("user") @Valid User user
            , BindingResult bindingResult
            , Principal principal
            , @PathVariable("user_id") Long userId
            , Model model) {

        model.addAttribute("userId", userId);

        if (bindingResult.hasErrors())
            return "user/edit_user";


        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.update(userId, user);

        return "redirect:/users/" + userId + "/products";
    }
}