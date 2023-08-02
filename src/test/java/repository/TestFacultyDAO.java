package repository;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.FacultyDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.UniversityDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;


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
}
