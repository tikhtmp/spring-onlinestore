package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.CartDAO;
import by.karas.onlinestore.dao.ProductDAO;
import by.karas.onlinestore.dao.UserDAO;
import by.karas.onlinestore.models.CartRecord;
import by.karas.onlinestore.models.Product;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admins")
public class ProductsControllerAdmin {
    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final CartDAO cartDAO;

    public ProductsControllerAdmin(ProductDAO productDAO, UserDAO userDAO, CartDAO cartDAO, BCryptPasswordEncoder encoder) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
    }

    private Long getPrincipalId(Principal principal) {
        return userDAO.getUserByLogin(principal.getName()).getId();
    }

    @GetMapping("/products")
    public String homeAdmins(Principal principal) {
        return "redirect:" + getPrincipalId(principal) + "/products";
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

        return "admin/products/product_index";
    }

    @GetMapping("products/{product_id}")
    public String showProduct(@PathVariable("product_id") Long productId, Model model, Principal principal) {
        model.addAttribute("product", productDAO.getProduct(productId));
        model.addAttribute("user_id", getPrincipalId(principal));
        return "admin/products/product_info";
    }

    @GetMapping("/products/new")
    public String createProduct(@ModelAttribute Product product) {
        return "/admin/products/add_product";
    }

    @PostMapping("/products/new")
    public String createProduct(
            @ModelAttribute("product") @Valid Product product
            , BindingResult bindingResult
            , Model model
            , Principal principal) {

        if (bindingResult.hasErrors()) {
            return "/admin/products/add_product";
        }

        Long userId = getPrincipalId(principal);
        product.setAuthor(userId);
        productDAO.save(product);

        return "redirect:/admins/products";
    }

    @GetMapping("/products/{product_id}/edit")
    public String editProduct(Model model, @PathVariable("product_id") Long productId) {
        model.addAttribute("product", productDAO.getProduct(productId));
        return "admin/products/edit_product";
    }

    @PatchMapping("/products/{product_id}")
    public String editProduct(
            @PathVariable("product_id") Long productId
            , @ModelAttribute("product") @Valid Product product
            , BindingResult bindingResult
            , Model model
            , Principal principal) {

        if (bindingResult.hasErrors()) {
            return "/admin/products/edit_product";
        }

        Long userId = getPrincipalId(principal);
        product.setAuthor(userId);
        productDAO.update(productId, product);

        return "redirect:/admins/products";
    }

    @DeleteMapping("/products/{product_id}")
    public String deleteProduct(@PathVariable("product_id") Long productId) {
        productDAO.delete(productId);
        return "redirect:/admins/products";
    }

    //---------------------------------- Cart

    @GetMapping("/{user_id}/cart/products")
    public String showCart(@PathVariable("user_id") Long userId, Model model) {
        model.addAttribute("cart", cartDAO.getCartByUserId(userId));
        model.addAttribute("userId", userId);
        return "admin/users/cart";
    }

    @GetMapping("/{user_id}/cart/products/{product_id}/edit")
    public String editCartRecord(
            @PathVariable("user_id") Long userId
            , @PathVariable("product_id") Long productId
            , Model model
    ) {

        CartRecord cartRecord = cartDAO.getCartRecord(userId, productId);
        Product product = productDAO.getProduct(productId);
        model.addAttribute("userId", userId);
        model.addAttribute("productId", productId);
        model.addAttribute("productName", product.getName());
        model.addAttribute("productDescription", product.getShortDescription());
        model.addAttribute("productPrice", product.getPrice());
        model.addAttribute("cartRecord", cartRecord);

        return "admin/users/edit_cart_record";
    }

    @GetMapping("/{user_id}/cart/products/{product_id}/new")
    public String createCartRecord(
            @ModelAttribute("cartRecord") CartRecord cartRecord
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

        return "admin/users/add_cart_record";
    }

    @PostMapping("/{user_id}/cart/products/{product_id}")
    public String createCartRecord(
            @ModelAttribute("cartRecord") @Valid CartRecord cartRecord
            , BindingResult bindingResult
            , @PathVariable("product_id") Long productId
            , @PathVariable("user_id") Long userId
            , Model model) {

        model.addAttribute("userId", userId);
        model.addAttribute("productId", productId);

        if (bindingResult.hasErrors()) {
            return "admin/users/add_cart_record";
        }

        cartRecord.setUser_id(userId);
        cartRecord.setProduct_id(productId);
        cartDAO.save(cartRecord);

        return "redirect:/admins/" + userId + "/cart/products";
    }

    @PatchMapping("/{user_id}/cart/products/{product_id}")
    public String updateCartRecord(
            @ModelAttribute("cartRecord") @Valid CartRecord cartRecord
            , BindingResult bindingResult
            , @PathVariable("product_id") Long productId
            , @PathVariable("user_id") Long userId
            , Model model) {

        model.addAttribute("userId", userId);
        model.addAttribute("productId", productId);
        cartRecord.setUser_id(userId);
        cartRecord.setProduct_id(productId);

        if (bindingResult.hasErrors()) {
            return "admin/users/edit_cart_record";
        }

        cartDAO.update(cartRecord, cartRecord.getQuantity());

        return "redirect:/admins/" + userId + "/cart/products";
    }

    @DeleteMapping("/{user_id}/cart/products/{product_id}")
    public String deleteProductFromCart(
            @PathVariable("user_id") Long userId
            , @PathVariable("product_id") Long productId) {

        cartDAO.delete(userId, productId);

        return "redirect:/admins/" + userId + "/cart/products";

    }
    //----------------------------------

}
