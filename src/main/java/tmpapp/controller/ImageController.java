package tmpapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tmpapp.model.Comment;
import tmpapp.model.Image;
import tmpapp.model.Tag;
import tmpapp.model.User;
import tmpapp.services.CommentService;
import tmpapp.services.ImageService;
import tmpapp.services.TagService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @RequestMapping("images")
    public String getUserImages(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "images";
    }

    @RequestMapping("/images/{imageId}/{title}")
    public String showImage(@PathVariable("imageId") Integer imageId, Model model) {
        Comment commentData=new Comment();
        model.addAttribute("Comment",commentData);
        Image image = imageService.getImage(imageId);
        model.addAttribute("image", image);
        model.addAttribute("tags", image.getTags());
        List<Comment> comment = commentService.getCommentsByImageId(imageId);
        model.addAttribute("comments", comment);
        return "images/image";
    }

    @RequestMapping(value = "/image/{imageId}/{title}/comments", method = RequestMethod.POST)
    public String commentUser(@PathVariable(name = "imageId") Integer imageId, HttpSession session,Comment commentData) {
        User user = (User) session.getAttribute("loggeduser");
        commentData.setUser(user);
        commentData.setCreatedDate(new Date());
        Image image = imageService.getImage(imageId);
//        Comment commentDta = new Comment();
        System.out.println(commentData.getText());
        System.out.println("commentData");
        commentData.setImage(image);
//        commentData.setText();
        commentService.addComment(commentData);
        return "redirect:/images/" + imageId + "/" + image.getTitle();
    }


    @RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)
    public String deleteImageSubmit(@RequestParam(name = "imageId") Integer imageId, Model model, HttpSession session) {
        Image image = imageService.getImage(imageId);
        User user = (User) session.getAttribute("loggeduser");
        if (image.getUser().getId() != user.getId()) {
            String error = "Only the owner of the image can delete the image";

            model.addAttribute("image", image);
            model.addAttribute("tags", image.getTags());
            model.addAttribute("deleteError", error);
            return "images/image";
        } else {
            imageService.deleteImage(imageId);
            return "redirect:/images";
        }
    }

    @RequestMapping(value = "/editImage")
    public String editImage(@RequestParam("imageId") Integer imageId, Model model) {
        Image image = imageService.getImage(imageId);

        String tags = convertTagsToString(image.getTags());
        model.addAttribute("image", image);
        model.addAttribute("tags", tags);
        return "images/edit";
    }

    //The method also receives tags parameter which is a string of all the tags separated by a comma using the annotation @RequestParam
    //The method converts the string to a list of all the tags using findOrCreateTags() method and sets the tags attribute of an image as a list of all the tags
    @RequestMapping(value = "/editImage", method = RequestMethod.PUT)
    public String editImageSubmit(@RequestParam("file") MultipartFile file, @RequestParam("imageId") Integer imageId, @RequestParam("tags") String tags, Image updatedImage, HttpSession session, Model model) throws IOException {
        Image image = imageService.getImage(imageId);
        User user = (User) session.getAttribute("loggeduser");
        if (user.getId() == image.getUser().getId()) {
            String updatedImageData = convertUploadedFileToBase64(file);
            List<Tag> imageTags = findOrCreateTags(tags);

            if (updatedImageData.isEmpty())
                updatedImage.setImageFile(image.getImageFile());
            else {
                updatedImage.setImageFile(updatedImageData);
            }

            updatedImage.setId(imageId);
            updatedImage.setUser(user);
            updatedImage.setTags(imageTags);
            updatedImage.setDate(new Date());

            imageService.updateImage(updatedImage);
            return "redirect:/images/" + updatedImage.getId() + "/" + updatedImage.getTitle();
        } else {
            String error = "Only the owner of the image can edit the image";
            model.addAttribute("image", image);
            model.addAttribute("editError", error);
            return "images/image";
        }
    }

    //This controller method is called when the request pattern is of type 'images/upload'
    //The method returns 'images/upload.html' file
    @RequestMapping("/images/upload")
    public String newImage() {
        return "images/upload";
    }

    //Get the 'tags' request parameter using @RequestParam annotation which is just a string of all the tags
    //Store all the tags in the database and make a list of all the tags using the findOrCreateTags() method
    //set the tags attribute of the image as a list of all the tags returned by the findOrCreateTags() method
    @RequestMapping(value = "/images/upload", method = RequestMethod.POST)
    public String createImage(@RequestParam("file") MultipartFile file, @RequestParam("tags") String tags, Image newImage, HttpSession session) throws IOException {

        User user = (User) session.getAttribute("loggeduser");
        newImage.setUser(user);
        String uploadedImageData = convertUploadedFileToBase64(file);
        newImage.setImageFile(uploadedImageData);

        List<Tag> imageTags = findOrCreateTags(tags);
        newImage.setTags(imageTags);
        newImage.setDate(new Date());
        imageService.uploadImage(newImage);
        return "redirect:/images";
    }

    //This method converts the image to Base64 format
    private String convertUploadedFileToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    //findOrCreateTags() method has been implemented, which returns the list of tags after converting the ‘tags’ string to a list of all the tags and also stores the tags in the database if they do not exist in the database. Observe the method and complete the code where required for this method.
    //Try to get the tag from the database using getTagByName() method. If tag is returned, you need not to store that tag in the database, and if null is returned, you need to first store that tag in the database and then the tag is added to a list
    //After adding all tags to a list, the list is returned
    private List<Tag> findOrCreateTags(String tagNames) {
        StringTokenizer st = new StringTokenizer(tagNames, ",");
        List<Tag> tags = new ArrayList<Tag>();

        while (st.hasMoreTokens()) {
            String tagName = st.nextToken().trim();
            Tag tag = tagService.getTagByName(tagName);

            if (tag == null) {
                Tag newTag = new Tag(tagName);
                tag = tagService.createTag(newTag);
            }
            tags.add(tag);
        }
        return tags;
    }

    private String convertTagsToString(List<Tag> tags) {
        StringBuilder tagString = new StringBuilder();

        for (int i = 0; i <= tags.size() - 2; i++) {
            tagString.append(tags.get(i).getName()).append(",");
        }

        Tag lastTag = tags.get(tags.size() - 1);
        tagString.append(lastTag.getName());

        return tagString.toString();
    }
}
