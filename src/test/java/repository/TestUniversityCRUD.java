package repository;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.AbstractDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.UniversityDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class TestUniversityCRUD {

    UniversityDAOImpl universityDao = new UniversityDAOImpl();

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

    @Test
    void testFindAny() throws DAOException {
        University university = universityDao.findAny().get();
        System.out.println(university);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Беларусский национальный технический университет",
    })
    void testFindByName(String name) throws DAOException {
        University university = universityDao.findByName(name).orElse(null);
        // if result university = null, then full DB necessary records
        Assertions.assertNotNull(university);
        System.out.println(university.getName());

        Assertions.assertNull(
                universityDao.findByName("Бераусский национальный технический университет")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Беларусский национальный технический университет",
            "Беларусский",
            "техни",
            "униве"
    })
    void testFindLikeByName(String name) throws DAOException {
        List<University> universityList = universityDao.findLikeByName(name);
        System.out.println(universityList);
        Assumptions.assumeFalse(universityList.isEmpty());
    }

    @Test
    void testCount() throws DAOException{
        long res = universityDao.count();
        Assertions.assertTrue(res > 0);
    }

    @Test
    void testCountLikeByName() throws DAOException{
        long res = universityDao.countLikeByName("Бел1");
        System.out.println(res);;
    }
}
