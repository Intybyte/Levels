package config;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.config.ConfigEntryHolder;
import com.thexfactor117.levels.common.config.ConfigManager;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.nio.file.Path;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnumConfigTest {

    enum MockEnumConfig implements ConfigEntryHolder {
        A, B, C, D, E;

        @Override
        public ConfigEntry getEntry() {
            HashMap<String, String> map = new HashMap<>();
            map.put(this.name(), "true");
            return new ConfigEntry(map);
        }
    }

    private ConfigManager getCfg(Path tempDir) {
        Path tempFile = tempDir.resolve("levels.cfg");

        ConfigManager manager = new ConfigManager(tempFile.toFile());
        manager
                .process(MockEnumConfig.A)
                .process(MockEnumConfig.B)
                .process(MockEnumConfig.C)
                .process(MockEnumConfig.D)
                .process(MockEnumConfig.E)
                .initFile();

        return manager;
    }

    @ParameterizedTest
    @EnumSource(MockEnumConfig.class)
    void test_MockEnumConfigEntry(MockEnumConfig entry, @TempDir Path tempDir) {
        ConfigManager manager = getCfg(tempDir);
        assertTrue(manager.getBoolean(entry.name()));
    }
}
