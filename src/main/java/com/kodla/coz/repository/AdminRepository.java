package com.kodla.coz.repository;

import com.kodla.coz.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@Transactional
public class AdminRepository {
    @Autowired
    public AdminRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    private final EntityManager entityManager;

    /**
     * SetLanguageOfTask metodu task oluşturulduktan sonra language_id ataması yapmak için kullanılan fonksiyondur
     * @param taskId
     * @param languageId
     */
    public void setLanguageOfTask(Integer taskId, Integer languageId){
        entityManager.createNativeQuery("UPDATE tasks SET language_id = ? WHERE id = ?").setParameter(1, languageId).setParameter(2, taskId).executeUpdate();
    }
    public Language findLanguageById(Integer id){
        return entityManager.find(Language.class, id);
    }
}
