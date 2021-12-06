package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.CartDAO;
import by.karas.onlinestore.dao.ProductDAO;
import by.karas.onlinestore.dao.UserDAO;
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
        model.addAttribute("users", userDAO.getAllUsers());

        if (filter == null) {
            model.addAttribute("products", productDAO.getAllProducts());
        } else model.addAttribute("products", productDAO.getProducts(filter));

        return "admin/products/product_index";
    }

    @GetMapping("products/{product_id}")
    public String show(@PathVariable("product_id") Long productId, Model model) {
        model.addAttribute("product", productDAO.getProduct(productId));
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

    @GetMapping("products/{product_id}/edit")
    public String edit(Model model, @PathVariable("product_id") Long productId) {
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


}
