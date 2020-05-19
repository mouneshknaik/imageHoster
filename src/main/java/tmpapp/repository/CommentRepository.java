package tmpapp.repository;

import org.springframework.stereotype.Repository;
import tmpapp.model.Comment;

import javax.persistence.*;
import java.util.List;

@Repository
public class CommentRepository {
    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;

    public void commentUser(Comment comment) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        System.out.println("commnet object");
        System.out.println(comment.getImage_id().toString());

        System.out.println(comment.getText());
        try {
            transaction.begin();
            em.persist(comment);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    public List<Comment> getAllComments(Integer imageId) {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Comment> typedQuery = em.createQuery("SELECT t from Comment t ", Comment.class);
//        .setParameter("imageId", imageId);
        List<Comment> resultList = typedQuery.getResultList();
        return resultList;

    }

}
