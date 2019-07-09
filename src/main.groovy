#!/usr/bin/env groovy
import org.fireman.HereICome

if (args == null) {
    println HereICome.help
    return
}
//TODO check param
def argList = Arrays.asList(args)
if (argList.contains("-c")) {
    def flagIndex = argList.indexOf("-c")
    HereICome.configPath = argList.get(flagIndex + 1)
    println "Using custom configuration => $HereICome.configPath"
}

//初始化工作空间、配置文件
HereICome.init()

if (argList.contains("-s")) {
    def flagIndex = argList.indexOf("-s")
    def scriptPath = argList.get(flagIndex + 1)
    println "Executing custom shell script => $scriptPath"
    "chmod +x $scriptPath".execute().waitFor()
    ["/bin/bash", "-c", scriptPath].execute().waitFor()
    println "Execute custom shell script Complete"
}

if (argList.contains("-a")) {
    //镜像
    HereICome.images()
    HereICome.imageLog()
    //工程
    HereICome.projects()
    HereICome.projectLog()
} else if (argList.contains("-i")) {
    HereICome.images()
    HereICome.imageLog()
} else if (argList.contains("-p")) {
    HereICome.projects()
    HereICome.projectLog()
} else {
    println HereICome.help
}
