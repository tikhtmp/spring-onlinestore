package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.ProductDAO;
import by.karas.onlinestore.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductsController {


    private final ProductDAO productDAO;

    @Autowired
    public ProductsController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @GetMapping
    public String productIndex(Model model){
        model.addAttribute("products", productDAO.productIndex());
        return "products/index";
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model){
        model.addAttribute("product", productDAO.getProductById(id));
        return "products/show_product";
    }

    @GetMapping("/new")
    public String newProduct(@ModelAttribute("product") Product product){
        return "products/new";
    }

    @PostMapping
    public String createProduct(@ModelAttribute("product") Product product, Model model){
        productDAO.save(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id){
        model.addAttribute("product", productDAO.getProductById(id));
        return "products/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("product") Product product){
        productDAO.update(id, product);
        return "redirect:/products";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        productDAO.delete(id);
        return "redirect:/products";
    }
}
