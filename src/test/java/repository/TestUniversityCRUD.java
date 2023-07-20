package repository;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.AbstractDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.UniversityDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class TestUniversityCRUD {

    AbstractDAOImpl<University, UUID> universityDao = new UniversityDAOImpl();

    @Test
    void testCreate1() throws DAOException {
        University university = new University();
        university.setName("Беларусский национальный технический университет");

        universityDao.create(university);
    }

    @Test
    void testCreate() throws DAOException {
        University university = new University();
        university.setName("Беларусский государственный университет");

        Faculty faculty = new Faculty();
        faculty.setName("Факультет информационных технологий");
        faculty.setUniversity(university);

        Faculty faculty1 = new Faculty();
        faculty1.setName("Факультете педагогов");
        faculty1.setUniversity(university);
        university.setFaculties(Stream.of(faculty, faculty1).toList());

        universityDao.create(university);
    }

    @Test
    void testGetAll() throws DAOException {
        List<University> universities = universityDao.findAll();
        Assertions.assertFalse(universities.isEmpty());
        System.out.println(universities.stream().map(University::getName).toList());
    }
}
