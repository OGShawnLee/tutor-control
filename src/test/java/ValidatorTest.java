import daima.business.rules.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidatorTest {
  @Test
  public void testMultiDomainEmail() {
    Assertions.assertDoesNotThrow(() -> {
      String email = Validator.getValidEmail("zS00010010@estudiantes.uv.mx");
      Assertions.assertEquals("zS00010010@estudiantes.uv.mx", email);
    });
  }
}
