package tmpapp.repository;


import org.springframework.stereotype.Repository;
import tmpapp.model.User;

import javax.persistence.*;

@Repository
public class UserRepository {
    @PersistenceUnit(unitName = "imageHoster")
    private EntityManagerFactory emf;

    public User checkUser(String username,String password){
        try{
            EntityManager em=emf.createEntityManager();
            TypedQuery<User> typedQuery=em.createQuery("select u from User u WHERE u.username = :username AND u.password = :password",User.class);
            typedQuery.setParameter("username",username);
            typedQuery.setParameter("password",password);
            return typedQuery.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
    public void registerUser(User newUser) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            //persist() method changes the state of the model object from transient state to persistence state
            em.persist(newUser);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
