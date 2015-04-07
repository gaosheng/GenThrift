package com.gs.thrift.servlet;

import com.gs.thrift.model.GenThriftForm;
import com.gs.thrift.param.Settings;
import com.gs.thrift.result.GenThriftFlag;
import com.gs.thrift.result.GenThriftResult;
import com.gs.thrift.service.GenThriftService;
import com.gs.thrift.utils.ShellProcess;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;


@Controller
public class GenThriftServlet {

    private static final Logger logger = LoggerFactory.getLogger(GenThriftServlet.class);

    @Autowired
    private Settings settings;

    private String timestamp;
    private GenThriftService genThriftService;
    private GenThriftResult result;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showHome(Model model) {
        model.addAttribute("genThriftForm", new GenThriftForm());
        return new ModelAndView("genThrift");
    }

    @RequestMapping(value = "/gen", method = RequestMethod.POST)
    public String homeFormHandle(@RequestParam("idlFiles") MultipartFile[] idlFiles,
                                 @ModelAttribute("genThriftForm") GenThriftForm genThriftForm,
                                 HttpSession session) throws Exception {

        timestamp = Long.toString(System.currentTimeMillis());
        logger.info("\n\n\n" + timestamp);

        //创建文件的根路径
        String fileDir = settings.getCommonDir() + timestamp + "/";
        String mkDir = "mkdir " + fileDir;
        ShellProcess.runShell(mkDir);

        for (MultipartFile idlFile : idlFiles) {
            if (idlFile.isEmpty()) {
                logger.warn("Upload files is null!");
            } else {
                FileUtils.copyInputStreamToFile(idlFile.getInputStream(), new File(fileDir, idlFile.getOriginalFilename()));
            }
        }

        genThriftService = new GenThriftService(genThriftForm, timestamp, settings);
        result = genThriftService.service();

        session.setAttribute("result", result);
        session.setAttribute("settings", settings);
        session.setAttribute("file", timestamp);

        if (GenThriftFlag.RUNERROR == result.getFlag()) {
            return "redirect:/";
        } else {
            return "redirect:/success";
        }

    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public ModelAndView showHelp() {
        return new ModelAndView("help");
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public ModelAndView showSuccess() {
        genThriftService.afterGen();
        return new ModelAndView("success");
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public ModelAndView showResult() {
        return new ModelAndView("result");
    }


}
