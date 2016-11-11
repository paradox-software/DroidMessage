/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.desktop.startup;

import net.thenightwolf.dm.desktop.MainApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class LogHeaderCommand {

    private static final Logger LOG = LoggerFactory.getLogger(LogHeaderCommand.class);

    public void execute() {

        LOG.info("======[ DroidMessage Information ]======");
        printSoftware();

        LOG.info("======[ Java Information ]======");
        printJava();

        LOG.info("======[ Hardware Information ]======");
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();
        LOG.info(os.toString());
        printProcessor(hal.getProcessor());
    }

    private void printSoftware(){
        LOG.info("Version: " + MainApp.VERSION);
    }

    private void printJava(){
        LOG.info("JRE version: {}", System.getProperty("java.version"));
        LOG.info("JRE vendor: {}", System.getProperty("java.vendor"));
    }

    private void printProcessor(CentralProcessor processor) {
        LOG.info(processor.toString());
        LOG.info(" " + processor.getPhysicalProcessorCount() + " physical CPU(s)");
        LOG.info(" " + processor.getLogicalProcessorCount() + " logical CPU(s)");

        LOG.info("Identifier: " + processor.getIdentifier());
        LOG.info("Serial Num: " + processor.getSystemSerialNumber());
    }


}

