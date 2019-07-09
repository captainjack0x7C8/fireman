package org.fireman

class ModuleVo {
    def projectName
    def moduleName

    ModuleVo(projectName, moduleName) {
        this.projectName = projectName
        this.moduleName = moduleName
    }
}
