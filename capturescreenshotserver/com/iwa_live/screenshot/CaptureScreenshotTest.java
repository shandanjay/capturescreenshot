package com.iwa_live.screenshot;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CaptureScreenshotTest
{
    private static int      DISPLAY_NUMBER  = 89;
    private static String   XVFB            = "/usr/bin/Xvfb";
    private static String   XVFB_COMMAND    = XVFB + " :" + DISPLAY_NUMBER +" -screen 0 1920x1280x24";
    private static String   URL             = "http://www.google.com/";
    private static String   RESULT_FILENAME = "/tmp/screenshot.png";


	private void imageCrop(File file, int width, int height){
			try {
				BufferedImage image = ImageIO.read(file); 
				BufferedImage dest = image.getSubimage(0, 0, width, height);
				ImageIO.write(dest, "png", file);
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
	}

    public String capture ( String[] args ) throws IOException
    {
		if(args.length>0){
			URL = args[0];
			RESULT_FILENAME = args[1];
		}
		
		
        Process p = Runtime.getRuntime().exec(XVFB_COMMAND);
        FirefoxBinary firefox = new FirefoxBinary();
        firefox.setEnvironmentProperty("DISPLAY", ":" + DISPLAY_NUMBER);
        WebDriver driver = new FirefoxDriver(firefox, null);
        
     //   driver.manage().timeouts().implicitlyWait(10, java.util.concurrent.TimeUnit.SECONDS);
     
       // ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.resizeTo(1920, 1280);");
        
     
     driver.manage().window().maximize();
     driver.get(URL);   
     
        int width = Integer.parseInt(String.valueOf(((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return document.body.clientWidth")));
        int height = Integer.parseInt(String.valueOf(((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return document.body.clientHeight")));
                            
        File scrFile = ( (TakesScreenshot) driver ).getScreenshotAs(OutputType.FILE);
        
        imageCrop(scrFile, width, height);
        System.out.println(width+" "+height);
        FileUtils.copyFile(scrFile, new File(RESULT_FILENAME));
        driver.close();
        p.destroy();
        return RESULT_FILENAME;
    }
}
