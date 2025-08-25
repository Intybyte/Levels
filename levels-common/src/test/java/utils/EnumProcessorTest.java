package utils;

import com.thexfactor117.levels.common.utils.EnumProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

public class EnumProcessorTest {
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
        MockEnum[] variable = EnumProcessor.getValues(MockEnum.class);

        for (MockEnum entry : variable) {
            System.out.println(entry);
        }
    }

    @Test
    void test_getValue_withInterface() {
        ObtainName[] variable = EnumProcessor.getValues(MockEnum.class);

        for (ObtainName entry : variable) {
            System.out.println(entry.getName());
        }
    }
}
