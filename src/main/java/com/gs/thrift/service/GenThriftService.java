package com.gs.thrift.service;

import com.gs.thrift.constant.Consts;
import com.gs.thrift.model.GenThriftForm;
import com.gs.thrift.param.Settings;
import com.gs.thrift.result.GenThriftFlag;
import com.gs.thrift.result.GenThriftResult;
import com.gs.thrift.utils.ParseFile;
import com.gs.thrift.utils.ShellProcess;
import com.gs.thrift.utils.TemplateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class GenThriftService {

    private static final Logger logger = LoggerFactory.getLogger(GenThriftService.class);

    private String filename;                //.thrift文件名
    private String language;                //编译语言参数
    private String thriftVersion;           //thrift版本
    private boolean genFrame;               //是否生成框架

    private String timestamp;               //文件名（时间戳）
    private Settings settings;              //路径相关设置

    private String thriftDir;               //thrift目录
    private String rootDir;                 //根目录
    private String fileDir;                 //文件目录

    private GenThriftResult result;

    private ParseFile parseFile;
    private TemplateHandler th;

    private String thriftFileName;          //生成的目标文件的绝对路径
    private String thriftTestDir;           //测试代码框架目录


    private String pack;                    //IDL文件包名
    private String service;                 //IDL文件service名
    private List<String> methodList;        //IDL文件方法列表


    public GenThriftService(GenThriftForm genThriftForm, String timestamp, Settings settings) {

        this.filename = genThriftForm.getFilename();
        this.language = genThriftForm.getLanguage();
        this.thriftVersion = genThriftForm.getThriftVersion();
        this.genFrame = ("yes".equals(genThriftForm.getGenFrame())) ? true : false;

        this.timestamp = timestamp;
        this.settings = settings;

        this.rootDir = settings.getCommonDir() + timestamp + "/";
        this.fileDir = this.rootDir + filename + ".thrift";

        result = new GenThriftResult();
        result.setFlag(GenThriftFlag.NOTRUN);
        result.setError("");

        parseFile = new ParseFile();
        th = new TemplateHandler();
    }

    public void checkLanguage() {
        if ("c++".equals(language))
            language = "cpp";
        else if ("python".equals(language))
            language = "py";
    }

    public void checkThriftDir() {
        if ("0.8.0".equals(thriftVersion))
            thriftDir = settings.getThriftDir080();
        else
            thriftDir = settings.getThriftDir092();

    }

    public boolean genThrift(String outputDir) {

        //-out指定输出目标文件的路径，-I指定include搜索路径，-r指定编译include文件
        String thriftCommand = thriftDir + " -out " + outputDir + " -I " + rootDir + " -gen " + language + " " + fileDir;

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(thriftCommand);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        logger.info(thriftCommand);

        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        //读取错误输出
        InputStreamReader isr = new InputStreamReader(process.getErrorStream());
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuffer sb = new StringBuffer();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        String error = sb.toString();
        result.setError(error);

        //1:发生错误
        if (1 == exitCode) {
            result.setFlag(GenThriftFlag.RUNERROR);
            logger.warn(result.getError());
            return false;
        }
        return true;
    }

    public void parseDir() {

        String name[] = pack.split("\\.");

        StringBuffer sbFileName = new StringBuffer();
        StringBuffer sbTestDir = new StringBuffer();

        sbFileName.append(rootDir);
        sbTestDir.append(rootDir + Consts.TEST_NAME + "/src/main/java/");


        for (int i = 0; i < name.length; i++) {
            sbFileName.append(name[i] + "/");
            sbTestDir.append(name[i] + "/");
        }
        sbFileName.append(service + ".java");

        this.thriftFileName = sbFileName.toString();
        this.thriftTestDir = sbTestDir.toString();

        logger.info("ThriftFileName:{}", this.thriftFileName);
        logger.info("ThriftTestDir:{}", this.thriftTestDir);

    }

    public void genTestFrame() {

        String cpTest = "cp -r " + settings.getCommonDir() + Consts.TEST_NAME + " " + rootDir + Consts.TEST_NAME;
        ShellProcess.runShell(cpTest);

        String thriftTestDemoDir = rootDir + Consts.TEST_NAME + "/src/main/java";

        //再次调用thrift命令重新生成到AutoGenThriftTest
        genThrift(thriftTestDemoDir);

        String templateServer = th.genServer(pack, service);
        th.writeFile(thriftTestDir + "Server.java", templateServer);

        String templateClient = th.genClient(pack, service);
        th.writeFile(thriftTestDir + "Client.java", templateClient);

        String templateServiceImpl = th.genServiceImpl(pack, service, methodList);
        th.writeFile(thriftTestDir + service + "Impl.java", templateServiceImpl);

        String templateServiceImplTest = th.genServiceImplTest(pack, service, methodList);
        th.writeFile(thriftTestDir + service + "ImplTest.java", templateServiceImplTest);
    }

    public void afterGen() {
        //生成打包脚本文件
        String cd = "cd " + settings.getCommonDir();
        String tar = "tar -zcf " + timestamp + ".zip " + timestamp;
        String shell = cd + "\n" + tar;
        String shFile = settings.getCommonDir() + timestamp + ".sh";

        try {
            FileWriter sh = new FileWriter(shFile);
            sh.write(shell, 0, shell.length());
            sh.flush();
            sh.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        String chmod = "chmod 777 " + shFile;
        ShellProcess.runShell(chmod);
        ShellProcess.runShell(shFile);
        String rmShFile = "rm -rf " + shFile;
        ShellProcess.runShell(rmShFile);
        String rmFile = "rm -rf " + settings.getCommonDir() + timestamp;
        ShellProcess.runShell(rmFile);

        //设置成功执行标志
        result.setFlag(GenThriftFlag.RUNSUCCESS);
    }

    public GenThriftResult service() {

        checkLanguage();
        checkThriftDir();

        if (!genThrift(rootDir))
            return result;

        //只有java才生出测试代码框架
        if (genFrame && "java".equals(language)) {

            parseFile.parseThriftFile(fileDir);

            pack = parseFile.getPack();
            service = parseFile.getService();

            logger.info("Package:" + pack);
            logger.info("Service:" + service);

            //如果没有service，就不用生成测试代码
            if (!service.equals("") && service != null) {

                parseDir();
                parseFile.parseMethod(thriftFileName);
                methodList = parseFile.getMethodList();

                //没有方法声明不用生成测试代码
                if (methodList.size() > 0) {
                    genTestFrame();
                }
            }
        }
        return result;
    }
}
