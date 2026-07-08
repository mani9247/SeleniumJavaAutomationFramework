package Utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ElementActions {

        private WebDriver driver;
        private WaitUtils wait;

    private static final Logger logger =
            LogManager.getLogger(ElementActions.class);

        public ElementActions(WebDriver driver){

            this.driver = driver;
            this.wait = new WaitUtils(driver);
        }

        public void type(By locator, String text){

            WebElement element = wait.waitForVisibility(locator);

            element.clear();

            element.sendKeys(text);
            logger.info("Entered text '{}' into {}", text, locator);
        }

        public void click(By locator){

            int attempts = 0;

            while (attempts < 3) {
                try {
                    wait.waitForClickable(locator).click();
                    logger.info("Clicked on {}", locator);
                    return;
                } catch (StaleElementReferenceException e) {
                    attempts++;
                    logger.warn("Stale element detected. Retry {}", attempts);
                }
            }

            throw new RuntimeException("Unable to click: " + locator);
        }

        public String getText(By locator){

            String text = wait.waitForVisibility(locator).getText();

            logger.info("Fetched text '{}' from {}", text, locator);

            return text;

        }

        public boolean isDisplayed(By locator){

            boolean status =
                    wait.waitForVisibility(locator).isDisplayed();

            logger.info("{} Displayed : {}", locator, status);

            return status;
        }
    }
