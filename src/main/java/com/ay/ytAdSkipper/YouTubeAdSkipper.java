package com.ay.ytAdSkipper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

public class YouTubeAdSkipper {
    private static final Logger logger = LoggerFactory.getLogger(YouTubeAdSkipper.class);

    public static void main(String[] args) throws InterruptedException {
        // Set the path to the WebDriver (e.g., ChromeDriver)
        System.setProperty("webdriver.chrome.driver", "/chromedriver-win64/chromedriver.exe");  // Change this path
     // Initialize WebDriver
        WebDriver driver = new ChromeDriver();
        logger.info("Chrome browser launched.");

        // Open YouTube and play the initial video (can be any starting point)
        driver.get("https://www.youtube.com");
        logger.info("Navigated to YouTube homepage.");

        // Store the initial URL
        String currentUrl = driver.getCurrentUrl();

        // Initialize WebDriverWait with a timeout (e.g., 10 seconds)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Infinite loop to keep checking for video URL changes
        while (true) {
        	try {
                // Detect and click "Skip Ad" button if present
                List<WebElement> skipButtons = driver.findElements(By.className("ytp-ad-skip-button"));
                if (!skipButtons.isEmpty()) {
                    skipButtons.get(0).click();
                    logger.info("Ad skipped.");
                } else {
                    logger.info("No ads to skip.");
                }
            } catch (Exception e) {
                logger.error("Error while skipping ad: ", e);
            }

            // Detect if the video is sponsored (based on title or description)
            try {
                // Assuming the sponsored content can be identified by keywords in the title
                WebElement videoTitle = driver.findElement(By.cssSelector("h1.title.style-scope.ytd-video-primary-info-renderer"));
                String titleText = videoTitle.getText();
                
                if (titleText.toLowerCase().contains("sponsored") || titleText.toLowerCase().contains("ad")) {
                    logger.info("Detected sponsored content, skipping video.");
                    driver.navigate().back(); // Navigate back to previous page or next video
                    break;  // Exit after skipping sponsored content
                }
            } catch (Exception e) {
                logger.error("Error while checking sponsored content: ", e);
            }

            // Wait a bit before checking again
            Thread.sleep(5000);
            // Get the current URL dynamically
            String newUrl = driver.getCurrentUrl();

            // Check if the URL has changed (i.e., new video is clicked)
            if (!newUrl.equals(currentUrl)) {
                currentUrl = newUrl; // Update current URL
                logger.info("New video detected: " + currentUrl);

                // Run the ad-block and sponsored content detection logic for the new video
               // skipAdsAndDetectSponsoredContent(driver, wait);
            }

            // Wait before checking again
            Thread.sleep(5000);
        }
    }

    /**
     * Function to skip ads and detect sponsored content for the current video
     */
    private static void skipAdsAndDetectSponsoredContent(WebDriver driver, WebDriverWait wait) {
        try {
            // Detect and click "Skip Ad" button if present
            List<WebElement> skipButtons = driver.findElements(By.className("ytp-ad-skip-button"));
            if (!skipButtons.isEmpty()) {
                skipButtons.get(0).click();
                logger.info("Ad skipped.");
            } else {
                logger.info("No ads to skip.");
            }
        } catch (Exception e) {
            logger.error("Error while skipping ad: ", e);
        }

        // Detect if the video is sponsored (based on title or description)
        try {
            // Assuming the sponsored content can be identified by keywords in the title
            WebElement videoTitle = driver.findElement(By.cssSelector("h1.title.style-scope.ytd-video-primary-info-renderer"));
            String titleText = videoTitle.getText();
            
            if (titleText.toLowerCase().contains("sponsored") || titleText.toLowerCase().contains("ad")) {
                logger.info("Detected sponsored content, skipping video.");
                driver.navigate().back(); // Navigate back to previous page or next video
            }
        } catch (Exception e) {
            logger.error("Error while checking sponsored content: ", e);
        }
    }
}