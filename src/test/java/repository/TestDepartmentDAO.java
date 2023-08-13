package repository;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.UniversityDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.DepartmentDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.FacultyDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.UniversityDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import com.sun.istack.Pool;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestDepartmentDAO {
    DepartmentDAOImpl departmentDAO = new DepartmentDAOImpl();
    static UniversityDAO universityDAO = new UniversityDAOImpl();

    //    @BeforeAll
    @Test
    void init() throws DAOException, ConnectionPoolException {
        Session session = PoolConnection.Builder.build().getSession();
        Transaction trans = session.beginTransaction();
        try {
            University u = new University();
            u.setName("Беларусский национальный технический университет");

            Faculty f1 = new Faculty();
            f1.setName("Факультет информационных технологий и робототехники");
            f1.setUniversity(u);
            u.getFaculties().add(f1);

            Department d = new Department();
            d.setName("Кафедра математики");
            d.setFaculty(f1);
            f1.getDepartments().add(d);

            d = new Department();
            d.setName("Кафедра информационных технологий");
            d.setFaculty(f1);
            f1.getDepartments().add(d);

            // --------------------------------------------------------------------------
            f1 = new Faculty();
            f1.setName("Факультет горного дело и инженерной экологии");
            f1.setUniversity(u);
            u.getFaculties().add(f1);

            d = new Department();
            d.setName("Горные машины");
            d.setFaculty(f1);
            f1.getDepartments().add(d);

            d = new Department();
            d.setName("Горные работы");
            d.setFaculty(f1);
            f1.getDepartments().add(d);

            d = new Department();
            d.setName("Инженерная экология");
            d.setFaculty(f1);
            f1.getDepartments().add(d);
            // -----------------------------------------------------------------------------

            universityDAO.create(u);
            trans.commit();
        } catch (PersistenceException e) {
            trans.rollback();
            throw new DAOException(e);
        }
    }

    @Test
    void testFindAny() throws DAOException, ConnectionPoolException {
        Session session = PoolConnection.Builder.build().getSession();
        Transaction trans = session.beginTransaction();

        Department department = departmentDAO.findAny().orElseThrow();
        Assertions.assertSame(department, departmentDAO.findAny().orElseThrow());
        trans.commit();
    }


    /**
     * How you will be much seen, when I invoke same method more time
     * Hibernate only execute one hql-query, although I invoke more time same method
     */
    @Test
    void testFindById() throws DAOException, ConnectionPoolException {
        Session session = PoolConnection.Builder.build().getSession();
        Transaction trans = session.beginTransaction();
        Department department = departmentDAO.findAny().orElseThrow();

        Department depClone = departmentDAO.findById(department.getId()).get();
        Assertions.assertSame(department, depClone);

        Department depClone1 = departmentDAO.findById(department.getId()).get();
        Assertions.assertSame(depClone, depClone1);

        trans.commit();

    }

    @Test
    void testGetAll() throws DAOException, ConnectionPoolException {
        Session session = PoolConnection.Builder.build().getSession();
        Transaction trans = session.beginTransaction();

        List<Department> departments = departmentDAO.findAll();
        Map<Faculty, List<Department>> map = departments.stream().collect(Collectors.groupingBy(Department::getFaculty));
        map.entrySet().forEach(entity -> {
            System.out.println("------------------- " + entity.getKey().getName());
            entity.getValue().forEach(dep -> {
                System.out.println("------ " + dep.getName());
            });
        });

        trans.commit();
    }

    @Test
    void testFindByFacultyName() throws ConnectionPoolException, DAOException {
        Session session = PoolConnection.Builder.build().getSession();
        Transaction trans = session.beginTransaction();

        List<Department> departments = departmentDAO.findByFacultyName("Факультет информационных технологий и робототехники");

        List<Department> departmentsClone = departmentDAO.findByFacultyName("Факультет информационных технологий и робототехники");

        // Как можно увидить, возвращаются одни и те же объекты Java, хоть было 2 запроса.
        // Таким образом Hibernarte хеширует объекты, чтобы не делать лишние запросы или не создвать
        // лишние java-объекты. (хотя в этом случаи, Hibernate сделал 2 запроса, однако он
        // посмотрел в hash и вернул те же объеты, что при первом разе были ициализированы)!!!
        Assertions.assertArrayEquals(departments.toArray(), departmentsClone.toArray());

        trans.commit();
    }

    FacultyDAOImpl facultyDAO = new FacultyDAOImpl();

    @Test
    void testFindByFacultyId() throws ConnectionPoolException, DAOException {
        Session session = PoolConnection.Builder.build().getSession();
        Transaction trans = session.beginTransaction();
        // так как мы делаем запрос для faculty, то даже этот объект жешируется в hibernate-hash-e
        Faculty faculty = facultyDAO.findAny().orElseThrow();

        // этот же объект (сслыку) facuty можно найти и здесь в departments.
        // ВыВОД Hiberante hash-ырует объекты в приделах session
        List<Department> departments = departmentDAO.findByFacultyId(faculty.getId());

        Assertions.assertArrayEquals(departments.toArray(), departmentDAO.findByFacultyId(faculty.getId()).toArray());
        trans.commit();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Горные",
            "работы",
            "пидо"

    })
    void testFindByLikeNameAndFacultyId(String departmentNamePattern) throws DAOException, ConnectionPoolException {
        FacultyDAOImpl facultyDAO = new FacultyDAOImpl();
        Faculty faculty = facultyDAO.findByName("Факультет горного дело и инженерной экологии").orElseThrow();

        List<Department> departments = departmentDAO.findByLikeNameAndFacultyId(departmentNamePattern, faculty.getId());
        System.out.println("---------------RESULT---------------------");
        System.out.println(departments.stream().map(Department::getName).collect(Collectors.joining(", ")));

    }
}
