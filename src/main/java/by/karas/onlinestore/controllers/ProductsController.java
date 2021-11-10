package by.karas.onlinestore.controllers;

import by.karas.onlinestore.dao.ProductDAO;
import by.karas.onlinestore.models.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {


    private final ProductDAO productDAO;

    @Autowired
    public ProductsController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("products", productDAO.index());
        return "products/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productDAO.getProduct(id));
        return "products/show";
    }

    @GetMapping("/new")
    public String newProduct(@ModelAttribute("product") Product product) {
        return "products/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("product") @Valid Product product,
                         BindingResult bindingResult) {


        if (bindingResult.hasErrors())
            return "products/new";
        productDAO.save(product);

        return "redirect:/products";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("product", productDAO.getProduct(id));
        return "products/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("product") @Valid Product product,
                         BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "products/edit";

        productDAO.update(id, product);
        return "redirect:/products";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        productDAO.delete(id);
        return "redirect:/products";
    }
}
