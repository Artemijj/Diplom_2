import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderActivities {
    @Step
    public ValidatableResponse orderCreate(String aToken, String bun, String sauce, String main) {
        String json = "{\"ingredients\": [\"" + bun + "\", \"" + sauce + "\", \"" + main + "\"]}";
        ValidatableResponse responseOrderCreate =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(aToken)
                        .body(json)
                        .when()
                        .post("/api/orders")
                        .then();
        return responseOrderCreate;
    }

    @Step
    public ValidatableResponse getOrderUser(String aToken) {
        ValidatableResponse responseGetOrderUser =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(aToken)
                        .when()
                        .get("/api/orders")
                        .then();
        return responseGetOrderUser;
    }
}
