/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.log2prov.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class Log2ProvController {

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public @ResponseBody
    String submit(@RequestParam("files") MultipartFile[] files, ModelMap modelMap) {

        modelMap.addAttribute("files", files);
        for (MultipartFile file : files) {
            
            
            System.err.println(file.getName());
            System.err.println(file.getClass().getName());
            System.err.println(file.getContentType());
//                String mimeType = Files.probeContentType(javaFile.toPath());
//                System.err.println(mimeType);

//                mimeType = Magic.getMagicMatch(javaFile.getAbsoluteFile(), false).getMimeType();
//                System.err.println(mimeType);
        }

        return "done";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    String get() {
        return "done";
    }

}
