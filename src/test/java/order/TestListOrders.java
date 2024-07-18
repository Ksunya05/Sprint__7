package order;

import app.BaseTest;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
public class TestListOrders extends BaseTest {
    public String hand = "/api/v1/orders";

    @Before
    public void setUp() {
        baseTestURL();
    }

    @Step("Запрос на список заказов")
    public Response listOrders() {
        Response response =
                given()
                        .get(hand);
        return response;
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void testListOrders() {
        Response response = listOrders();
        response
                .then().statusCode(SC_OK);
    }
}
