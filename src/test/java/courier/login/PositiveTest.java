package courier.login;

import app.BaseTest;
import params.LoginCourier;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class PositiveTest extends BaseTest {
    private String hand = "/api/v1/courier/login";

    @Before
    public void setUp() {
        baseTestURL();
        createTestCourier("magic", "4567", "one");
    }

    @Step("Авторизация курьера")
    public Response login() {
        LoginCourier courier = new LoginCourier("magic", "4567");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .post(hand);
        return response;
    }

    @Step("Проверка id - успешного логина")
    public void checkId(Response response) {
        response
                .then().assertThat().body("id", notNullValue());
    }

    @Step("Проверка кода ответа - Успешный логин")
    public void checkStatusCode(Response response) {
        response
                .then().assertThat().statusCode(200);

    }

    @Test
    @DisplayName("Успешный логин")
    public void testPositiveLoginCase() {
        Response response = login();
        checkStatusCode(response);
        checkId(response);
    }

    @After
    public void deleteCourier() {
        deleteTestCourier("magic", "4567");
    }
}

