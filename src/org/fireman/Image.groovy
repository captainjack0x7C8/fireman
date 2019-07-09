package org.fireman

class Image {
    private String shortName
    private String name
    private String tag
    private List<DockerParam> dockerParams

    String getShortName() {
        return shortName
    }

    void setShortName(String shortName) {
        this.shortName = shortName
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getTag() {
        return tag
    }

    void setTag(String tag) {
        this.tag = tag
    }

    List<DockerParam> getDockerParams() {
        return dockerParams
    }

    void setDockerParams(List<DockerParam> dockerParams) {
        this.dockerParams = dockerParams
    }
}
