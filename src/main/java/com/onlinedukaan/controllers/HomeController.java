package com.onlinedukaan.controllers;

import com.onlinedukaan.model.CartItem;
import com.onlinedukaan.model.Product;
import com.onlinedukaan.model.User;
import com.onlinedukaan.repository.CartItemRepository;
import com.onlinedukaan.service.ProductService;
import com.onlinedukaan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    ProductService productService;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(Model model) {

        return "index";
    }

    @GetMapping("/shop")
    public String shop(Model model, @PathParam(value = "category")String category) {
        List<Product> products;
        if(category != null && category.equals("stationary"))
        {
            products = productService.getStationaryProducts();
            model.addAttribute("products",products);
            return "shop";
        }
        if(category != null && category.equals("grocery"))
        {
            products = productService.getGroceryProducts();
            model.addAttribute("products",products);
            return "shop";
        }
        products = productService.getAllProduct();
        model.addAttribute("products", products);

        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(@PathVariable long id, Model model, Principal principal) {
        Product product = productService.getProduct(id);
        System.out.println(principal);
        User user = userService.findUserByEmail(principal.getName());
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user,product);
        if (cartItem == null)
        {
            cartItem = new CartItem();
        }
        model.addAttribute("product", product);
        model.addAttribute("cartItem",cartItem);
        return "viewproduct";
    }
}
