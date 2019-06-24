package cn.edu.sicau.pfdistribution.entity;

import java.util.List;

public class ODPath {
    private String o;
    private String d;
    private List<DirectedPath> namePaths;
    private List<DirectedPath> idPaths;
    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getD() {
        return d;
    }

    public List<DirectedPath> getNamePaths() {
        return namePaths;
    }

    public void setNamePaths(List<DirectedPath> namePaths) {
        this.namePaths = namePaths;
    }

    public List<DirectedPath> getIdPaths() {
        return idPaths;
    }

    public void setIdPaths(List<DirectedPath> idPaths) {
        this.idPaths = idPaths;
    }

    public void setD(String d) {
        this.d = d;
    }
}
