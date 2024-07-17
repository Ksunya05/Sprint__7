package courier.creation;

import app.BaseTest;
import params.CreateCourier;
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
    private String firstName;
    private int code;
    private String message;
    private String hand = "/api/v1/courier";

    public NegativeTest(String login, String password, String firstName, int code, String message) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.code = code;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {"cinderella", "1234", "Zoya", 409, "Этот логин уже используется. Попробуйте другой."},
                {"cinderella", "", "Zoya", 400, "Недостаточно данных для создания учетной записи"},
                {"", "1234", "Zoya", 400, "Недостаточно данных для создания учетной записи"},
                {"", "", "Zoya", 400, "Недостаточно данных для создания учетной записи"}
        };
    }

    @Before
    public void setUp() {
        baseTestURL();
        createTestCourier("cinderella", "1234", "Zoya");
    }

    @Step("Регистрация с неполными/повторяющимися данными")
    public Response signIn() {
        CreateCourier courier = new CreateCourier(login, password, firstName);
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
    @DisplayName("Создание курьера - негативный кейс")
    public void createNegativeTest() {
        Response response = signIn();
        checkStatusCode(response);
        checkMessage(response);
    }

    @After
    public void deleteCourier() {

        deleteTestCourier("cinderella", "1234");
    }
}
