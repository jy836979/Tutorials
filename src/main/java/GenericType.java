import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * E - Element (used extensively by the Java Collections Framework)
 * K - Key
 * N - Number
 * T - Type
 * V - Value
 * S,U,V etc. - 2nd, 3rd, 4th types
 */
public class GenericType {
    private static final Logger logger = LoggerFactory.getLogger(GenericType.class);
    public static void main(String[] argv) {
        logger.info("#################");
        SimpleBox simpleBox = new SimpleBox();
        GenerixBox generixBox = new GenerixBox();
        simpleBox.set("aaa");
        generixBox.set("aaa");
        logger.info("Generic Version SimpleBox => {}", generixBox.get());
        logger.info("Generic Version GenerixBox => {}", generixBox.get());
        logger.info("#################");

        logger.info("#################");
        Pair<String, Integer> p1 = new OrderedPair<>("Event", 8);
        Pair<String, String> p2 = new OrderedPair<>("hello", "world");
        logger.info("Multiple Type Parameters p1 => Key - {}, Value - {} ", p1.getKey(), p1.getValue());
        logger.info("Multiple Type Parameters p2 => Key - {}, Value - {} ", p2.getKey(), p2.getValue());
        logger.info("#################");

        logger.info("#################");
        Pair<String, Integer> p1_ = new OrderedPair<>("Event", 8);
        Pair<String, String> p2_ = new OrderedPair<>("hello", "world");
        logger.info("Multiple Type Parameters p1_ => Key - {}, Value - {} ", p1_.getKey(), p1_.getValue());
        logger.info("Multiple Type Parameters p2_ => Key - {}, Value - {} ", p2_.getKey(), p2_.getValue());
        logger.info("#################");
    }
}

// Simple GenerixBox Class
class SimpleBox {
    private Object object;

    public void set(Object object) { this.object = object; }
    public Object get() { return object; }
}

// Generic Version of the GenerixBox Class
class GenerixBox<T> {
    // T stands for "Type"
    private T t;

    public void set(T t) { this.t = t; }
    public T get() { return t; }
}

// Multiple Type Parameters
interface Pair<K, V> {
    public K getKey();
    public V getValue();
}

class OrderedPair<K, V> implements Pair<K, V> {
    private K key;
    private V value;

    public OrderedPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
}