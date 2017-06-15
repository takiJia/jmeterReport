 这是一个基于ant+jmeter的报告样式修改已经发送邮件的功能
 使用方式:
 
        1.下载源代码,打成一个jar包扔到ant里面详细参考
        配置 http://www.cnblogs.com/puresoul/p/4808416.html 
        
        2.将这个工程依赖的几个jar也复制进去
        jcommon-1.0.16.jar、jfreechart-1.0.13.jar、servlet-api-2.5.jar、jstl-1.2.jar、ant-javamail.jar
        
        3.需要修改jtl文件的格式为xml，在build里面可以修改,还有需要输出的报告列名,我注释了很多,
        因为全部打开可能导致无法生成报告,如果需要的可以在jmeter的propertity里面修改
        
        4.运行用reportTask 这个target这个是自己写的。