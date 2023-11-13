package ua.te.tk;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import ua.te.tk.entity.Contact;
import ua.te.tk.entity.Name;

import java.util.List;
import java.util.function.Consumer;

public class MainWithJPA {
    EntityManagerFactory entityManagerFactory;

    public MainWithJPA() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("lab10");
    }

    public static void main(String[] args) {
        MainWithJPA main = new MainWithJPA();
        main.doWithJPA();
    }

    public void doWithJPA() {
        doWithJPA((entityManager -> {
            Contact contact = new Contact(new Name("Petro", "Petrovych", "Petrenko"), "мобільний", "+38 097 123 45 67", true);
            System.out.println("Contact before persist:" + contact);
            entityManager.persist(contact);
            System.out.println("Contact after persist:" + contact);

        }));

        doWithJPA(entityManager -> {
            Contact contact1 = entityManager.find(Contact.class, 1);
            System.out.println("Contact is taken from the database : " + contact1);

            contact1.setPhone("+38 096 123 11 11");
            System.out.println("Contact is changed: " + contact1);

        });

        doWithJPA(entityManager -> {
            Contact contactToDelete = entityManager.find(Contact.class, 1);
            if (contactToDelete != null) {
                entityManager.remove(contactToDelete);
            }
        });

        doWithJPA(entityManager -> {
            Contact contact = new Contact(new Name("Petro", "Petrovych", "Petrenko"), "мобільний", "+38 097 123 45 67", true);
            contact.setId(1);
//            entityManager.persist(contact);     //throw exception cause entity is detached!
        });

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            List<Contact> contacts = entityManager.createQuery("select contact from Contact contact", Contact.class).getResultList();
            System.out.println(contacts);
        }
    }

    public void doWithJPA(Consumer<EntityManager> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            work.accept(entityManager);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }

    }


}