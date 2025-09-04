package config;

import com.thexfactor117.levels.common.config.ConfigManager;
import com.thexfactor117.levels.common.config.MainConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MainConfigTest {

    private ConfigManager getCfg(Path tempDir) {
        Path tempFile = tempDir.resolve("levels.cfg");

        ConfigManager manager = new ConfigManager(tempFile.toFile());
        manager.process(new MainConfig()).initFile();

        return manager;
    }

    @Test
    void test_MainConfigDefault(@TempDir Path tempDir) throws IOException {
        ConfigManager manager = getCfg(tempDir);

        assertEquals(10, manager.getInt("maxLevel"));
        assertEquals(2.1, manager.getDouble("expExponent"));
        assertEquals(20, manager.getDouble("expMultiplier"));

        List<String> list = new ArrayList<>();
        list.add("modid:item");
        list.add("modid:item2");

        assertEquals(list, manager.getStringList("itemBlackList"));
        assertFalse(manager.getBoolean("unlimitedDurability"));
    }

    @Test
    void test_EditMainConfigDefault(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("levels.cfg");

        Properties properties = new Properties();
        HashMap<String, String> dummy = new HashMap<>();
        dummy.put("maxLevel", "20");
        dummy.put("whatever", "hi");

        properties.putAll(dummy);
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            properties.store(writer, "Saved Config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ConfigManager manager = getCfg(tempDir);

        // shouldn't been edited/deleted
        assertEquals(20, manager.getInt("maxLevel"));
        assertEquals("hi", manager.get("whatever"));
    }
}
