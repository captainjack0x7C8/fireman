package org.fireman

import com.esotericsoftware.yamlbeans.YamlException
import com.esotericsoftware.yamlbeans.YamlReader

class ConfigLoader {

    @GrabResolver(name = 'ali', root = 'http://maven.aliyun.com/nexus/content/groups/public/')
    @Grab(group = "com.esotericsoftware.yamlbeans", module = "yamlbeans", version = "1.13")
    static Param load(String configPath) throws FileNotFoundException, YamlException {
        YamlReader reader = new YamlReader(new FileReader(configPath))
        return reader.read(Param.class)
    }
}
