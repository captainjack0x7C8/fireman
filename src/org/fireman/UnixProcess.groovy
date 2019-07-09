package org.fireman

class UnixProcess {
    Process process
    def pid

    UnixProcess(process, pid) {
        this.process = process
        this.pid = pid
    }
}
