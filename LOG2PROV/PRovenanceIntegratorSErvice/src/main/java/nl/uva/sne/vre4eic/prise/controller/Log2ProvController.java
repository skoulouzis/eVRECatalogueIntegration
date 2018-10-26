/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.controller;

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
    String submit(@RequestParam("files") MultipartFile[] files) {
        for (MultipartFile file : files) {
            System.err.println(file.getContentType());
            System.err.println(file.getOriginalFilename());
        }

        return "done";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    String get() {
        return "done";
    }

}
