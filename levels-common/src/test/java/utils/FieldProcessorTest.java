package utils;

import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.SwordAttribute;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.utils.FieldProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void test_getFields_onAnyAttributes() {
        List<AttributeBase> variables = FieldProcessor.getFields(AttributeBase.class, AnyAttributes.class);

        for (AttributeBase attr : variables) {
            System.out.println(attr.getBaseName());
        }

        assertEquals(1, variables.stream().filter( it ->
                it.getBaseName().equals("Fire")
        ).count());
    }

    @Test
    void test_getFields_multiple() {
        List<AttributeBase> list = FieldProcessor.getFields(
                AttributeBase.class,
                SwordAttribute.class,
                ArmorAttribute.class,
                BowAttribute.class,
                AnyAttributes.class
        );

        for (AttributeBase attr : list) {
            System.out.println("Found attribute: " + attr.getBaseName() + " -> " +
                    Arrays.toString(attr.getAllowedTypes()));
        }

        //assertTrue(list.contains(AnyAttributes.DURABLE));
    }
}
