package femo.visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.ArrayList;

public class PlotGraph extends ApplicationFrame {

    public PlotGraph(String title, ArrayList<Double> xValues, ArrayList<Double> yValues) {
        super(title);

        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series1 = new XYSeries("Series 1");
        for(int i=0; i<xValues.size(); i++){
            series1.add(xValues.get(i), yValues.get(i));
        }
        dataset.addSeries(series1);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Neural Net Performance", // title
                "X", "Y", // axis labels
                dataset, // dataset
                PlotOrientation.VERTICAL,
                true, // legend? yes
                true, // tooltips? yes
                false // URLs? no
        );

        XYPlot plot = chart.getXYPlot();
        StandardXYItemRenderer regressionRenderer = new StandardXYItemRenderer();
        regressionRenderer.setBaseSeriesVisibleInLegend(false);
        plot.setDataset(1, regress(dataset));
        plot.setRenderer(1, regressionRenderer);
        DrawingSupplier ds = plot.getDrawingSupplier();
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            Paint paint = ds.getNextPaint();
            //scatterRenderer.setSeriesPaint(i, paint);
            regressionRenderer.setSeriesPaint(i, paint);
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);

    }

    private static XYDataset regress(XYSeriesCollection data) {
        // Determine bounds
        double xMin = Double.MAX_VALUE, xMax = 0;
        for (int i = 0; i < data.getSeriesCount(); i++) {
            XYSeries ser = data.getSeries(i);
            for (int j = 0; j < ser.getItemCount(); j++) {
                double x = ser.getX(j).doubleValue();
                if (x < xMin) {
                    xMin = x;
                }
                if (x > xMax) {
                    xMax = x;
                }
            }
        }
        // Create 2-point series for each of the original series
        XYSeriesCollection coll = new XYSeriesCollection();
        for (int i = 0; i < data.getSeriesCount(); i++) {
            XYSeries ser = data.getSeries(i);
            int n = ser.getItemCount();
            double sx = 0, sy = 0, sxx = 0, sxy = 0, syy = 0;
            for (int j = 0; j < n; j++) {
                double x = ser.getX(j).doubleValue();
                double y = ser.getY(j).doubleValue();
                sx += x;
                sy += y;
                sxx += x * x;
                sxy += x * y;
                syy += y * y;
            }
            double b = (n * sxy - sx * sy) / (n * sxx - sx * sx);
            double a = sy / n - b * sx / n;
            XYSeries regr = new XYSeries(ser.getKey());
            regr.add(xMin, a + b * xMin);
            regr.add(xMax, a + b * xMax);
            coll.addSeries(regr);
        }
        return coll;
    }

    public static void main(String[] args) {
        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Double> y = new ArrayList<Double>();
        x.add(1d);
        x.add(2d);
        y.add(1d);
        y.add(2d);
        PlotGraph demo = new PlotGraph("Scatter Plot Demo", x, y);
        demo.pack();

        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}