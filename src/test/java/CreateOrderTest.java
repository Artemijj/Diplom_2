import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    String email = "testqaemail@gmail.com";
    String password = "testqapassword";
    String name = "testqaname";
    String bun = "61c0c5a71d1f82001bdaaa6d";
    String sauce = "61c0c5a71d1f82001bdaaa72";
    String main = "61c0c5a71d1f82001bdaaa6f";
    String aToken;
    UserActivities userActivities = new UserActivities();
    OrderActivities orderActivities = new OrderActivities();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Authorized order create test")
    @Description("Создание заказа авторизованным пользователем")
    public void authorizedOrderCreateTest() {
        aToken = userActivities.create(email, password, name)
                .extract()
                .path("accessToken");
        orderActivities.orderCreate(aToken.substring(7), bun, sauce, main)
                .assertThat()
                .body("order.owner.name", equalTo(name))
                .statusCode(200);
    }

    @Test
    @DisplayName("Not authorized order create test")
    @Description("Создание заказа неавторизованным пользователем")
    public void notAuthorizedOrderCreateTest() {
        userActivities.create(email, password, name);
        aToken = "";
        orderActivities.orderCreate(aToken, bun, sauce, main)
                .assertThat()
                .body(not(hasKey("order.owner.name")))
                .statusCode(200);
    }

    @Test
    @DisplayName("Authorized without ingredients order create test")
    @Description("Попытка создания заказа без ингредиентов")
    public void authorizedWithoutIngredientsOrderCreateTest() {
        aToken = userActivities.create(email, password, name)
                .extract()
                .path("accessToken");
        String json = "{\"ingredients\": []}";
        ValidatableResponse responseOrderCreate =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(aToken.substring(7))
                        .body(json)
                        .when()
                        .post("/api/orders")
                        .then()
                        .assertThat()
                        .body("message", equalTo("Ingredient ids must be provided"))
                        .statusCode(400);
    }

    @Test
    @DisplayName("Authorized wrong hash order create test")
    @Description("Попытка создания заказа с неверным хешем ингредиентов")
    public void authorizedWrongHashOrderCreateTest() {
        aToken = userActivities.create(email, password, name)
                .extract()
                .path("accessToken");
        String bun = "123456789";
        String sauce = "987654321";
        String main = "147852369";
        orderActivities.orderCreate(aToken.substring(7), bun, sauce, main)
                .assertThat()
                .statusCode(500);
    }

    @After
    public void thisIsTheEnd() throws NullPointerException {
        try {
            aToken = userActivities.login(email, password)
                    .extract()
                    .path("accessToken");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (aToken != null) {
            userActivities.delete(aToken.substring(7));
        }
    }
}
