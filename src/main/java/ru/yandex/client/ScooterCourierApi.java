package ru.yandex.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.yandex.model.Courier;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;

public class ScooterCourierApi {
    private RequestSpecification spec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri("https://qa-scooter.praktikum-services.ru")
            .build();
    @Step("Логин курьера")
    public Response loginCourier(Courier courier) {
        return given().spec(spec)
                .body(courier)
                .post("/api/v1/courier/login");
    }
    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given().spec(spec)
                .body(courier)
                .post("/api/v1/courier");
    }
    @Step("Удаление курьера")
    public Response deleteCourier(String id) {
        return given().spec(spec)
                .delete("/api/v1/courier/" + id);
    }
    @Step("Получение ID залогированного курьера")
    public String extractId(Response loginCourier) {
        return Integer.toString(loginCourier.then().extract().path("id"));
    }
}
