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
public class PositiveTest extends BaseTest {
    private String login;
    private String password;
    private String firstName;
    private String hand = "/api/v1/courier";

    public PositiveTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {"summer1", "1234", "Hans"},
                {"summer2", "1234", "Senya"}
        };
    }

    @Before
    public void setUp() {
        baseTestURL();
    }

    @Step("Регистрация")
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

    @Step("Проверка сообщения ok: true")
    public void checkMessage(Response response) {
        response
                .then().assertThat().body("ok", equalTo(true));
    }

    @Step("Проверка кода ответа Успешное создание учетной записи")
    public void checkStatusCode(Response response) {
        response
                .then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("Создание курьера")
    public void createPositiveTest() {
        Response response = signIn();
        checkStatusCode(response);
        checkMessage(response);
    }

    @After
    public void deleteCourier() {
        deleteTestCourier(login, password);
    }
}

