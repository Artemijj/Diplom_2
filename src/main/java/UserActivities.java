import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserActivities {
    @Step
    public ValidatableResponse create(String email, String password, String name) {
        Gson gson = new Gson();
        User user = new User(email, password, name);
        String json = gson.toJson(user);
        ValidatableResponse responseCreate =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/auth/register")
                        .then();
        return responseCreate;
    }

    @Step
    public ValidatableResponse login(String email, String password) {
        String json = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        ValidatableResponse responseLogin =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .post("/api/auth/login")
                        .then();
        return responseLogin;
    }

    @Step
    public ValidatableResponse getData(String aToken) {
        ValidatableResponse responseGetData =
                given()
                .header("Content-type", "application/json")
                .auth().oauth2(aToken)
                .get("/api/auth/user")
                .then();
        return responseGetData;
    }

    @Step
    public ValidatableResponse changeData(String aToken, String email, String password, String name) {
        Gson gson = new Gson();
        User user = new User(email, password, name);
        String json = gson.toJson(user);
        ValidatableResponse responseChangeData =
                given()
                .header("Content-type", "application/json")
                .auth().oauth2(aToken)
                        .body(json)
                        .patch("/api/auth/user")
                        .then();
        return responseChangeData;
    }

    @Step
    public ValidatableResponse delete(String aToken) {
        ValidatableResponse responseDelete =
                given()
                .header("Content-type", "application/json")
                .auth().oauth2(aToken)
                        .delete("/api/auth/user")
                        .then();
        return responseDelete;
    }

    @Step
    public ValidatableResponse logout(String rToken) {
        String json = "{\"token\": \"" + rToken + "\"}";
        ValidatableResponse responseLogout =
                given()
                .header("Content-type", "application/json")
//                .auth().oauth2(rToken)
                        .body(json)
                        .post("/api/auth/logout")
                        .then();
        return responseLogout;
    }

    @Step
    public ValidatableResponse tokensRenew(String rToken) {
        String json = "{\"token\": \"" + rToken + "\"}";
        ValidatableResponse responseTokensRenew =
                given()
                        .header("Content-type", "application/json")
//                .auth().oauth2(rToken)
                        .body(json)
                        .post("/api/auth/token")
                        .then();
        return responseTokensRenew;
    }
}
