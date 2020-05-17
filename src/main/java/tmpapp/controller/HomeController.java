package tmpapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tmpapp.model.Image;
import tmpapp.services.ImageService;

import java.sql.SQLException;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ImageService imageService;
    @RequestMapping("/")
    public String getAllImage(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "index";
    }
}
