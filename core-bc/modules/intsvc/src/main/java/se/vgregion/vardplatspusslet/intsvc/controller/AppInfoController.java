package se.vgregion.vardplatspusslet.intsvc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

@Controller
@RequestMapping("/appInfo")
public class AppInfoController {

    @Value("${header.message}")
    private String headerMessage;

    @RequestMapping(value = "/compiledTimestamp", method = RequestMethod.GET)
    @ResponseBody
    public String compiledTimestamp() {
        URL resource = getClass().getResource(getClass().getSimpleName() + ".class");

        if (resource == null) {
            throw new IllegalStateException("Failed to find class file for class: " +
                    getClass().getName());
        }

        if (resource.getProtocol().equals("file")) {

            try {
                return new File(resource.toURI()).lastModified() + "";
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

        } else if (resource.getProtocol().equals("jar")) {

            String path = resource.getPath();
            return new File(path.substring(5, path.indexOf("!"))).lastModified() + "";

        } else {

            throw new IllegalArgumentException("Unhandled url protocol: " +
                    resource.getProtocol() + " for class: " +
                    getClass().getName() + " resource: " + resource.toString());
        }
    }

    @RequestMapping(value = "/headerMessage", method = RequestMethod.GET)
    @ResponseBody
    public String headerMessage() {
        return headerMessage;
    }
}
