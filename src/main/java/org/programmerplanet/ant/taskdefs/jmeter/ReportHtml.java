package org.programmerplanet.ant.taskdefs.jmeter;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jfree.data.general.DefaultPieDataset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReportHtml {
    private static int allCase = 0;
    private static int passCase = 0;
    private static NumberFormat numberFormat = NumberFormat.getInstance();

    private static int caseNum = 0;

    public static void main(String[] args) {
        ReportHtml.write(
            "C:\\Users\\linjun\\Desktop\\temp\\TestReport201708310339.jtl",
            "C:\\Users\\linjun\\Desktop\\temp\\report.html", "林俊");
    }

    public static String write(String jtlPath, String outHtmlPath,
            String runUser) {
        System.out.println("开始读取JTL文件,路径:" + jtlPath);
        Document document = null;
        DocumentBuilder documentBuilder = null;
        File file = new File(jtlPath);
        try {
            DocumentBuilderFactory factory = null;
            factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
            document = documentBuilder.parse(file);
        } catch (Exception e) {
            System.out.println("转换JTL文件失败，请确认格式是否正确:" + e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("读取JTL文件完毕，开始获取测试用例:");
        Element element = document.getDocumentElement();
        NodeList nodeList = element.getChildNodes();
        long startTime = 0L;
        long endTime = 0L;
        List<ReportCase> reportCaseList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String nodeName = node.getNodeName();
            if (nodeName.contains("Sample") || nodeName.contains("sample")) {
                ReportCase reportCase = new ReportCase();
                NamedNodeMap namedNodeMap = node.getAttributes();
                try {
                    Node lb = namedNodeMap.getNamedItem("lb");
                    String caseName = lb.getNodeValue();
                    reportCase.setCaseName(caseName);
                    Node runTimeNode = namedNodeMap.getNamedItem("t");
                    if (!ReportHtml.isEmpty(runTimeNode)) {
                        reportCase
                            .setRunTime(runTimeNode.getNodeValue() + "ms");
                    }
                    Node responseCodeNode = namedNodeMap.getNamedItem("rc");
                    if (!ReportHtml.isEmpty(responseCodeNode)) {
                        reportCase
                            .setResponseCode(responseCodeNode.getNodeValue());
                    }
                    Node responseMessageNode = namedNodeMap.getNamedItem("rm");
                    if (!ReportHtml.isEmpty(responseMessageNode)) {
                        reportCase.setResponseMessage(
                            responseMessageNode.getNodeValue());
                    }
                    Node startTimeNode = namedNodeMap.getNamedItem("ts");
                    if (!ReportHtml.isEmpty(startTimeNode)) {
                        String startTimeStr = startTimeNode.getNodeValue();
                        if (startTime == 0L) {
                            startTime = Long.parseLong(startTimeStr);
                        }

                        endTime = Long.parseLong(startTimeStr);
                    }

                    Node statusNode = namedNodeMap.getNamedItem("s");
                    if (!ReportHtml.isEmpty(statusNode)) {
                        String s = statusNode.getNodeValue();
                        boolean status = Boolean.parseBoolean(s);

                        reportCase.setStatus(status);
                    }

                    Node tnNode = namedNodeMap.getNamedItem("tn");
                    if (!ReportHtml.isEmpty(tnNode)) {
                        String s = tnNode.getNodeValue();
                        reportCase.setSuiteName(s);
                    }

                    Node assertionNode = ReportHtml.getNodeByName(node,
                        "assertionResult");
                    if (!ReportHtml.isEmpty(assertionNode)) {
                        AssertionResult assertionResult = ReportHtml
                            .formatAssertion(assertionNode);
                        reportCase.setAssertionResult(assertionResult);
                    } else {
                        reportCase.setAssertionResult(null);
                    }
                    Node responseDataNode = ReportHtml.getNodeByName(node,
                        "responseData");
                    if (!ReportHtml.isEmpty(responseDataNode)) {
                        reportCase
                            .setResponseData(responseDataNode.getTextContent());
                    }

                    Node methodNode = ReportHtml.getNodeByName(node, "method");
                    if (!ReportHtml.isEmpty(methodNode)) {
                        reportCase.setMethodName(methodNode.getTextContent());
                    }
                    Node queryStringNode = ReportHtml.getNodeByName(node,
                        "queryString");
                    if (!ReportHtml.isEmpty(queryStringNode)) {
                        reportCase
                            .setQueryString(queryStringNode.getTextContent());
                    }
                    Node urlNode = ReportHtml.getNodeByName(node,
                        "java.net.URL");
                    if (!ReportHtml.isEmpty(urlNode)) {
                        reportCase.setUrl(urlNode.getTextContent());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                reportCaseList.add(reportCase);
            }
        }
        System.out.println("用例转换完毕，开始写入报告，写入路径:" + outHtmlPath);
        long runTime = (endTime - startTime) / 1000L;
        ReportHtml.writeReport(reportCaseList, runTime, outHtmlPath, runUser);
        String mailContent = MailReport.reportHtml(ReportHtml.allCase,
            ReportHtml.passCase, runTime, runUser);
        return mailContent;
    }

    private static boolean isEmpty(Object o) {
        boolean f = false;
        if (o == null) {
            f = true;
        }
        return f;
    }

    private static AssertionResult formatAssertion(Node assertionNode) {
        AssertionResult assertionResult = new AssertionResult();
        Node nameNode = ReportHtml.getNodeByName(assertionNode, "name");
        assertionResult.setName(nameNode.getTextContent());
        Node failureNode = ReportHtml.getNodeByName(assertionNode, "failure");
        boolean failure = Boolean.parseBoolean(failureNode.getTextContent());
        assertionResult.setFailure(failure);
        Node errorNode = ReportHtml.getNodeByName(assertionNode, "error");
        boolean error = Boolean.parseBoolean(errorNode.getTextContent());
        assertionResult.setError(error);
        Node failureMessageNode = ReportHtml.getNodeByName(assertionNode,
            "failureMessage");
        if (!ReportHtml.isEmpty(failureMessageNode)) {
            assertionResult
                .setFailureMessage(failureMessageNode.getTextContent());
        }
        return assertionResult;
    }

    public static Node getNodeByName(Node node, String name) {
        NodeList msgNodeList = node.getChildNodes();
        Node node1 = null;
        for (int k = 0; k < msgNodeList.getLength(); k++) {
            Node childNode = msgNodeList.item(k);
            String childName = childNode.getNodeName();
            if (childName.equalsIgnoreCase(name)) {
                node1 = childNode;
                break;
            }
        }
        return node1;
    }

    public static void writeReport(List<ReportCase> reportCaseList,
            long runTime, String htmlPath, String runUser) {
        String title = ReportHtml.getTitle() + ReportHtml.getBodyTitle();
        String suiteBody = ReportHtml.getSuiteList(reportCaseList)
            + ReportHtml.getCaseStep(reportCaseList) + ReportHtml.htmlEnd();
        String summary = ReportHtml.getSummary(runTime, runUser);
        String html = title + summary + suiteBody;
        String path = "";
        File directory = new File(htmlPath);
        try {
            path = directory.getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File htmlPathFile = new File(htmlPath);
            String pathParent = htmlPathFile.getParent();
            File parentFile = new File(pathParent);
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileUtil.writeFile(htmlPath, html);

        System.out.println("报告写入结束!!!");
        System.out.println("开始生成饼状图!!!");
        try {
            DefaultPieDataset dataset = new DefaultPieDataset();
            int casePassCount = ReportHtml.passCase;
            int caseFailCount = ReportHtml.allCase - ReportHtml.passCase;
            dataset.setValue("PassCase", new Double(casePassCount));
            dataset.setValue("FailCase", new Double(caseFailCount));
            String pngPath = path + File.separator + "report.png";
            PicReport picReport = new PicReport();
            picReport.save(dataset, pngPath, "通过率");
            System.out.println("饼状图生成结束,路径:" + pngPath);
        } catch (Exception e) {
            System.out.println("饼状图生成失败:" + e);
        }
    }

    private static String getTitle() {
        return ReportHtml.getHeadStart() + ReportHtml.getStyle()
            + ReportHtml.getJavaScript() + ReportHtml.getHeadEnd();
    }

    private static String getBodyTitle() {
        String reportName = "自动化测试报告";
        String date = DateUtil.getCurrentDate();
        String bodyTitle = "<body><h1>" + reportName + "</h1>"
            + "<table width=\"100%\">" + "    <tr>"
            + "        <td align=\"left\">Date report: " + date + "</td>"
            + "        <td align=\"right\">Designed for use with <a href=\"#\">longteng</a> and <a href=\"#\">贾鸥</a>.modified by<a href=\"mailTo:linjun@ucredit.com\">林俊</ </td>"
            + "    </tr>" + "</table>";
        return bodyTitle;
    }

    private static String getSummary(long runTimeLong, String runUser) {
        int failCase = ReportHtml.allCase - ReportHtml.passCase;
        String passRate = ReportHtml.numberFormat
            .format(ReportHtml.passCase / ReportHtml.allCase * 100.0F) + "%";

        String runTime = runTimeLong + "秒";
        String ip = OS.getLocalIP();
        String summary = "" + "<hr size=\"1\">" + "<h2>概要</h2>"
            + "<table align=\"center\" class=\"details\" border=\"0\" cellpadding=\"5\" cellspacing=\"2\" width=\"95%\">"
            + "    <tr valign=\"top\">" + "        <th>用例总数</th>"
            + "        <th>通过数</th>" + "        <th>失败数</th>"
            + "        <th>通过率</th>" + "        <th>运行时间</th>"
            + "        <th>执行机器IP</th>" + "        <th>执行人</th>" + "    </tr>"
            + "    <tr valign=\"top\" class=\"\">"
            + "        <td align=\"center\">" + ReportHtml.allCase + "</td>"
            + "        <td align=\"center\">"
            + "           <span style=\"color: green\">" + "             <b>"
            + ReportHtml.passCase + "</b>" + "           </span>"
            + "        </td>" + "        <td align=\"center\">"
            + "              <span style=\"color: red;width:120px;height:20px;\">"
            + "             <b>" + failCase + "</b>" + "           </span></td>"
            + "        <td align=\"center\">" + passRate + "</td>"
            + "        <td align=\"center\">" + runTime + "</td>"
            + "        <td align=\"center\">" + ip + "</td>"
            + "        <td align=\"center\">" + runUser + "</td>" + "    </tr>"
            + "</table>" + "</br>";
        return summary;
    }

    private static String getSuiteList(List<ReportCase> reportCaseList) {
        String s = "<hr size=\"1\" width=\"100%\" align=\"center\">" + "<div>"
            + "    <div id=\"div_left\" style=\"overflow:auto\">"
            + " <table id=\"suites\">";
        String suites = "";
        int i = 0;

        String tbodyId = "tests-" + i;
        String toggleId = "toggle-" + i;

        Map<String, List<ReportCase>> suiteMap = new LinkedHashMap<>();

        for (ReportCase reportCase : reportCaseList) {
            List<ReportCase> caseList = suiteMap.getOrDefault(
                reportCase.getSuiteName(), new LinkedList<ReportCase>());
            caseList.add(reportCase);
            suiteMap.put(reportCase.getSuiteName(), caseList);
        }

        for (String suite : suiteMap.keySet()) {
            suites = suites + "<table>";
            i++;
            tbodyId = "tests-" + i;
            toggleId = "toggle-" + i;
            suites = suites
                + "<thead>            <tr>                <th class=\"suite\" onclick=\"toggleElement('"
                + tbodyId + "', 'table-row-group'); toggle('" + toggleId
                + "')\">" + "                    <span id=\"" + toggleId
                + "\">&#x25bc;</span>" + "                    <span id=\"" + i
                + "\">" + suite + "</span>" + "                </th>"
                + "            </tr>" + "            </thead>";
            String suiteCase = ReportHtml.getCaseList(tbodyId,
                suiteMap.get(suite));
            suites = suites + suiteCase;
            suite = suite + "</table></tbody>";
        }

        String center = "<div id=\"div_center\">    </div>";

        s = s + suites + "  </table></div>" + center;

        return s;
    }

    private static String getCaseList(String tbodyId,
            List<ReportCase> caseList) {
        String c = "<tbody id=\"" + tbodyId + "\" class=\"tests\">";
        String div = "<div id=\"allSpan\" style=\"display:none\">";
        for (int i = 0; i < caseList.size(); i++) {
            ReportCase reportCase = caseList.get(i);
            String caseName = reportCase.getCaseName();
            boolean caseStatus = reportCase.isStatus();
            c += "<tr><td class=\"test\">";
            ReportHtml.allCase++;
            div += "<div>";
            if (caseStatus) {
                ReportHtml.passCase++;
                c += " <span class=\"successIndicator\" title=\"全部通过\">&#x2714;</span>";
                div += " <span class=\"successIndicator\" title=\"全部通过\">&#x2714;</span>";
            } else {
                c += " <span class=\"failureIndicator\" title=\"部分失败\">&#x2718;</span>";
                div += " <span class=\"failureIndicator\" title=\"部分失败\">&#x2718;</span>";
            }
            c += "   <a id=\"Case" + ReportHtml.caseNum
                + "\" href=\"#\" onclick=\"showDetail(this)\">" + caseName
                + "</a>" + "   </td>" + "   </tr>";
            div += "<a  href=\"#\" onclick=\"showDetail(this)\">" + caseName
                + "</a>";
            div += "</div>";
            ReportHtml.caseNum++;
        }
        div += "</div>";
        c += div;
        c += "</tbody>";
        return c;
    }

    private static String htmlEnd() {
        return "</div></div></body></html>";
    }

    private static String getCaseStep(List<ReportCase> reportCaseList) {
        String div = "<div id=\"div_right\" style=\"overflow:auto\">";
        div = div + "<ol id=\"right-panel\">\n";
        ReportHtml.caseNum = 0;
        String caseDiv = "";
        String firstCaseName = "";
        String step = "";
        for (int k = 0; k < reportCaseList.size(); k++) {
            ReportCase reportCase = reportCaseList.get(k);
            String caseName = reportCase.getCaseName();
            String display = "none";
            if (ReportHtml.caseNum == 0) {
                display = "";
                firstCaseName = caseName;
            }
            step = step + "<div id =\"parentCase" + ReportHtml.caseNum
                + "\" style=\"display: " + display + "\">";
            step = step + ReportHtml.newD(reportCase);
            step = step + "</div>";
            ReportHtml.caseNum += 1;
        }
        caseDiv = caseDiv + step;
        div = div + "<table><tr><td><h1 id =\"caseName\">当前用例:" + firstCaseName
            + "</h1></td>" + "<td>\n" + "    &nbsp;&nbsp;&nbsp;&nbsp;\n"
            + "</td>\n" + "</tr>" + "</table>";
        div = div + caseDiv;
        div = div + "<input id=\"allCaseNum\" type=\"hidden\" value=\""
            + ReportHtml.caseNum + "\">"
            + " <input id=\"currentCaseId\" type=\"hidden\" value=\"parentCase0\">"
            + "</div></div>";
        return div;
    }

    private static String newD(ReportCase reportCase) {
        AssertionResult assertionResult = reportCase.getAssertionResult();
        String div = "<div class=\"group\">Sampler</div>\n<div class=\"zebra\">\n<table>\n<tr><td class=\"data key\">Time</td><td class=\"data delimiter\">:</td><td class=\"data\">"
            + reportCase.getRunTime() + "</td></tr>\n"
            + "<tr><td class=\"data key\">Response Code</td><td class=\"data delimiter\">:</td><td class=\"data\">"
            + reportCase.getResponseCode() + "</td></tr>\n"
            + "<tr><td class=\"data key\">Response Message</td><td class=\"data delimiter\">:</td><td class=\"data\">"
            + reportCase.getResponseMessage() + "</td></tr>\n" + "</table>\n"
            + "</div>\n";
        if (assertionResult != null
            && (assertionResult.isFailure() || assertionResult.isError())) {
            div = div
                + "<div class=\"trail\"></div>\n<div class=\"group\">Assertion</div>\n<div class=\"zebra\">\n<table>\n<tbody class=\"failure\"><tr><td class=\"data assertion\" colspan=\"3\">"
                + assertionResult.getName() + "</td></tr>\n"
                + "<tr><td class=\"data key\">Failure</td><td class=\"data delimiter\">:</td><td class=\"data\">"
                + assertionResult.isFailure() + "</td></tr>\n"
                + "<tr><td class=\"data key\">Error</td><td class=\"data delimiter\">:</td><td class=\"data\">"
                + assertionResult.isError() + "</td></tr>\n"
                + "<tr><td class=\"data key\">Failure Message</td><td class=\"data delimiter\">:</td>\n"
                + "<td class=\"data\">" + assertionResult.getFailureMessage()
                + "</td></tr></tbody></table></div>\n";
        }

        div = div
            + "<div class=\"trail\">\n</div><div class=\"group\">Request</div>\n<div class=\"zebra\"><table><tr><td class=\"data key\">接口/Url</td><td class=\"data delimiter\">:</td><td class=\"data\"><pre class=\"data\">"
            + reportCase.getUrl() + "</pre></td></tr>"
            + "<tr><td class=\"data key\">Method</td><td class=\"data delimiter\">:</td><td class=\"data\"><pre class=\"data\">"
            + reportCase.getMethodName() + "</pre></td></tr>"
            + "<tr><td class=\"data key\">Query String</td><td class=\"data delimiter\">:</td><td class=\"data\"><pre class=\"data\">"
            + reportCase.getQueryString() + "</pre></td></tr>\n" + "</table>\n"
            + "</div>\n" + "<div class=\"trail\"></div>\n"
            + "<div class=\"group\">Response</div><div class=\"zebra\">\n"
            + "<table>\n"
            + "<tr><td class=\"data key\">Response Data</td><td class=\"data delimiter\">:</td><td class=\"data\">\n"
            + "<pre class=\"data\">" + reportCase.getResponseData()
            + "</pre></td></tr>\n" + "</table>\n" + "</div>\n";
        return div;
    }

    private static String getStyle() {
        String style = "<style type=\"text/css\">        body {            font:normal 68% verdana,arial,helvetica;            color:#000000;        }        table tr td, table tr th {            font-size: 68%;        }        table.details tr th{            color: #ffffff;            font-weight: bold;            text-align:center;            background:#2674a6;            white-space: nowrap;        }        table.details tr td{            background:#eeeee0;            white-space: nowrap;        }        h1 {            margin: 0px 0px 5px; font: 165% verdana,arial,helvetica        }        h2 {            margin-top: 1em; margin-bottom: 0.5em; font: bold 125% verdana,arial,helvetica        }        h3 {            margin-bottom: 0.5em; font: bold 115% verdana,arial,helvetica        }        .Failure {            font-weight:bold; color:red;        }        img        {            border-width: 0px;        }        #div_left {            float: left;            width: 15%;            height: 100%;        }        #div_center {            float: left;            width: 2%;            height: 100%;        }        #div_right {            float: left;            width: 83%;            height: 100%;        }\n#right-panel {\nmargin-left: -40;    right: 0;\n    top: 0;\n    bottom: 0;\n    left: 11px;\n    overflow: auto;\n    background: white\n}\n\n#right-panel .group {\n    font-size: 15px;\n    font-weight: bold;\n    line-height: 16px;\n    padding: 0 0 0 18px;\n    counter-reset: assertion;\n    background-repeat: repeat-x;\n    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAQCAYAAADXnxW3AAAAAXNSR0IArs4c6QAAAAZiS0dEAP8A/wD/oL2nkwAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9sDEBUkDq8pxjkAAAAdaVRYdENvbW1lbnQAAAAAAENyZWF0ZWQgd2l0aCBHSU1QZC5lBwAAADdJREFUCNdVxrERwDAMAzGK0v47eS6Z927SpMFBAAbkvSvnRk5+7K5cVfLMyN39bWakJAjA5xw9R94jN3tVhVEAAAAASUVORK5CYII=)\n}\n\n#right-panel .zebra {\n    background-repeat: repeat;\n    padding: 0 0 0 18px;\n    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAmCAYAAAAFvPEHAAAAAXNSR0IArs4c6QAAAAZiS0dEAP8A/wD/oL2nkwAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9sDEBYWFlNztEcAAAAdaVRYdENvbW1lbnQAAAAAAENyZWF0ZWQgd2l0aCBHSU1QZC5lBwAAABdJREFUCNdjYKAtePv5338mBgYGBpoQAGy1BAJlb/y6AAAAAElFTkSuQmCC)\n}\n\n#right-panel .data {\n    line-height: 19px;\n    white-space: nowrap\n}\n\n#right-panel pre.data {\n    white-space: pre\n}\n\n#right-panel tbody.failure {\n    color: red\n}\n\n#right-panel td.key {\n    min-width: 108px\n}\n\n#right-panel td.delimiter {\n    min-width: 18px\n}    </style>    <style type=\"text/css\">        #suites {            line-height: 1.7em;            border-spacing: 0.1em;            width: 100%;        }        .suite {            text-align: left;            background-color: #eeeeee;            font-weight: bold;        }        .header {            font-size: 1.0em;            font-weight: bold;            text-align: left;        }        .header.suite {            cursor: pointer;            clear: right;            height: 1.214em;            margin-top: 1px;        }        .toggle {            font-family: monospace;            font-weight: bold;            padding-left: 2px;            padding-right: 5px;            color: #777777;        }        .test {            background-color: #eeeeee;            padding-left: 2em;        }        .successIndicator {            float: right;            font-family: monospace;            font-weight: bold;            padding-right: 2px;            color: #44aa44;        }        .skipIndicator {            float: right;            font-family: monospace;            font-weight: bold;            padding-right: 2px;            color: #ffaa00;        }        .failureIndicator {            float: right;            font-family: monospace;            font-weight: bold;            padding-right: 2px;            color: #ff4444;        } .resultsTitleTable {\n        border: 0;\n        width: 100%;\n        margin-top: 1.8em;\n        line-height: 1.7em;\n        border-spacing: 0.1em;\n    }        .resultsTable {            border: 0;            width: 100%;            line-height: 1.7em;            border-spacing: 0.1em;        }        .resultsTable .method {            width: 1em;        }        .resultsTable.passed {\n        background: #008000;width: 1em;\n    }\n    .resultsTable.failure {\n        background: red;width: 1em;\n    }        .resultsTable .duration {            width: 6em;        }        .resultsTable td {            vertical-align: top;        }        .passed {            background-color: #44aa44;width: 1em;        }        .skipped {            background-color: #ffaa00;width: 1em;        }        .failed {            background-color: #ff4444;width: 1em;        }        .arguments {            font-family: Lucida Console, Monaco, Courier New, monospace;            font-weight: bold;        }    </style>\n";

        return style;
    }

    private static String getJavaScript() {
        String javaScript = "\n<script language=\"JavaScript\">\n        function toggleElement(elementId, displayStyle)        {            var current = getStyle(elementId, 'display');            document.getElementById(elementId).style.display = (current == 'none' ? displayStyle : 'none');        }        function getStyle(elementId, property)        {            var element = document.getElementById(elementId);            return element.currentStyle ? element.currentStyle[property]                    : document.defaultView.getComputedStyle(element, null).getPropertyValue(property);        }\n        function toggle(toggleId)        {            var toggle;            if (document.getElementById)            {                toggle = document.getElementById(toggleId);            }            else if (document.all)            {                toggle = document.all[toggleId];            }            toggle.textContent = toggle.innerHTML == '\\u25b6' ? '\\u25bc' : '\\u25b6';        }\n/*\n* 删除左面菜单所有子元素\n* */\nfunction deleteAllTestBody(testTbody) {\n    var trArray = testTbody.childNodes;\n    var length = trArray.length;\n    for (var i = 0; i < length; i++) {\n        try {\n            var nodeName = trArray[i].nodeName;\n            if (nodeName == \"TR\") {\n                testTbody.removeChild(trArray[i]);\n            }\n        } catch (e) {\n\n        }\n    }\n}\nfunction showCaseType(obj) {\n    var status = \"successIndicator\";\n    status=obj.value;\n    var testTbody = document.getElementById(\"tests-0\");\n    deleteAllTestBody(testTbody);\n    var allSpan = document.getElementById(\"allSpan\");\n    var spanDivList = allSpan.getElementsByTagName(\"div\");\n    var htmlTr = \"\";\n    var index =0;\n    for (var i = 0; i < spanDivList.length; i++) {\n        var div = spanDivList[i];\n        try {\n            var span = div.getElementsByTagName(\"span\")[0];\n            var a = div.getElementsByTagName(\"a\")[0];\n            a.setAttribute(\"id\", \"Case\" + index);\n            var spanOuterHTML = span.outerHTML;\n            var aOuterHTML = a.outerHTML;\n            if (status == \"all\") {\n                htmlTr += \"<tr><Td class='test'>\" + spanOuterHTML + aOuterHTML + \"</Td></tr>\"\n            } else {\n                var spanClassName = span.className;\n                if (status == spanClassName) {\n                    htmlTr += \"<tr><Td class='test'>\" + spanOuterHTML + aOuterHTML + \"</Td></tr>\"\n                }\n            }\n            index++;\n        } catch (E) {\n\n        }\n    }\n    testTbody.innerHTML=htmlTr;\n}\nfunction searchCase() {\n    var searchText = document.getElementById(\"searchText\").value;\n    var table = document.getElementById(\"suites\");\n    var aList = table.getElementsByTagName(\"a\");\n    if (aList.length > 0) {\n        var index=0;\n        for (var i = 0; i < aList.length; i++) {\n            var obj = aList[i];\n            var text = obj.text;\n            if (searchText == text) {\n                showDetail(obj);\n                break;\n            }\n            console.info(text);\n          index++;\n        }\n        if(index==aList.length){\n            alert(\"当前状态下没有该用例\")\n        }\n    }\n}function showDetail(obj) {\n var caseId = obj.id;\n  document.getElementById(\"currentCaseId\").value = caseId; \n var caseName =obj.text;\n document.getElementById(\"caseName\").innerHTML=\"当前用例:\"+caseName;\n  var parentCaseId=\"parent\"+caseId;\n  var allCaseNum = document.getElementById(\"allCaseNum\").value;\n        for (var i = 0; i < allCaseNum; i++) {\n            var div = \"parentCase\" + i;\n            if (div == parentCaseId) {\n               document.getElementById(div).style.display=\"inline\"\n            } else {\n               document.getElementById(div).style.display=\"none\"\n            }\n        }\n    }\n    </script>";

        return javaScript;
    }

    private static String getHeadStart() {
        String title = "<!DOCTYPE html private \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html><head>    <META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">    <title>Load Test Results</title>";

        return title;
    }

    private static String getHeadEnd() {
        return "</head>";
    }
}