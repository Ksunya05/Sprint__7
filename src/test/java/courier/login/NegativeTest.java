package courier.login;

import app.BaseTest;
import params.LoginCourier;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class NegativeTest extends BaseTest {
    private String login;
    private String password;
    private int code;
    private String message;
    private String hand = "/api/v1/courier/login";

    public NegativeTest(String login, String password, int code, String message) {
        this.login = login;
        this.password = password;
        this.code = code;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {"", "4567", 400, "Недостаточно данных для входа"},
                {"magic", "", 400, "Недостаточно данных для входа"},
                {"", "", 400, "Недостаточно данных для входа"},
                {"magical", "4567", 404, "Учетная запись не найдена"},
                {"magic", "6789", 404, "Учетная запись не найдена"}
        };
    }

    @Before
    public void setUp() {
        baseTestURL();
        createTestCourier("magic", "4567", "one");
    }

    @Step("Отправка логина с некорректными данными")
    public Response login() {
        LoginCourier courier = new LoginCourier(login, password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .post(hand);
        return response;
    }

    @Step("Проверка сообщения об ошибке")
    public void checkMessage(Response response) {
        response
                .then().assertThat().body("message", equalTo(message));
    }

    @Step("Проверка кода ошибки")
    public void checkStatusCode(Response response) {
        response
                .then().assertThat().statusCode(code);
    }

    @Test
    @DisplayName("Логин курьера - негативный кейс")
    public void testNegativeLoginCase() {
        Response response = login();
        checkStatusCode(response);
        checkMessage(response);
    }

    @After
    public void deleteCourier() {
        deleteTestCourier("magic", "4567");
    }
}
