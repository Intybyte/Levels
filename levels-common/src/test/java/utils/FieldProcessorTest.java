package utils;

import com.thexfactor117.levels.common.utils.FieldProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldProcessorTest {
    public interface ObtainName {
        String getName();
    }

    @AllArgsConstructor
    @Getter
    public enum MockEnum implements ObtainName {
        A("First"), B("Second"), C("Third"), D("Fourth");

        private final String name;
    }

    @Test
    void test_getValue() {
        List<MockEnum> variable = FieldProcessor.getFields(MockEnum.class, MockEnum.class);

        for (MockEnum entry : variable) {
            System.out.println(entry);
        }

        assertEquals(variable.size(), MockEnum.values().length);
    }

    @Test
    void test_getValue_withInterface() {
        List<ObtainName> variable = FieldProcessor.getFields(ObtainName.class, MockEnum.class);

        for (ObtainName entry : variable) {
            System.out.println(entry.getName());
        }

        assertEquals(variable.size(), MockEnum.values().length);
    }
}
