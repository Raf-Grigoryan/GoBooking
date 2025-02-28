package org.example.gobookingweb.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class MainController {


    @Value("${image.upload.path}")
    private String imageUploadPath;

    @GetMapping("/")
    public String getMainPage() {
        return "index";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "redirect:/auth";
    }

    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("imageName") String imageName) throws IOException {
        File file = new File(imageUploadPath + imageName);
        if (file.exists()) {
            try (InputStream in = new FileInputStream(file)) {
                return IOUtils.toByteArray(in);
            }
        }
        return null;
    }

}
