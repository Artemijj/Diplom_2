import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetUserOrdersTest {
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
        RestAssured.baseURI = new Url().bUrl;
    }

    @Test
    @DisplayName("Get order authorized user test")
    @Description("Получение заказов авторизованным пользователем")
    public void getOrderAuthorizedUserTest() {
        aToken = userActivities.create(email, password, name)
                .extract()
                .path("accessToken");
        orderActivities.orderCreate(aToken.substring(7), bun, sauce, main);
        orderActivities.getOrderUser(aToken.substring(7))
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Get order not authorized user test")
    @Description("Получение заказов неавторизованным пользователем")
    public void getOrderNotAuthorizedUserTest() {
        aToken = userActivities.create(email, password, name)
                .extract()
                .path("accessToken");
        orderActivities.orderCreate(aToken.substring(7), bun, sauce, main);
        aToken = "";
        orderActivities.getOrderUser(aToken)
                .assertThat()
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
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
