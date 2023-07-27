package tests.ui;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.utils.BaseTest;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@Test(groups = "ui")
public class LoginUITest extends BaseTest {
    private final SelenideElement usernameInput = $(By.name("username"));
    private final SelenideElement passwordInput = $(By.name("password"));
    private final SelenideElement loginButton = $(By.xpath("//button[contains(text(), 'Sign in')]"));


    @BeforeClass
    public void setUpBeforeTest() {
        setUp();
    }


    @Test(priority = 1)
    public void invalidAllLoginTest(){
        Selenide.open("/login");
        usernameInput.setValue("adminFalse");
        passwordInput.setValue("falsePassword");
        loginButton.click();

        $("body > div > p").shouldHave(text("Bad username or password"));
    }

    @Test(priority = 2)
    public void invalidPasswordLoginTest(){
        Selenide.open("/login");
        usernameInput.setValue("admin");
        passwordInput.setValue("111111");
        loginButton.click();

        $("body > div > p").shouldHave(text("Bad username or password"));
    }

    @Test(priority = 3)
    public void logInTest(){
        logInTestPositive();
    }

    @AfterClass
    public void closeBrowser(){
        tearDown();
    }
}
