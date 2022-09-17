import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private UserActivities userActivities = new UserActivities();
    String email = "testqaemail@gmail.com";
    String password = "testqapassword";
    String name = "testqaname";
    String aToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("User login test")
    @Description("Авторизация существующего пользователя")
    public void userLoginTest() {
        userActivities.create(email, password, name);
        userActivities.login(email, password)
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(200);
    }

    @Test
    @DisplayName("User login wrong email test")
    @Description("Попытка авторизации с неверным email")
    public void userLoginWrongEmailTest() {
        userActivities.create(email, password, name);
        String email = "wrongtestqaemail@gmail.com";
        userActivities.login(email, password)
                .assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(401);
    }

    @Test
    @DisplayName("User login wrong password test")
    @Description("Попытка авторизации с неверным паролем")
    public void userLoginWrongPasswordTest() {
        userActivities.create(email, password, name);
        String password = "wrongtestqapassword";
        userActivities.login(email, password)
                .assertThat()
                .body("message", equalTo("email or password are incorrect"))
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
