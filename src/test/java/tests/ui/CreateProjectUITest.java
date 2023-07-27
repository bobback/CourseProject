package tests.ui;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.utils.BaseTest;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@Test(groups = "ui")
public class CreateProjectUITest extends BaseTest {

    private final SelenideElement createProjectButton =   $(By.xpath("/html/body/section/div[1]/ul/li[1]/a"));
    private final SelenideElement projectNameInput = $(By.id("form-name"));
    private final SelenideElement createButton = $(By.xpath("//*[@id=\"project-creation-form\"]/div[2]/div/button"));


    @BeforeClass
    public void setUpBeforeTest(){
        setUp();
        logInTestPositive();
    }

    @Test
    public void createProjectTest(){
        Selenide.open(API_URL + "/projects");
        createProjectButton.click();

        String newProjectName = "Test Project";
        projectNameInput.setValue(newProjectName);
        createButton.click();

        $(By.xpath("/html/body/header/div[1]/h1/span[2]")).shouldHave(text(newProjectName));
    }

    @AfterClass
    public void cleanUp(){
        $(By.xpath("//*[@id=\"main\"]/section/div[1]/ul/li[18]/a")).click();
        $(By.xpath("//*[@id=\"modal-confirm-button\"]")).click();
        tearDown();
    }
}


