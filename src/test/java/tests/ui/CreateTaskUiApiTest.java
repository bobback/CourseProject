package tests.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.testng.annotations.*;
import tests.utils.BaseTest;

import java.util.Base64;

import static com.codeborne.selenide.Selenide.$;

@Test(groups = "ui")
public class CreateTaskUiApiTest extends BaseTest {
    private static final String API_ENDPOINT = "http://64.227.74.146/jsonrpc.php";
    private String authHeader;

    private final String userName = "bobak";
    private final String password = "123456";
    private final SelenideElement usernameInput = $(By.name("username"));
    private final SelenideElement passwordInput = $(By.name("password"));
    private final SelenideElement loginButton = $(By.xpath("//button[contains(text(), 'Sign in')]"));

    private int userId;
    private int projectId;


    @BeforeClass
    public void setUpBeforeTest(){
        setUp();
        logInTestPositive();


        Selenide.open("/settings/api");

        String API_TOKEN = $(By.xpath("//*[@id=\"config-section\"]/div[2]/div[2]/ul/li[1]/strong")).getText();
        authHeader = "Basic " + Base64.getEncoder().encodeToString(("jsonrpc:" + API_TOKEN).getBytes());

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
                .post(API_ENDPOINT);

        createUserResponse.then().statusCode(200);
        createUserResponse.prettyPrint();

        userId = createUserResponse.then().extract().path("result");

        String createProjectRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createProject\",\n" +
                "    \"id\": \"1797076613\",\n" +
                "    \"params\": {\n" +
                "        \"name\": \"Bobak Project\"\n" +
                "    }\n" +
                "}";

        Response createProjectResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(createProjectRequest)
                .post(API_ENDPOINT);

        createProjectResponse.then().statusCode(200);
        createProjectResponse.prettyPrint();

        projectId = createProjectResponse.then().extract().path("result");


        String addUserToProjectRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"addProjectUser\",\n" +
                "    \"id\": \"1294688355\",\n" +
                "    \"params\": [\n" +
                "\"" + projectId + "\",\n" +
                "\"" + userId + "\",\n" +
                "        \"project-manager\"\n" +
                "    ]\n" +
                "}";

        Response addUserToProjectResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(addUserToProjectRequest)
                .post(API_ENDPOINT);

        addUserToProjectResponse.then().statusCode(200);
        addUserToProjectResponse.prettyPrint();

        tearDown();
    }


    @Test
    public void createTaskTest(){
        setUp();
        Selenide.open("/login");
        usernameInput.setValue(userName);
        passwordInput.setValue(password);
        loginButton.click();


        $(By.xpath("//*[@id=\"dashboard\"]/div[2]/div[2]/div[2]/div[1]/div/a")).click();
        $(By.xpath("//*[@id=\"dropdown\"]/ul/li[2]/a")).click();
        $(By.xpath("/html/body/section/div/div[1]/div/a")).click();
        $(By.xpath("//*[@id=\"dropdown\"]/ul/li[1]")).click();
        $("#form-title").setValue("Bobak's Task");
        $(By.xpath("//*[@id=\"modal-content\"]/form/div/div[2]/small/a")).click();
        $(By.xpath("//*[@id=\"modal-content\"]/form/div/div[4]/div/div/button")).click();
        $(By.xpath("/html/body/div[1]/div[3]/div/form/div/div[1]/div/ul/li[2]/a")).click();

        $(By.xpath("/html/body/div[2]/div/div[1]")).shouldHave(Condition.text("1 task"));

        tearDown();
    }


    @AfterClass
    public void cleanUp(){
        String removeUserRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeUser\",\n" +
                "    \"id\": 1518863034,\n" +
                "    \"params\": {\n" +
                "        \"user_id\": " + "\"" + userId + "\"\n" +
                "    }\n" +
                "}";

        Response removeUserResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(removeUserRequest)
                .post(API_ENDPOINT);

        removeUserResponse.then().statusCode(200);
        removeUserResponse.prettyPrint();

        String removeProjectRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeProject\",\n" +
                "    \"id\": \"46285125\",\n" +
                "    \"params\": {\n" +
                "        \"project_id\": " + "\"" + projectId + "\"\n" +
                "    }\n" +
                "}";

        Response removeProjectResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(removeProjectRequest)
                .post(API_ENDPOINT);

        removeProjectResponse.then().statusCode(200);
        removeProjectResponse.prettyPrint();
    }
}
