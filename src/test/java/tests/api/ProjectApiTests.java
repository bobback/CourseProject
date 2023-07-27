package tests.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.utils.BaseTest;


@Test(groups = "api")
public class ProjectApiTests extends BaseTest {
    public static final String API_URL = "http://64.227.74.146/jsonrpc.php";
    private String authHeader;

    private int projectId;


    @BeforeClass
            public void setUpBeforeTest(){
        setUp();
        logInTestPositive();
        authHeader = getAuthHeader();
    }


    @Test(priority = 1)
    public void createProjectTest(){
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
                .post(API_URL);

        createProjectResponse.then().statusCode(200);
        createProjectResponse.prettyPrint();

        projectId = createProjectResponse.then().extract().path("result");
    }


    @Test(priority = 2)
    public void deleteProjectTest(){
        String deleteProjectRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeProject\",\n" +
                "    \"id\": \"46285125\",\n" +
                "    \"params\": {\n" +
                "        \"project_id\": " + "\"" + projectId + "\"\n" +
                "    }\n" +
                "}";

        Response deleteProjectResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(deleteProjectRequest)
                .post(API_URL);

        deleteProjectResponse.then().statusCode(200);
        deleteProjectResponse.prettyPrint();
    }

    @AfterClass
    public void closeBrowser(){
        tearDown();
    }

}
