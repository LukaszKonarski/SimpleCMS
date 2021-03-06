package pl.coderslab.web;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.coderslab.entity.Article;


@Component
@Transactional
public class ArticleDao {
    @PersistenceContext
    EntityManager entityManager;

    public void save(Article entity) {
        entity.setCreated(LocalDate.now());
        if (entity.getId() > 0) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    public Article findById(long id) {

        return entityManager.find(Article.class, id);
    }

    public void update(Article entity) {
        entity.setUpdated(LocalDate.now());
        entityManager.merge(entity);
    }

    public void delete(long id) {
        Article entity = entityManager.find(Article.class, id);
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    public List<Article> getLatest() {
        Query query = this.entityManager.createQuery("SELECT a FROM Article a ORDER BY a.created DESC");
        return query.setMaxResults(5).getResultList();
    }

    public List<Article> getAll() {
        Query query = this.entityManager.createQuery("SELECT a FROM Article a");
        return query.getResultList();
    }

    public List<Article> showByCategory(long id) {
        Query query = this.entityManager.createQuery("SELECT a FROM Article a JOIN a.categories category WHERE category.id = :id");
        query.setParameter("id", id);
        return query.getResultList();


    }
}
