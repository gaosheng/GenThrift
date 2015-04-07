package com.gs.thrift.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class TemplateHandler {

    private static final Logger logger = LoggerFactory.getLogger(TemplateHandler.class);

    public final String test_m1 = "@Test\n" + "public void test";
    public final String test_m2 = "() throws TException { }";


    public String genServer(String pack, String service) {
        String template = readFile("/TestFrame/Server.template");
        template = template.replace("${PACKAGE}", pack);
        template = template.replace("${SERVICE}", service);
        return template;
    }

    public String genClient(String pack, String service) {
        String template = readFile("/TestFrame/Client.template");
        template = template.replace("${PACKAGE}", pack);
        template = template.replace("${SERVICE}", service);
        return template;
    }

    public String genServiceImpl(String pack, String service, List<String> methodList) {
        String template = readFile("/TestFrame/ServiceImpl.template");
        StringBuffer sb_method = new StringBuffer();
        for (int i = 0; i < methodList.size(); i++)
            sb_method.append(methodList.get(i) + "\n");
        String method = sb_method.toString();
        template = template.replace("${PACKAGE}", pack);
        template = template.replace("${SERVICE}", service);
        template = template.replace("${METHOD}", method);
        return template;
    }

    public String genServiceImplTest(String pack, String service, List<String> methodList) {

        String template = readFile("/TestFrame/ServiceImplTest.template");
        StringBuffer sb_testMethod = new StringBuffer();

        //methodList数据：
        //public String sayHello(String username)  throws TException { return null; }
        //public String sayBye(String username)  throws TException { return null; }
        for (int i = 0; i < methodList.size(); i++) {
            String method = methodList.get(i).trim().split("\\s+")[2].split("\\(")[0];
            //改变首字母为大写
            method = method.substring(0, 1).toUpperCase() + method.substring(1);
            sb_testMethod.append(test_m1 + method + test_m2 + "\n\n");
        }

        String method = sb_testMethod.toString();
        template = template.replace("${PACKAGE}", pack);
        template = template.replace("${SERVICE}", service);
        template = template.replace("${METHOD}", method);
        return template;
    }

    public String readFile(String filename) {
        InputStream in = getClass().getResourceAsStream(filename);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

        String line;
        StringBuffer sb = new StringBuffer();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
            in.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        String template;
        template = sb.toString();
        return template;
    }


    public void writeFile(String filename, String template) {

        logger.info("Filename:" + filename);
        FileWriter fw;
        try {
            fw = new FileWriter(filename);
            fw.write(template, 0, template.length());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
