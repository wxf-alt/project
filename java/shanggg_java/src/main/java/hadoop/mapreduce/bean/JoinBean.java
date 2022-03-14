package hadoop.mapreduce.bean;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@SuppressWarnings("ALL")
public class JoinBean implements Writable {

    private String source;
    private String orderId;
    private String pid;
    private String pname;
    private String amount;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(source);
        out.writeUTF(orderId);
        out.writeUTF(pid);
        out.writeUTF(pname);
        out.writeUTF(amount);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        source = in.readUTF();
        orderId = in.readUTF();
        pid = in.readUTF();
        pname = in.readUTF();
        amount = in.readUTF();
    }

    @Override
    public String toString() {
        return orderId + "\t" + pname + "\t" + amount;
    }
}
