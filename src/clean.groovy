#!/usr/bin/env groovy
import org.fireman.HereICome

HereICome.containersWorkspace


def getDirectory
getDirectory = {
    file, handler ->
        file.listFiles().each { x ->
            x.isDirectory() ? getDirectory(x, handler) : handler.call(x)
        }
}

//clean Projects
def projectsPath = new File(HereICome.projectsWorkspace)
if (projectsPath != null) {
    getDirectory(projectsPath, {
        x ->
            if (x.getName() == "pid") {
                def pid = x.text.trim()
                "kill $pid".execute().waitFor()
                println("Terminate PID $pid")
                x.delete()
            }
    })
}

//clean Containers
def containersPath = new File(HereICome.containersWorkspace)
if (containersPath != null) {
    getDirectory(containersPath, {
        x ->
            if (x.getName().endsWith(".did")) {
                def fullName = x.getName()
                def name = fullName.substring(0, fullName.lastIndexOf("."))
                def did = x.text.trim()
                "docker stop $did".execute().waitFor()
                println("Stop constainer $name with id $did")
                x.delete()
            }
    })
}
