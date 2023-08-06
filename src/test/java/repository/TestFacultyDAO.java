package repository;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.FacultyDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.UniversityDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class TestFacultyDAO {
    FacultyDAOImpl facultyDAO = new FacultyDAOImpl();


    @Test
    void testFindByUniversityId() throws DAOException {
        UniversityDAOImpl unaUniversityDAO = new UniversityDAOImpl();
        UUID universityID = unaUniversityDAO.findByName("Беларусский государственный университет").orElseThrow().getId();

        List<Faculty> listFaculty = facultyDAO.findByUniversityId(universityID);
        Assumptions.assumeFalse(listFaculty.isEmpty());
    }

    @Test
    void testFindByUniversityName() throws DAOException {
        List<Faculty> listFaculty = facultyDAO.findByUniversityName("Беларусский государственный университет");
        System.out.println(listFaculty.stream().map(Faculty::getName).toList());
        Assumptions.assumeFalse(listFaculty.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Факультет инфо",
            "технол",
            "Факультет"
    })
    void testFindByLikeNameAndUniversityId(String facultyNamePattern) throws DAOException {
        UniversityDAOImpl universityDAO = new UniversityDAOImpl();
        UUID universityId = universityDAO.findByName("Беларусский национальный технический университет").orElseThrow().getId();

        List<Faculty> listFaculty = facultyDAO.findByLikeNameAndUniversityId(facultyNamePattern, universityId);
        System.out.println("---------------RESULT---------------------");
        System.out.println(listFaculty.stream().map(Faculty::getName).collect(Collectors.joining(", ")));
    }
}
