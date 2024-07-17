package app;

import params.CreateCourier;
import params.LoginCourier;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class BaseTest {
    public void baseTestURL() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    public void createTestCourier(String login, String password, String firstName) {
        CreateCourier courier = new CreateCourier(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/api/v1/courier");
    }

    public void deleteTestCourier(String login, String password) {
        LoginCourier loginCourier = new LoginCourier(login, password);
        int id = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourier)
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .body()
                .path("id");
        given()
                .delete("/api/v1/courier/{id}", id)
                .then().assertThat().statusCode(200);

    }
}

