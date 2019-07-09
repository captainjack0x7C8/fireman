package org.fireman

class Module {
    private String name
    private List<String> jvmParams

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    List<String> getJvmParams() {
        return jvmParams
    }

    void setJvmParams(List<String> jvmParams) {
        this.jvmParams = jvmParams
    }
}
