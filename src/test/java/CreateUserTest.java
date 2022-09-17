import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
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
    @DisplayName("User create test")
    @Description("Создание пользователя")
    public void userCreateTest() {
        userActivities.create(email, password, name)
                .assertThat()
                .body("success", equalTo(true))
                .statusCode(200);
    }

    @Test
    @DisplayName("User create double test")
    @Description("Попытка создания, который уже зарегистрирован")
    public void userCreateDoubleTest() {
        userActivities.create(email, password, name);
        userActivities.create(email, password, name)
                .assertThat()
                .body("message", equalTo("User already exists"))
                .statusCode(403);
    }

    @Test
    @DisplayName("User create missed email test")
    @Description("Попытка создания пользователя без email")
    public void userCreateMissedEmailTest() {
        String email = "";
        userActivities.create(email, password, name)
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(403);
    }

    @Test
    @DisplayName("User create missed password test")
    @Description("Попытка создания пользователя без пароля")
    public void userCreateMissedPasswordTest() {
        String password = "";
        userActivities.create(email, password, name)
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(403);
    }

    @Test
    @DisplayName("User create missed name test")
    @Description("Попытка создания пользователя без имени")
    public void userCreateMissedNameTest() {
        String name = "";
        userActivities.create(email, password, name)
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(403);
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
