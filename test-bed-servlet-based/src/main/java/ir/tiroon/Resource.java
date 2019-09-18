package ir.tiroon;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
@RestController
@RequestMapping
@Component
public class Resource {


////////////////////////////////////////////////////////////////////////////////
/////////////this controller is to imitate the behaviour of upload asset resource in asset core service
    @PostMapping(value = "/uploadCore")
    @ResponseBody
    public String uploadCore(@RequestParam MultipartFile data,
                              @RequestParam String assetType,
                              @RequestParam String mimeType,
                              @RequestParam String extension,
                              @RequestParam(required = false) String assetKey) throws IOException {
        File f = new File("C:\\Users\\mahdi.amini\\servlet_"+ System.nanoTime());
        data.transferTo(f);
        return "::" + f.getAbsolutePath() + " Is uploaded using servlet based 'uploadCore' api. The extension was: " + extension;
    }
////////////////////////////////////////////////////////////////////////////////
}