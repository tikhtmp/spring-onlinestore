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
    private final Principal principal;


    public ProductsControllerAdmin(ProductDAO productDAO, UserDAO userDAO, CartDAO cartDAO, BCryptPasswordEncoder encoder, Principal principal) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
        this.principal = principal;
    }

//    private Long getPrincipalId(Principal principal) {
//        return userDAO.getUserByLogin(principal.getName()).getId();
//    }
    private Long getPrincipalId() {
        return userDAO.getUserByLogin(principal.getName()).getId();
    }

    @GetMapping("/admins/products")
    public String homeAdmins(){
        return "/" + getPrincipalId() + "/products";
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

    @GetMapping("/products/new")
    public String createProduct(@ModelAttribute Product product) {
        return "/admin/products/add_product";
    }

    @PostMapping("/products/new")
    public String createProduct(
            @ModelAttribute("product") @Valid Product product
            , BindingResult bindingResult
            , Model model) {

        if(bindingResult.hasErrors()){
            return "/admin/products/add_product";
        }

        product.setAuthor(getPrincipalId());

        productDAO.save(product);

        return "redirect: /admins/" + getPrincipalId() + "/products";
    }

}
