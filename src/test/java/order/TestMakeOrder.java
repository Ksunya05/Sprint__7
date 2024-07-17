package order;

import params.MakeOrder;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class TestMakeOrder {
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;
    private String hand = "/api/v1/orders";

    public TestMakeOrder(String lastName, String address, String firstName, int metroStation, String phone, int rentTime, String comment, String deliveryDate, String[] color) {
        this.lastName = lastName;
        this.address = address;
        this.firstName = firstName;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.comment = comment;
        this.deliveryDate = deliveryDate;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getParameters() {
        String[] blackColor = {"BLACK"};
        String[] greyColor = {"GREY"};
        String[] twoColors = {"BLACK", "GREY"};
        String[] withoutColor = {};
        return new Object[][]{
                {"Vasilisa", "Mudraya", "Lugovaya, 5-15", 4, "+79099876543", 5, "2024-04-2024", "No comments", blackColor},
                {"Vasilisa", "Mudraya", "Lugovaya, 5-15", 4, "+79099876543", 5, "2024-04-2024", "No comments", greyColor},
                {"Vasilisa", "Mudraya", "Lugovaya, 5-15", 4, "+79099876543", 5, "2024-04-2024", "No comments", twoColors},
                {"Vasilisa", "Mudraya", "Lugovaya, 5-15", 4, "+79099876543", 5, "2024-04-2024", "No comments", withoutColor},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

    }

    @Step("Отправка параметров для создания заказа")
    public Response makeOrder() {
        MakeOrder makeOrder = new MakeOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response =
                given()
                        .body(makeOrder)
                        .post(hand);
        return response;
    }

    @Step("Проверка track в теле ответа")
    public void checkWordTrack(Response response) {
        response
                .then().assertThat().body("track", notNullValue());
    }

    @Step("Проверка кода ответа - Успешное создание заказа")
    public void checkStatusCode(Response response) {
        response
                .then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("Проверка создания заказа")
    public void testMakeOrder() {
        Response response = makeOrder();
        checkStatusCode(response);
        checkWordTrack(response);

    }
}
