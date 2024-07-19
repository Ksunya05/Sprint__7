package order;

import app.BaseTest;
import params.MakeOrder;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import params.OrderApi;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class TestMakeOrder extends BaseTest {
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

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
        baseTestURL();

    }

    @Step("Отправка параметров для создания заказа")
    public Response makeOrders() {
        MakeOrder order = new MakeOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = OrderApi.makeOrder(order);
        return response;
    }

    @Test
    @DisplayName("Проверка создания заказа")
    public void testMakeOrder() {
        Response response = makeOrders();
        response
                .then().assertThat().statusCode(SC_CREATED).body("track", notNullValue());
    }
}
