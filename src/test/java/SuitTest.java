import org.junit.platform.suite.api.*;


@Suite
@SelectPackages(value = "") // root folder  test/java/
@IncludePackages(value = {"subtest1.subsubtest1"}) // limit scanning within given package
@ExcludeClassNamePatterns(value = {"Test1CreateDB", "TestVariableSourceMyAnnotation"})
public class SuitTest {

}
