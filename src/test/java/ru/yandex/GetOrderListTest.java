package ru.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.client.ScooterOrderApi;
import ru.yandex.model.Order;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

public class GetOrderListTest {
    ScooterOrderApi orderApi;
    Order order;
    Response orderCreationResponse;

    @Before
    public void setUp() {
        orderApi = new ScooterOrderApi();
        order = new Order("Петр", "Черный", "Красная площадь",
                1, "+71112223344", 1, "2022-12-12",
                "1", new String[]{"BLACK"});
        orderCreationResponse = orderApi.createOrder(order);
    }

    @Test
    @DisplayName("Запрос по /api/v1/orders")
    @Description("Проверка размера получаемого массива. Должно быть больше 0, так как заранее создается заказ.")
    public void checkOrderListNotEmpty() {
        orderApi.getOrderList()
                .then()
                .assertThat()
                .body("orders", hasSize(greaterThan(0)));
    }

    @After
    public void tearDown() {
        String track = Integer.toString(orderCreationResponse.then().extract().path("track"));
        orderApi.cancelOrder(track);
    }
}
