package org.fireman

class HereICome {

    //TODO
    static def config
    static def configPath
    static def workspace = "/opt/fireman"
    static def projectsWorkspace = "$workspace/projects"
    static def containersWorkspace = "$workspace/containers"
    static def consoleOutDir = "/mnt/console-output"

    static def help = '''Usage:
    -a Start up all
    -i Start up images
    -p Start up projects
    -c Custom configuration file. Default => ./config.yaml
    -s Custom Shell Script. Execute before main job.
    -h Show current message'''
    static Map<ModuleVo, UnixProcess> processMap = [:]
    static Map<String, String> containerMap = [:]

    static def init(path) {
        if (path == null) {
            configPath = this.classLoader.getResource("config.yaml").getPath()
        } else {
            configPath = path
        }
        config = ConfigLoader.load(configPath)

        def dir = new File(workspace)
        println "Init workspace => $workspace"
        if (!dir.exists()) {
            dir.mkdir()
        }
        def containerDir = new File("$containersWorkspace")
        if (!containerDir.exists()) {
            containerDir.mkdir()
        }
        def projectDir = new File("$projectsWorkspace")
        if (!projectDir.exists()) {
            projectDir.mkdir()
        }
        def logDir = new File("$consoleOutDir")
        if (!logDir.exists()) {
            logDir.mkdir()
        }
        println "Init workspace successfully"
    }

    static def projects() {
        def projects = config.gitProjects
        //clone & package
        for (p in projects) {
            if (!new File("$consoleOutDir/$p.name").exists()) {
                new File("$consoleOutDir/$p.name").mkdir()
            }
            def projectPath = "$projectsWorkspace/$p.name"
            if (new File(projectPath).exists()) {
                println "Pulling..."
                def pullProc = "git pull".execute()
                print(pullProc)
            } else {
                def cloneCommand = "git clone $p.url"
                println "Cloning..."
                def cloneProc = cloneCommand.execute(null, new File(projectsWorkspace))
                print(cloneProc)
            }

            def checkoutCommand = "git checkout -B $p.branch origin/$p.branch"
            def packageCommand = "mvn -Dmaven.test.skip=true clean package"

            println "Checkout..."
            def checkoutProc = checkoutCommand.execute(null, new File(projectPath))
            print(checkoutProc)

            println "Packaging..."
            def packageProc = packageCommand.execute(null, new File(projectPath))
            print(packageProc)

            //start up
            for (m in p.modules) {
                def modulePath = "$projectPath/$m.name"
                def runCommand = "setsid java -jar "
                def pidFile = new File("$modulePath/pid")
                if (pidFile.exists()) {
                    def pid = pidFile.text.trim()
                    def killProc = "kill $pid".execute()
                    print killProc
                    pidFile.delete()
                }
                for (j in p.globalJvmParams) {
                    runCommand += "-D$j "
                }
                for (j in m.jvmParams) {
                    runCommand += "-D$j "
                }
                runCommand += "$modulePath/target/app.jar >> $consoleOutDir/$p.name/$m.name & echo \$!"
                println "Executing command => $runCommand"

                def process = ["/bin/sh", "-c", runCommand].execute()
                def lines = process.in.readLines()
                def pid = lines.get(lines.size() - 1)
                new File("$modulePath/pid").write(pid)
                processMap.put(new ModuleVo(p.name, m.name), new UnixProcess(process, pid.trim()))
            }

        }
    }

    static def images() {
        def images = config.images
        for (i in images) {
            def containerIdFile = "$containersWorkspace/$i.shortName" + ".did"
            if (new File(containerIdFile).exists()) {
                def id = new FileReader(containerIdFile).readLine()
                println "Container of image $i.name is existed.\nContainer ID file path => $containerIdFile\nContainer ID => $id\nContinue..."
                containerMap.put("$i.name:$i.tag".toString(), new FileReader(containerIdFile).readLine())
                continue
            }
            def command = "docker run -itd --name $i.shortName" + System.currentTimeMillis()
            def image = i.name + ':' + i.tag
            for (p in i.dockerParams) {
                def arg
                if (p.flag != null) {
                    arg = "-$p.flag $p.value "
                } else {
                    arg = "--$p.value "
                }
                command += arg

            }
            command += "--cidfile=$containerIdFile "
            command += image
            def containerProc = command.execute()
            print(containerProc)
            println "Start container of image $i.name. Command => $command"

            def key = "$i.name:$i.tag".toString()
            containerMap.put(key, new FileReader(containerIdFile).readLine())
        }

    }

    static def print(def process) {
        def stream = process.inputStream
        def lines = stream.readLines("UTF-8")
        for (l in lines) {
            println l
        }
    }

    static def projectLog() {
        def generator = new TableGenerator(3, true)
        println "Processes info:\n"
        generator.appendRow().appendCol("Project Name").appendCol("Module Name").appendCol("PID")
        for (e in processMap) {
            generator.appendRow().appendCol(e.key.projectName).appendCol(e.key.moduleName).appendCol(e.value.pid)
        }
        println(generator.toString())
    }

    static def imageLog() {
        def generator = new TableGenerator(2, true)
        println "Containers info:\n"
        generator.appendRow().appendCol("Image Name").appendCol("Container ID")
        for (c in containerMap) {
            generator.appendRow().appendCol(c.key).appendCol(c.value)
        }
        println(generator.toString())
    }
}
