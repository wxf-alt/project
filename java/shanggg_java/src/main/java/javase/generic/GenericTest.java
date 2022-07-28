package javase.generic;

/**
 * @author wxf
 */
public class GenericTest<T> {

    private T data;
    private String order;
    private Boolean flag;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }



}
