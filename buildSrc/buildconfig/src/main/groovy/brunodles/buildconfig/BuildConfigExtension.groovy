package brunodles.buildconfig

class BuildConfigExtension {
    HashMap<String, String> properties = new HashMap<>()

    def void add(String key, String value) {
        properties.put(key, value)
    }
}