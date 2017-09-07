package org.programmerplanet.ant.taskdefs.jmeter;


import com.sun.mail.util.MailSSLSocketFactory;


import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: jiaou
 * Date: 14-8-19
 * Time: 下午2:55
 * To change this template use File | Settings | File Templates.
 */
public class SendEmail {
//    private static Logger logger = Logger.getLogger(SendEmail.class);

    // JavaMail需要Properties来创建一个session对象。它将寻找字符串"mail.smtp.host"，属性值就是发送邮件的主机
    public static void sendEmail(MailObj mailObj) throws Exception {
        Properties properties = new Properties();

        // 开启debug调试
//        properties.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        properties.setProperty("mail.smtp.auth", Boolean.toString(mailObj.isSsl()));
        // 设置邮件服务器主机名
        properties.setProperty("mail.host", mailObj.getHost());
        // 发送服务器端口
        if (mailObj.getPort() != null && !mailObj.getPort().equalsIgnoreCase("")) {
            properties.setProperty("mail.smtp.port", mailObj.getPort());
        }
        // 发送邮件协议名称
        properties.setProperty("mail.transport.protocol", "smtp");

        //开启了 SSL 加密 QQ邮箱要加上
        if (mailObj.getFrom().endsWith("@qq.com")) {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
        }
        final String userName = mailObj.getName();
        final String password = mailObj.getPassword();
        /*
         * 在 JavaMail 中，可以通过 extends Authenticator 抽象类，在子类中覆盖父类中的
         * getPasswordAuthentication() 方法，就可以实现以不同的方式来进行登录邮箱时的用户身份认证。JavaMail
         * 中的这种设计是使用了策略模式（Strategy
         */
        MimeMessage message = new MimeMessage(Session.getInstance(properties,
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(//设置发送帐号密码
                                userName, password);
                    }
                }));
        // 设置邮件的属性

        // 设置邮件的发件人
        message.setFrom(new InternetAddress(mailObj.getFrom()));

        String sendTo = mailObj.getToAddress();
        String sendToCC = mailObj.getToCc();

        // 设置邮件的收件人 cc表示抄送 bcc 表示暗送
        if (null != sendTo) {
            InternetAddress[] senderList = new InternetAddress()
                    .parse(sendTo);
            message.setRecipients(Message.RecipientType.TO, senderList);
        } else {
            return;
        }
        if (null != sendToCC) {
            InternetAddress[] iaCCList = new InternetAddress()
                    .parse(sendToCC);
            message.setRecipients(Message.RecipientType.CC, iaCCList);
        }
        String subject = mailObj.getMailTitle();
        if (subject.equalsIgnoreCase("")) {
            // 设置邮件的主题
            message.setSubject("自动化测试报告");
        } else {
            message.setSubject(subject);
        }

        // 创建邮件的正文
        MimeBodyPart text = new MimeBodyPart();
        // setContent(“邮件的正文内容”,”设置邮件内容的编码方式”)
        text.setContent(mailObj.getMailContent(),
                "text/html;charset=UTF-8");

        // 点到点的发送
        // 一对多发送只要改一个地方如下：


        // 创建图片
        MimeBodyPart img = new MimeBodyPart();
        /*
         * JavaMail API不限制信息只为文本,任何形式的信息都可能作茧自缚MimeMessage的一部分.
         * 除了文本信息,作为文件附件包含在电子邮件信息的一部分是很普遍的. JavaMail
         * API通过使用DataHandler对象,提供一个允许我们包含非文本BodyPart对象的简便方法.
         */

//
//        MimeBodyPart img2 = new MimeBodyPart();
//        DataHandler dh2 = new DataHandler(new FileDataSource(BeanClass.basePath + "responseTime" + BeanClass.buildTestId + ".png"));//第二张图片路径
//        img2.setDataHandler(dh2);
//        img2.setContentID("responseTime");


        // 关系 正文和图片的
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);

        // 创建图片的一个表示用于显示在邮件中显示
        File pngFile = new File(mailObj.getReportPath() +File.separator+ "report.png");
        if (pngFile.exists()) {
            DataHandler dh = new DataHandler(new FileDataSource(pngFile.getAbsolutePath()));//图片路径
            img.setDataHandler(dh);
            img.setContentID("passRate");
            mm.addBodyPart(img);
        }
//        mm.addBodyPart(img2);
        mm.setSubType("related");// 设置正文与图片之间的关系

        // 创建附件
        File file = new File(mailObj.getHtmlPath());
        if (file.exists()) {
            MimeBodyPart attch = new MimeBodyPart();
            DataHandler dh1 = new DataHandler(new FileDataSource(file.getAbsolutePath()));
            attch.setDataHandler(dh1);
            String filename1 = dh1.getName();
            //   MimeUtility 是一个工具类，encodeText（）//用于处理附件字，防止中文乱码问题
            attch.setFileName(MimeUtility.encodeText(filename1));
            mm.addBodyPart(attch);
        }

        // 图班与正文的 body
        MimeBodyPart all = new MimeBodyPart();
        all.setContent(mm);
        // 附件与正文（text 和 img）的关系
        MimeMultipart mm2 = new MimeMultipart();
        mm2.addBodyPart(all);
        mm2.setSubType("mixed");// 设置正文与附件之间的关系

//        mm3.setSubType("related");// 设置正文与图片之间的关系
        message.setContent(mm);
        message.saveChanges(); // 保存修改

        Transport.send(message);// 发送邮件
//        logger.info("邮件发送成功");
        System.out.println("邮件发送成功");
    }


    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sendTo", "165157905@qq.com");
//        map.put("sendToCC", "jiaou@jd.com");
        try {
//            SendEmail.sendEmail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
