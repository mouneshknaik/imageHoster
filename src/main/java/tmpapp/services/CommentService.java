package tmpapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmpapp.model.Comment;
import tmpapp.model.Tag;
import tmpapp.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void addComment(Comment comment) {
        commentRepository.commentUser(comment);
    }
    public List<Comment> getCommentsByImageId(Integer imageId) {
        return commentRepository.getAllComments(imageId);
    }

}
