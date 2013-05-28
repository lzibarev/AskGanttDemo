package ru.ask.primaview.gantt.demo.client.dummydata;
 
import com.gantt.client.config.GanttConfig.DependencyType;
 
public class Dependency {
    String id;
    String fromId;
    String toId;
    DependencyType type;
    String css;
 
     
    public Dependency(String id, String fromId, String toId, DependencyType type) {
        this(id, fromId, toId, type, "");
    }
 
    public Dependency(String id, String fromId, String toId,
            DependencyType type, String css) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.type = type;
        this.css = css;
    }
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getFromId() {
        return fromId;
    }
 
    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
 
    public String getToId() {
        return toId;
    }
 
    public void setToId(String toId) {
        this.toId = toId;
    }
 
    public DependencyType getType() {
        return type;
    }
 
    public void setType(DependencyType type) {
        this.type = type;
    }
 
    public String getCss() {
        return css;
    }
 
    public void setCss(String css) {
        this.css = css;
    }
 
}
