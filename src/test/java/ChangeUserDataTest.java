import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangeUserDataTest {
    private UserActivities userActivities = new UserActivities();
    String email;
    String password;
    String name;
    String aToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = new Url().bUrl;
        email = "testqaemail@gmail.com";
        password = "testqapassword";
        name = "testqaname";
    }

    @Test
    @DisplayName("User authorized data change email test")
    @Description("Изменение email авторизованного пользователя")
    public void userAuthorizedDataChangeEmailTest() {
        aToken = userActivities.create(email, password, name)
                .extract()
                .path("accessToken");
        email = "newtestqaemail@gmail.com";
        userActivities.changeData(aToken.substring(7), email, password, name)
                .assertThat()
                .body("user.email", equalTo(email))
                .statusCode(200);
    }

    @Test
    @DisplayName("User authorized data change password test")
    @Description("Изменение пароля авторизованного пользователя")
    public void userAuthorizedDataChangePasswordTest() {
        aToken = userActivities.create(email, password, name)
                .extract()
                .path("accessToken");
        password = "newtestqapassword";
        userActivities.changeData(aToken.substring(7), email, password, name)
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(200);
    }

    @Test
    @DisplayName("User authorized data change name test")
    @Description("Изменение имени авторизованного пользователя")
    public void userAuthorizedDataChangeNameTest() {
        aToken = userActivities.create(email, password, name)
                .extract()
                .path("accessToken");
        name = "newtestqaname";
        userActivities.changeData(aToken.substring(7), email, password, name)
                .assertThat()
                .body("user.name", equalTo(name))
                .statusCode(200);
    }

    @Test
    @DisplayName("User not authorized data change email test")
    @Description("Изменение email неавторизованного пользователя")
    public void userNotAuthorizedDataChangeEmailTest() {
        userActivities.create(email, password, name);
        aToken = "";
        email = "newtestqaemail@gmail.com";
        userActivities.changeData(aToken, email, password, name)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
    }

    @Test
    @DisplayName("User not authorized data change password test")
    @Description("Изменение пароля неавторизованного пользователя")
    public void userNotAuthorizedDataChangePasswordTest() {
        userActivities.create(email, password, name);
        aToken = "";
        password = "newtestqapassword";
        userActivities.changeData(aToken, email, password, name)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
        password = "testqapassword";
    }

    @Test
    @DisplayName("User not authorized data change name test")
    @Description("Изменение имени неавторизованного пользователя")
    public void userNotAuthorizedDataChangeNameTest() {
        userActivities.create(email, password, name);
        aToken = "";
        name = "newtestqaname";
        userActivities.changeData(aToken, email, password, name)
                .assertThat()
                .body("success", equalTo(false))
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
