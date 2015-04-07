package com.gs.thrift.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParseFile {

    private static final Logger logger = LoggerFactory.getLogger(ParseFile.class);
    public static final String method = " throws TException { return null; }";

    private String pack;       //java包名
    private String service;    //服务名
    public  List<String> methodList = new ArrayList<String>();    //方法列表


    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<String> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<String> methodList) {
        this.methodList = methodList;
    }


    /**
     * 解析.thrift文件，得到包名和服务名
     * @param filename
     */
    public void parseThriftFile(String filename) {

        this.pack = "";
        this.service = "";

        File file = new File(filename);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }

        String line;
        try {
            while ((line = br.readLine()) != null) {
                parsePackage(line);
                parseService(line);

                if (this.pack != "" && this.service != "")
                    break;
            }
            br.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 解析包名
     * @param str
     */
    public void parsePackage(String str) {
        String s[] = str.trim().split("\\s+");
        if (3 == s.length && s[0].equals("namespace") && s[1].equals("java")) {
            this.pack = s[2];
        }
    }

    /**
     * 解析服务名
     * @param str
     */
    public void parseService(String str) {
        String s[] = str.trim().split("\\s+");
        if (s[0].equals("service")) {
            if (s[1].endsWith("{")) {
                this.service = s[1].substring(0, s[1].length() - 1);
            } else {
                this.service = s[1];
            }
        }
    }


    /**
     * 解析thrift命令生成的目标文件，得到方法列表
     * @param filename
     */
    public void parseMethod(String filename) {

        this.methodList.clear();

        File file = new File(filename);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }

        String line;
        try {
            while ((line = br.readLine()) != null) {
                String s[] = line.trim().split("\\s+");
                if (s[0].equals("public") && s[1].equals("interface") && s[2].equals("Iface")) {

                    while ((line = br.readLine()) != null) {
                        if (line.trim().equals("}"))
                            break;
                        if (!line.trim().equals("")) {
                            this.methodList.add(line.split("throws")[0] + method);
                            logger.info(line.split("throws")[0] + method);
                        }
                    }
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
