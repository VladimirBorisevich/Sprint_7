package ru.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.client.ScooterCourierApi;
import ru.yandex.model.Courier;

import static org.hamcrest.Matchers.equalTo;


public class CourierCreateTest {
    Courier courier;
    Response createdCourier;
    ScooterCourierApi courierApi = new ScooterCourierApi();

    @Test
    @DisplayName("Проверка статуса 201 для /api/v1/courier")
    @Description("Запрос с корректными данными логина и пароля")
    public void checkCorrectCreationStatusCode201() {
        courier = new Courier("Pikachu", "Detective", "Pika");
        createdCourier = courierApi.createCourier(courier);
        createdCourier.then()
                .assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка статуса 400 для /api/v1/courier")
    @Description("Запрос без пароля")
    public void checkMissingPasswordCreationStatusCode400() {
        courier = new Courier("Pikachu", null, "Pika");
        createdCourier = courierApi.createCourier(courier);
        createdCourier.then().statusCode(400);
    }

    @Test
    @DisplayName("Проверка статуса 400 для /api/v1/courier")
    @Description("Запрос без логина")
    public void checkMissingLoginCreationStatusCode400() {
        courier = new Courier(null, "Detective", "Pika");
        createdCourier = courierApi.createCourier(courier);
        createdCourier.then().statusCode(400);
    }

    @Test
    @DisplayName("Проверка статуса 409 для /api/v1/courier")
    @Description("Попытка создания курьера, который уже существует")
    public void checkSameLoginCourierCreationStatusCode409() {
        courier = new Courier("Pikachu", "Detective", "Pika");
        createdCourier = courierApi.createCourier(courier);
        createdCourier = courierApi.createCourier(courier);
        createdCourier.then().statusCode(409);
    }

    @After
    public void tearDown() {
        Response loginCourierResponse = courierApi.loginCourier(courier);
        int statusCode = loginCourierResponse.then().extract().statusCode();
        if (statusCode == 200) {
            String id = courierApi.extractId(loginCourierResponse);
            courierApi.deleteCourier(id);
        }
    }
}
