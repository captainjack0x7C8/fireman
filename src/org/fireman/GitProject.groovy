package org.fireman

class GitProject {
    private String name
    private String url
    private String branch
    private List<String> globalJvmParams
    private List<Module> modules

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getUrl() {
        return url
    }

    void setUrl(String url) {
        this.url = url
    }

    String getBranch() {
        return branch
    }

    void setBranch(String branch) {
        this.branch = branch
    }

    List<String> getGlobalJvmParams() {
        return globalJvmParams
    }

    void setGlobalJvmParams(List<String> globalJvmParams) {
        this.globalJvmParams = globalJvmParams
    }

    List<Module> getModules() {
        return modules
    }

    void setModules(List<Module> modules) {
        this.modules = modules
    }
}
