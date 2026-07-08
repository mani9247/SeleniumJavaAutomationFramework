package Retry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY = 2;

    private static final Logger logger =
            LogManager.getLogger(RetryAnalyzer.class);

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {

            retryCount++;
            logger.info("Retrying test: {} | Retry Count: {}",
                    result.getName(), retryCount);
            return true;
        }
        return false;
    }
}


