package ly.snmp.ui;

import ly.snmp.core.model.DataSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

import java.awt.Dimension;
import java.awt.Font;

/**
 * @author gluo
 */
public class Chart extends javax.swing.JPanel {

    private int width, high;
    private String title;

    /**
     * Creates new form Chart
     */
    public Chart(String title, int width, int high, DataSet<Double> dataSet) {
        this.width = width;
        this.high = high;
        initComponents(title, dataSet);
    }

    public Chart(String title, int width, int high) {
        this.width = width;
        this.high = high;
        this.title = title;
        //initComponents(title, dataSet);
    }

    public void setData(DataSet<Double> dataSet) {
        initComponents(title, dataSet);
    }

    private void initComponents(String title, DataSet<Double> dataSet) {
        YIntervalSeries series = new YIntervalSeries(dataSet.getName());
        for (Long time : dataSet.getTimes()) {
            series.add((double) time, dataSet.getData(time), 0d, 0d);
        }
        YIntervalSeriesCollection collection = new YIntervalSeriesCollection();
        collection.addSeries(series);
        chart = ChartFactory.createTimeSeriesChart(title, "", dataSet.getName(), collection);
        chart.getTitle().setFont(new Font("TimesRoman", Font.ITALIC, 10));
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(width, high));
        this.add(chartPanel);
    }


    private ChartPanel chartPanel;
    private JFreeChart chart;
}
