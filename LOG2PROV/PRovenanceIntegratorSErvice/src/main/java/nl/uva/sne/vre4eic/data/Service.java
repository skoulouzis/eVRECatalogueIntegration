package nl.uva.sne.vre4eic.data;

public class Service {
    private String name, endpoint, httpMethod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String toString(){
        return "{" + "\"name\":" + "\"" + getName() + "\"" + ", "
                + "\"endpoint\":" + "\"" + getEndpoint() + "\"" + ", "
                + "\"method\":" + "\"" + getHttpMethod() + "\"" + "}";
    }
}
