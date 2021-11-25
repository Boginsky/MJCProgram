package by.boginsky.util;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringUtilsTest {

    private Boolean actual;

    @BeforeAll
    public void setUp(){
        actual = new StringUtils().isPositiveNumber("-55555");
    }

    @Test
    public void isPositiveNumberTest(){
        Assertions.assertTrue(actual);
    }

    @AfterAll
    public void tierDown(){
        actual = null;
    }

}
