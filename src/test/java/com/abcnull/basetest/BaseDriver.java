package com.abcnull.basetest;

import com.abcnull.util.PropertiesReader;
import io.appium.java_client.MobileDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * BaseDriver
 *
 * @author abcnull@qq.com
 * @version 1.0.0
 * @date 2020/8/2 16:53
 */
@Slf4j
public class BaseDriver {
    /**
     * 手机驱动
     */
    private MobileDriver<WebElement> driver;

    /**
     * 手机系统
     */
    private String platformName;

    /**
     * 唯一设备标识符
     */
    private String udid;

    /**
     * app 包名
     */
    private String appPackage;

    /**
     * app Activity
     */
    private String appActivity;

    /**
     * automation name
     */
    private String automationName;

    /**
     * hub ip 地址
     */
    private String remoteIP;

    /**
     * hub 端口号
     */
    private String remotePort;


    /**
     * @param platformName   手机系统：Android/IOS
     * @param udid           设备唯一标识符
     * @param appPackage     app 包名
     * @param appActivity    app Activity
     * @param automationName automation name
     * @param remoteIP       远端 ip
     * @param remotePort     端口
     * @return MobileDriver
     * @throws MalformedURLException URL
     */
    public MobileDriver<WebElement> startApp(String platformName, String udid, String appPackage, String appActivity, String automationName, String remoteIP, String remotePort) throws MalformedURLException {
        /* 驱动基本信息参数 */
        this.platformName = platformName;
        this.udid = udid;
        this.appPackage = appPackage;
        this.appActivity = appActivity;
        this.automationName = automationName;
        this.remoteIP = remoteIP;
        this.remotePort = remotePort;

        /* 初始化驱动的责任链中各个对象 */
        DriverHandler headHandler = new HeadHandler();
        DriverHandler AndroidDriverHandler = new AndroidDriverHandler();
        DriverHandler IOSDriverHandler = new IOSDriverHandler();
        DriverHandler tailHandler = new TailHandler();

        /* 构建一条驱动初始化的完整责任链 */
        headHandler.setNext(AndroidDriverHandler).setNext(IOSDriverHandler).setNext(tailHandler);

        /* 通过责任链启动浏览器 */
        this.driver = headHandler.start(platformName, udid, appPackage, appActivity, automationName, remoteIP, remotePort);

        /* 驱动设置等待时长 */
        long implicitlyWait = Long.parseLong(PropertiesReader.getKey("driver.timeouts.implicitlyWait"));
        // 隐式等待
        driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
        log.info((platformName.equals("Android")) ? ("安卓" + appPackage + "成功启动！") : ("苹果" + appPackage + "成功启动！"));

        return this.driver;
    }

    /**
     * getDriver
     *
     * @return MobileDriver
     */
    public MobileDriver<WebElement> getDriver() {
        return driver;
    }

    /**
     * getPlatformName
     *
     * @return platformName
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * getUdid
     *
     * @return udid
     */
    public String getUdid() {
        return udid;
    }

    /**
     * getAppPackage
     *
     * @return appPackage
     */
    public String getAppPackage() {
        return appPackage;
    }

    /**
     * getAppActivity
     *
     * @return appActivity
     */
    public String getAppActivity() {
        return appActivity;
    }

    /**
     * getAutomationName
     *
     * @return automationName
     */
    public String getAutomationName() {
        return automationName;
    }

    /**
     * getRemoteIP
     *
     * @return remoteIP
     */
    public String getRemoteIP() {
        return remoteIP;
    }

    /**
     * getRemotePort
     *
     * @return remotePort
     */
    public String getRemotePort() {
        return remotePort;
    }

    /**
     * 退出驱动
     */
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        log.info(platformName + "驱动已成功关闭！");
    }
}
