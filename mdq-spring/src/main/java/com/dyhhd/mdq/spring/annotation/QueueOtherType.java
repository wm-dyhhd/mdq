package com.dyhhd.mdq.spring.annotation;

/**
 * 对垒其它类型
 *
 * @author lv ning
 */
public class QueueOtherType {

    private int index = -1;

    private QueueOtherMetadata metadata;

    public QueueOtherType() {
    }

    public QueueOtherType(int index, QueueOtherMetadata metadata) {
        this.index = index;
        this.metadata = metadata;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public QueueOtherMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(QueueOtherMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "QueueOtherType{" +
                "index=" + index +
                ", metadata=" + metadata +
                '}';
    }
}
