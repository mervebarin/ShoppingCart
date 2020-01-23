package com.example.demo.infrastructure;

import com.example.demo.application.request.PrepareShoppingCartRequest;
import com.example.demo.application.response.Response;
import com.example.demo.domain.model.entity.Coupon;
import com.example.demo.domain.model.enumtype.DiscountType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestShoppingCartControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    protected int serverPort;

    @Test
    public void should_prepare_shopping_cart_for_checkout() {
        //given
        HashMap<Long, Integer> productIdQuantityMap = new HashMap<>();
        productIdQuantityMap.put(1L, 3);
        productIdQuantityMap.put(2L, 1);
        productIdQuantityMap.put(3L, 1);
        PrepareShoppingCartRequest prepareShoppingCartRequest = new PrepareShoppingCartRequest();

        Coupon coupon = new Coupon(10.0, 5.0, DiscountType.AMOUNT);

        prepareShoppingCartRequest.setProductIdQuantityMap(productIdQuantityMap);
        prepareShoppingCartRequest.setCoupon(coupon);

        //when
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Response> responseEntity = testRestTemplate
                .exchange("http://localhost:" + serverPort + "/shopping-cart/prepare", HttpMethod.POST, new HttpEntity<>(prepareShoppingCartRequest, headers), Response.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Response response = responseEntity.getBody();
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat((response.getTotalPriceOfShoppingCart())).isEqualTo(BigDecimal.valueOf(73.47));
    }
}