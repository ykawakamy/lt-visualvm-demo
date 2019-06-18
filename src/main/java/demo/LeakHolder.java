package demo;

public class LeakHolder {
    byte[] leak;

    public LeakHolder() {
        leak = new byte[1024 * 1024];
    }

    public LeakHolder(int num) {
        leak = new byte[num];
    }
    public void set(byte l) {
        leak[20] = l;
    }

    public byte get() {
        return leak[20];
    }
}
