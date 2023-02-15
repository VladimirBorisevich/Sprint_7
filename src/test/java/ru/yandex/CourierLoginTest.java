package ru.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.client.ScooterCourierApi;
import ru.yandex.model.Courier;

import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
    Courier courier;
    Response loginCourierResponse;
    ScooterCourierApi courierApi = new ScooterCourierApi();

    @Test
    @DisplayName("Проверка статуса 200 для /api/v1/courier/login")
    @Description("Запрос с корректными данными логина и пароля")
    public void checkCorrectLoginStatusCode200() {
        courier = new Courier("Pikachu", "Detective", "Pika");
        courierApi.createCourier(courier);
        loginCourierResponse = courierApi.loginCourier(courier);
        loginCourierResponse.then().assertThat().statusCode(200);
        loginCourierResponse.then().assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Проверка статуса 400 для /api/v1/courier/login")
    @Description("Запрос с отсутвующим полем пароля")
    public void checkMissingPasswordStatusCode400() {
        courier = new Courier("Pikachu", null, "Pika");
        courierApi.createCourier(courier);
        loginCourierResponse = courierApi.loginCourier(courier);
        loginCourierResponse.then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("Проверка статуса 400 для /api/v1/courier/login")
    @Description("Запрос с отсутвующим полем логина")
    public void checkMissingLoginStatusCode400() {
        courier = new Courier(null, "Detective", "Pika");
        courierApi.createCourier(courier);
        loginCourierResponse = courierApi.loginCourier(courier);
        loginCourierResponse.then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("Проверка статуса 404 для /api/v1/courier/login")
    @Description("Запрос с данными курьера, который не был создан")
    public void checkIncorrectFieldStatusCode404() {
        courier = new Courier("Pikapcha", "Detective", "Pika");
        loginCourierResponse = courierApi.loginCourier(courier);
        loginCourierResponse.then().assertThat().statusCode(404);
    }

    @After
    public void tearDown() {
        int statusCode = loginCourierResponse.then().extract().statusCode();
        if (statusCode == 200) {
            String id = courierApi.extractId(loginCourierResponse);
            courierApi.deleteCourier(id);
        }
    }
}