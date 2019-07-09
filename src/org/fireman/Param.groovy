package org.fireman

class Param {
    private List<GitProject> gitProjects
    private List<Image> images

    List<GitProject> getGitProjects() {
        return gitProjects
    }

    void setGitProjects(List<GitProject> gitProjects) {
        this.gitProjects = gitProjects
    }

    List<Image> getImages() {
        return images
    }

    void setImages(List<Image> images) {
        this.images = images
    }
}
