/*
 * ChartTest.java
 * Date: 4/13/2015
 * Time: 11:07 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package ly.snmp.ui;

import ly.snmp.core.model.DataSet;

import javax.swing.*;
import java.awt.*;

public class ChartTest {
    public static void main(String... args) {
        DataSet<Double> dataSet = new DataSet<Double>(" ");
        dataSet.appendData(123456l, 1.0);
        dataSet.appendData(1234533l, 2.0);
        Chart chart = new Chart("Test", 300, 150);
        JFrame frame = new JFrame();
        frame.add(chart);
        frame.setSize(500, 270);
        frame.setVisible(true);
        chart.setData(dataSet);
    }
}
