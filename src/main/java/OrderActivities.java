import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderActivities {
    @Step
    public ValidatableResponse orderCreate(String aToken, String bun, String sauce, String main) {
        Gson gson = new Gson();
        String[] ingredient = {bun, sauce, main};
        Foods foods = new Foods(ingredient);
        String json = gson.toJson(foods);
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
    public ValidatableResponse orderWithoutIngredientsCreate(String aToken) {
        String json = "{\"ingredients\": []}";
        ValidatableResponse responseOrderWithoutIngredientsCreate =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(aToken)
                        .body(json)
                        .when()
                        .post("/api/orders")
                        .then();
        return responseOrderWithoutIngredientsCreate;
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
