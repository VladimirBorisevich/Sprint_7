package ru.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.client.ScooterOrderApi;
import ru.yandex.model.Order;

import static org.hamcrest.Matchers.hasKey;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    ScooterOrderApi orderApi;
    Response orderCreationResponse;
    String firstName;
    String lastName;
    String address;
    int metroStation;
    String phone;
    int rentTime;
    String deliveryDate;
    String comment;
    String[] color;

    public OrderCreateTest(String firstName, String lastName, String address,
                           int metroStation, String phone, int rentTime,
                           String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"Петр", "Черный", "Красная площадь", 1, "+71112223344", 1, "2022-12-12", "1", new String[]{"BLACK"}},
                {"Петр", "Серый", "Кремль", 2, "+71212223344", 2, "2022-11-11", "2", new String[]{"Grey"}},
                {"Петр", "СероЧерный", "Арбат", 3, "+71312223344", 3, "2022-10-10", "3", new String[]{"BLACK", "Grey"}},
                {"Петр", "Никакой", "ул. Льва Толстого, 16, Москва, 119021",
                        4, "+71412223344", 4, "2022-09-09", "4", new String[]{}},
        };
    }

    @Before
    public void setUp() {
        orderApi = new ScooterOrderApi();
    }

    @Test
    @DisplayName("Проверка наличия поля track в /api/v1/orders")
    @Description("Создание заказа и проверка наличия поля track в ответе на запрос")
    public void checkOrderCreationHasTrack() {
        Order order = new Order(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        orderCreationResponse = orderApi.createOrder(order);
        orderCreationResponse.then().assertThat().body("$", hasKey("track"));
    }

    @After
    public void tearDown() {
        String track = Integer.toString(orderCreationResponse.then().extract().path("track"));
        orderApi.cancelOrder(track);
    }
}
