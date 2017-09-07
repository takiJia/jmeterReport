package org.programmerplanet.ant.taskdefs.jmeter;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;

/**
 * Created with IntelliJ IDEA.
 * User: jiaou
 * Date: 14-5-5
 * Time: 下午11:05
 * To change this template use File | Settings | File Templates.
 */
public class PicReport extends ServletUtilities {
    private static String fileName;

    private String titleName;

    public String getTitleName() {
        return this.titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getFileName() {
        return PicReport.fileName;
    }

    public void setFileName(String fileName) {
        PicReport.fileName = fileName;
    }

    protected static void createTempDir() {
        String path = System.getProperty("user.dir") + "\\pic";
        String file = new File("d:").getAbsolutePath() + "/health";
        ///判断目录是否存在，不存在创建一个
        File getFile = new File(path);
        if (getFile.exists() == false) {
            getFile.mkdirs();
        }
        System.out.println("createTempDir:" + file);
    }

    public static String saveChartAsPNG(JFreeChart chart, int width, int height) throws IOException {
        String path = System.getProperty("user.dir") + "\\pic";
        ///判断目录是否存在，不存在创建一个
        File getFile = new File(path);
        if (getFile.exists() == false) {
            getFile.mkdirs();
        }
        String tempDirName = getFile.getAbsolutePath(); //此路径可以在属性文件中配置
        System.out.println("tempDirName:" + tempDirName);
        if (chart == null) {
            throw new IllegalArgumentException("Null 'chart' argument.");
        }
//        createTempDir();
        String prefix = ServletUtilities.getTempFilePrefix();
        prefix = PicReport.fileName;
        File tempFile = File.createTempFile(prefix, ".png", new File(tempDirName));
        ChartUtilities.saveChartAsJPEG(tempFile, chart, width, height);
        return path + "\\" + tempFile.getName();
    }


    public void getCaky() {

        DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
        defaultpiedataset.setValue("CRM", 10.02D);
        defaultpiedataset.setValue("市场人员", 20.23D);
        defaultpiedataset.setValue("开发人员", 60.02D);
        defaultpiedataset.setValue("OEM人员", 10.02D);
        defaultpiedataset.setValue("其他人员", 5.11D);
        JFreeChart chart = ChartFactory.createPieChart3D("能量来源分析 ", defaultpiedataset,
                true, false, false);
        chart.setTitle(new TextTitle("能量来源分析 饼型图"));// 标题字体
        // 设置图例部分
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(new Font("华文楷体", Font.BOLD, 15));// 设置图例的字体
        // 设置图的部分
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("华文楷体", Font.BOLD, 13));// 设置实际统计图的字体
        plot.setBackgroundAlpha(0.4f);
    }


    public String pic(double[][] data, String[] rowKeys, String name) {
//    public String pic() {
//        double[][] data = new double[][]{{1320}, {720}, {830}, {400},{100},{100},{100}};



        String[] columnKeys = {""};
        CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);

        JFreeChart chart = ChartFactory.createBarChart3D("", "", name, dataset, PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot plot = chart.getCategoryPlot();

//设置网格背景颜色

        plot.setBackgroundPaint(Color.white);

//设置网格竖线颜色

        plot.setDomainGridlinePaint(Color.pink);

//设置网格横线颜色

        plot.setRangeGridlinePaint(Color.pink);

//显示每个柱的数值，并修改该数值的字体属性

        BarRenderer3D renderer = new BarRenderer3D();

        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        renderer.setBaseItemLabelsVisible(true);

//默认的数字显示在柱子中，通过如下两句可调整数字的显示

//注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题

        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));

        renderer.setItemLabelAnchorOffset(10D);

        renderer.setItemLabelFont(new Font("宋体", Font.PLAIN, 12));

        renderer.setItemLabelsVisible(true);

//设置每个地区所包含的平行柱的之间距离

//renderer.setItemMargin(0.3);

        plot.setRenderer(renderer);

//设置地区、销量的显示位置

//将下方的“肉类”放到上方

//        plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);

//将默认放在左边的“销量”放到右方

//        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        Font font = new Font("宋体", Font.BOLD, 25);


        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();

        CategoryAxis domainAxis = plot.getDomainAxis();
        TextTitle title = new TextTitle(this.getFileName(), font);

//        TextTitle subtitle = new TextTitle("副标题", new Font("黑体", Font.BOLD, 12));
//
//
//        chart.addSubtitle(subtitle);

        chart.setTitle(title); //标题

//        2.1、X轴坐标上的文字：

        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 13));

//        2.2、X轴坐标标题（用例数量）

        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 17));

        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 17));

//        3.2、Y轴坐标标题（销量）：

        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 17));

//        4、  图表底部乱码（项目等文字）

        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 20));
        String fileName1 = "";
        try {
            fileName1 = PicReport.saveChartAsPNG(chart, 1200, 550);   //宽 高
        } catch (IOException e) {
            e.printStackTrace();
        }

//        fileName = fileName.substring(fileName.lastIndexOf("j"));
        return fileName1;
    }

    /**
     * 饼状图
     *
     * @param dataset
     * @param fileName1
     * @param titleName
     */
    public static void pieChart3D(DefaultPieDataset dataset, String fileName1,
            String titleName) {
//        DefaultPieDataset dataset = new DefaultPieDataset();
//        dataset.setValue(" 市场前期", new Double(10));
        JFreeChart chart = ChartFactory.createPieChart3D(titleName, dataset,
                true, true, false);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setSectionPaint("FailCase", Color.RED);
        plot.setSectionPaint("PassCase", Color.green);

        // 图片中显示百分比:默认方式
        // plot.setLabelGenerator(new
        // StandardPieSectionLabelGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));
        // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}({2})", NumberFormat.getNumberInstance(),
                new DecimalFormat("0.00%")));
        // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}={1}({2})"));
        // 设置背景色为白色
        chart.setBackgroundPaint(Color.white);
        // 指定图片的透明度(0.0-1.0)
        plot.setForegroundAlpha(1.0f);
        // 指定显示的饼图上圆形(false)还椭圆形(true)
        plot.setCircular(true);
        // 设置图标题的字体
        Font font = new Font("SimSun", Font.CENTER_BASELINE, 20);
        TextTitle title = new TextTitle(titleName);
        title.setFont(font);
        chart.setTitle(title);
        plot.setLabelFont(new Font("SimSun", 0, 15));//
        LegendTitle legend = chart.getLegend(0);
        legend.setItemFont(new Font("SimSun", Font.BOLD, 16));
//        PicReport barChart = new PicReport();
//        try {
//            fileName = barChart.saveChartAsPNG(chart, 1200, 550);   //宽 高
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            ChartUtilities.saveChartAsJPEG(
                new File(fileName1), //输出到哪个输出流
                    1, //JPEG图片的质量，0~1之间
                    chart, //统计图标对象
                    640, //宽
                    300,//宽
                    null //ChartRenderingInfo 信息
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void save(DefaultPieDataset dataset, String fileName1,
            String titleName1) {
        PicReport.pieChart3D(dataset, fileName1, titleName1);
    }
}
