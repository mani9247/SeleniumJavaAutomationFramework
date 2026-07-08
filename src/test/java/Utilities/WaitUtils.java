package Utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
public class WaitUtils {

        private WebDriverWait wait;

        public WaitUtils(WebDriver driver){

            ConfigReader config = new ConfigReader();

            wait = new WebDriverWait(driver,
                    Duration.ofSeconds(config.getExplicitWait()));
        }

        public WebElement waitForVisibility(By locator){

           return  wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator));
        }

    // Clickable
    public WebElement waitForClickable(By locator){
        return  wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForClickable(WebElement element){
        return  wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // URL
    public void waitForUrlContains(String partialUrl){
        wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    // Title
    public void waitForTitleContains(String title){
        wait.until(ExpectedConditions.titleContains(title));
    }

    // Alert
    public void waitForAlert(){
        wait.until(ExpectedConditions.alertIsPresent());
    }
}


