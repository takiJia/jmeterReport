package org.programmerplanet.ant.taskdefs.jmeter;


import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: jiaou
 * Date: 16-7-23
 * Time: 下午10:51
 * To change this template use File | Settings | File Templates.
 */
public class MailReport {
    private static NumberFormat numberFormat = NumberFormat.getInstance();
    /**
     * 获取发送邮件的征文
     * @return String
     */
    public static String reportHtml(int allCase,int passCase,long runTime,String user) {
        String ip = OS.getLocalIP();
        int failCase = allCase - passCase;
        String passRate = numberFormat.format((float) passCase / (float) allCase * 100) + "%";  //通过率
        String h = "<TABLE Align='center' width=1000px>\n" +
                "    <TR>\n" +
                "        <TD Align='center'>\n" +
                "         <span style='font-family:微软雅黑;font-size:42px;font-weight:normal;font-style:italic;text-decoration:none;color:#31c5ff;'><strong>自动化测试报告</strong></span>\n" +
                "        </TD>\n" +
                "    </TR>\n" +
                "</table>\n" +
                "<div>\n" +
                "    <hr size=\"1\" width=\"90%\">\n" +
                "    <TABLE Align='center' class=\"details\"  border=0 cellpadding=5 cellspacing=2 width=85%>\n" +
                "        <tr>\n" +
                "            <td>\n" +
                "            <h1>概要</h1>\n" +
                "            </td>\n" +
                "            <Td colspan=\"6\"></Td>\n" +
                "        </tr>\n" +
                "        <tr valign=\"top\">\n" +
                "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                "                用例总数\n" +
                "            </th>\n" +
                "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                "                通过数\n" +
                "            </th>\n" +
                "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                "                失败数\n" +
                "            </th>\n" +
                "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                "                通过率\n" +
                "            </th>\n" +
                "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                "                运行时间\n" +
                "            </th>\n" +
                "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                "                执行机器IP\n" +
                "            </th>\n" +
                "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                "                执行人\n" +
                "            </th>\n" +
                "        </tr>\n" +
                "        <tr valign=\"top\" class=\"Failure\">\n" +
                "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">" + allCase + "</td>\n" +
                "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\"><span style =\"color: green;\">" + passCase + "</span></td>\n" +
                "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\"><span style =\"color: red;\">" + failCase + "</span></td>\n" +
                "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">" + passRate + "</td>\n" +
                "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">" + runTime + "秒</td>\n" +
                "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">" + ip + "</td>\n" +
                "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">" + user + "</td>\n" +
                "        </tr>\n" +
                "\n" +
                "    </table>\n";
        String detail =
                "    <TABLE Align='center' class=\"details\"  border=0 cellpadding=5 cellspacing=2 width=80%>\n" +
                        "        <tr>\n" +
                        "            <td>\n" +
                        "                <h1>详细</h1>\n" +
                        "            </td>\n" +
                        "            <Td colspan=\"6\"></Td>\n" +
                        "        </tr>\n" +
                        "        <tr valign=\"top\">\n" +
                        "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                        "                测试场景\n" +
                        "            </th>\n" +
                        "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                        "                用例数量\n" +
                        "            </th>\n" +
                        "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                        "                通过用例\n" +
                        "            </th>\n" +
                        "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                        "                失败用例\n" +
                        "            </th>\n" +
                        "            <th style=\" color: #ffffff;font-weight: bold;text-align: center;background: #2674a6;white-space: nowrap;\">\n" +
                        "                通过率\n" +
                        "            </th>\n" +
                        "        </tr>\n" +
                        "        <tr valign=\"top\" class=\"Failure\">\n" +
                        "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">22</td>\n" +
                        "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">7</td>\n" +
                        "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">68.18%</td>\n" +
                        "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">25 ms</td>\n" +
                        "            <td align=\"center\" style=\"background: #eeeee0;white-space: nowrap;\">NaN</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "</div></br>";
        h = h + "<TABLE Align='center'  width=600px  >\n" +
                "<tr> <td>" +
                "<h1></h1></td></tr>" +
                "    <tr>" +
                "        <td Align='center'>\n" +
                "            <img src='cid:passRate'' width=800 height=400  Align='center' alt=''>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n";
        return h;
    }
}

