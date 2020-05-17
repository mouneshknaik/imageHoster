package tmpapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tmpapp.model.Image;
import tmpapp.services.ImageService;

import java.util.List;

@Controller
public class ImageController {
    @Autowired
    private ImageService imageService;

    @RequestMapping("images")
    public String getUserImages(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "images";
    }
    @RequestMapping("/images/{title}")
    public String showImage(@PathVariable("title") String title, Model model) {
        Image image = imageService.getImageByTitle(title);
        model.addAttribute("image", image);
//        model.addAttribute("tags",image.getTags());
        return "images/image";
    }

    @RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)
    public String deleteImageSubmit(@RequestParam(name = "imageId") Integer imageId) {
        imageService.deleteImage(imageId);
        return "redirect:/images";
    }

    @RequestMapping(value = "/editImage")
    public String editImage(@RequestParam("imageId") Integer imageId, Model model) {
        Image image = imageService.getImage(imageId);

//        String tags = convertTagsToString(image.getTags());
        model.addAttribute("image", image);
//        model.addAttribute("tags",tags);
        return "images/edit";
    }

    //This controller method is called when the request pattern is of type 'images/upload'
    //The method returns 'images/upload.html' file
    @RequestMapping("/images/upload")
    public String newImage() {
        return "images/upload";
    }

}
