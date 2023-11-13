package ua.te.tk;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ua.te.tk.entity.Contact;
import ua.te.tk.entity.Name;

import java.util.List;

public class MainWithHibernate {

    private SessionFactory sessionFactory;

    public MainWithHibernate() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        try {
            sessionFactory =
                    new MetadataSources(registry)
                            .addAnnotatedClass(Contact.class)
                            .buildMetadata()
                            .buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we
            // had trouble building the SessionFactory so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void main(String[] args) {
        MainWithHibernate main = new MainWithHibernate();
        main.sessionFactory.inTransaction(session -> {
            Contact contact = new Contact(new Name("Petro", "Petrovych", "Petrenko"), "мобільний", "+38 097 123 45 67", true);
            System.out.println("inTransaction Contact before persist:" + contact);
            session.persist(contact);
            System.out.println("Contact after persist:" + contact);
        });

        main.sessionFactory.inSession(session -> {
            Contact contact = new Contact(new Name("Andrii", "Petrovych", "Petrenko"), "мобільний", "+38 097 123 45 67", true);
            System.out.println("inSession Contact before persist:" + contact);
            session.persist(contact);
            System.out.println("Contact after persist:" + contact);
        });

        Contact contact = main.sessionFactory.fromSession(session -> {
            return session.find(Contact.class, 1);
        });
        System.out.println(contact);
        contact.setPhone("111111111");
        try (Session session = main.sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.merge(contact);
            session.getTransaction().commit();
        }

        List<Contact> contacts = main.sessionFactory.fromSession(session -> {
            return session.createQuery("Select contact from Contact contact", Contact.class).getResultList();
        });

        System.out.println(contacts);
    }
}
