package com.example.demo.infrastructure;

import com.example.demo.application.request.PrepareShoppingCartRequest;
import com.example.demo.application.response.Response;
import com.example.demo.domain.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RestShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public RestShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/shopping-cart/prepare")
    @ResponseStatus(HttpStatus.CREATED)
    public Response prepareShoppingCartForCheckout(@RequestBody PrepareShoppingCartRequest prepareShoppingCartRequest) {
        return shoppingCartService.prepareShoppingCartForCheckout(prepareShoppingCartRequest.getProductIdQuantityMap(), prepareShoppingCartRequest.getCoupon());
    }
}