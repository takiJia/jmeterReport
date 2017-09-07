package org.programmerplanet.ant.taskdefs.jmeter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: jiaou
 * Date: 16-11-7
 * Time: 下午12:52
 * To change this template use File | Settings | File Templates.
 */
public class ReportTask extends Task {
    private String in;
    private String out;
    private String mailhost;
    private String mailport;
    private boolean ssl;
    private String user;
    private String password;
    private String mailTitle;
    private String from;
    private String toAddress;
    private String runUser;

    /**
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute() throws BuildException {
        System.out.println("开始执行收集报告任务");
        if (null == in || in.equals("")) {
            System.out.println("in,Jtl文件不能为空!");
            return;
        }
        if (null == out || out.equals("")) {
            System.out.println("out,输出目录不能为空!");
            return;
        }
        String mailContent = ReportHtml.write(in, out,runUser);
        File directory = new File(out);
        String path = "";
        try {
            path = directory.getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("报告和图片生成完成，开始发送邮件");
        //smtp.qq.com
        MailObj mailObj = new MailObj();
        mailObj.setHost(mailhost);
        mailObj.setPort(mailport);
        mailObj.setMailTitle(mailTitle);
        mailObj.setName(user);
        mailObj.setPassword(password);
        mailObj.setFrom(from);
        mailObj.setToAddress(toAddress);
        mailObj.setSsl(ssl);
        mailObj.setMailContent(mailContent);
        mailObj.setReportPath(path);
        mailObj.setHtmlPath(out);
        try {
            SendEmail.sendEmail(mailObj);
        } catch (Exception e) {
            System.out.println("发送邮件失败:"+e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void setIn(String in) {
        this.in = in;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public void setMailhost(String mailhost) {
        this.mailhost = mailhost;
    }

    public void setMailport(String mailport) {
        this.mailport = mailport;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getIn() {
        return in;
    }

    public String getOut() {
        return out;
    }

    public String getMailhost() {
        return mailhost;
    }

    public String getMailport() {
        return mailport;
    }

    public boolean isSsl() {
        return ssl;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public String getFrom() {
        return from;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getRunUser() {
        return runUser;
    }

    public void setRunUser(String runUser) {
        this.runUser = runUser;
    }
}
