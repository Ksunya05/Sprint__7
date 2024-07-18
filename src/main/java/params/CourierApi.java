package params;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {
    private static final String hand_courier = "/api/v1/courier";
    private static final String hand_login = "/api/v1/courier/login";

    public static Response createCourier(CreateCourier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post(hand_courier);
    }

    public static Response loginCourier(LoginCourier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post(hand_login);
    }

}

