package tests.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.*;
import tests.utils.BaseTest;


@Test(groups = "api")
public class UserApiTests extends BaseTest {
    public static final String API_URL = "http://64.227.74.146/jsonrpc.php";
    private String authHeader;

    public static final String userName = "bobak";
    public static final String password = "123456";
    private int userId;


    @BeforeClass
    public void setUpBeforeTest(){
        setUp();
        logInTestPositive();
        authHeader = getAuthHeader();
    }

    @Test(priority = 1)
    public void createUserTest(){
        String createUserRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createUser\",\n" +
                "    \"id\": 1518863034,\n" +
                "    \"params\": {\n" +
                "        \"username\": " + "\"" + userName + "\",\n" +
                "        \"password\": " + "\"" + password + "\",\n" +
                "        \"role\": \"app-manager\"\n" +
                "    }\n" +
                "}";

        Response createUserResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(createUserRequest)
                .post(API_URL);

        createUserResponse.then().statusCode(200);
        createUserResponse.prettyPrint();

        userId = createUserResponse.then().extract().path("result");
    }

    @Test(priority = 2)
    public void deleteUserTest(){
        String deleteUserRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeUser\",\n" +
                "    \"id\": 1518863034,\n" +
                "    \"params\": {\n" +
                "        \"user_id\": " + "\"" + userId + "\"\n" +
                "    }\n" +
                "}";

        Response deleteUserResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(deleteUserRequest)
                .post(API_URL);

        deleteUserResponse.then().statusCode(200);
        deleteUserResponse.prettyPrint();
    }

    @AfterClass
    public void closeBrowser(){
        tearDown();
    }

}
