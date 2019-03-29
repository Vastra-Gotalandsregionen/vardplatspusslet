package se.vgregion.vardplatspusslet.intsvc.controller;

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

    @RequestMapping(value = "/compiledTimestamp", method = RequestMethod.GET)
    @ResponseBody
    public String compiledTimestamp() throws URISyntaxException {
        URL resource = getClass().getResource(getClass().getSimpleName() + ".class");

        if (resource == null) {
            throw new IllegalStateException("Failed to find class file for class: " +
                    getClass().getName());
        }

        if (resource.getProtocol().equals("file")) {

            return new File(resource.toURI()).lastModified() + "";

        } else if (resource.getProtocol().equals("jar")) {

            String path = resource.getPath();
            return new File(path.substring(5, path.indexOf("!"))).lastModified() + "";

        } else {

            throw new IllegalArgumentException("Unhandled url protocol: " +
                    resource.getProtocol() + " for class: " +
                    getClass().getName() + " resource: " + resource.toString());
        }
    }
}
