package leveling;

import com.thexfactor117.levels.common.config.ConfigEntryHolder;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.utils.FieldProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemTypeTest {

    @Test
    void test_raw() {
        ItemType.init();
        assertEquals(1,
                ItemType.SWORD.attributesRaw().stream()
                        .filter(it -> it.getBaseName().equals("Fire"))
                        .count()
        );
    }

    @Test
    void test_enabled(@TempDir Path tempDir) {
        Configs.init(tempDir.toFile());
        List<AttributeBase> list = FieldProcessor.getFields(AttributeBase.class, AnyAttributes.class);

        for (AttributeBase base : list) {
            assertTrue(base.isEnabled());
        }
    }

    @Test
    void test_any_presence(@TempDir Path tempDir) {
        Configs.init(tempDir.toFile());
        ItemType.init();

        System.out.println(AnyAttributes.FIRE.isEnabled());
        for (ItemType type : AnyAttributes.FIRE.getAllowedTypes()) {
            System.out.println(type);
        }

        assertTrue(ItemType.SWORD.attributesRaw().contains(AnyAttributes.FIRE));
    }

    @Test
    void test_same_but_different(@TempDir Path tempDir) {
        Configs.init(tempDir.toFile());
        List<AttributeBase> listAttribute = FieldProcessor.getFields(AttributeBase.class, AnyAttributes.class);
        List<ConfigEntryHolder> listConfig = FieldProcessor.getFields(ConfigEntryHolder.class, AnyAttributes.class);

        assertEquals(listAttribute.size(), listConfig.size());
    }
}
