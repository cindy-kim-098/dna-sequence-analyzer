public class LinkStrand implements IDnaStrand {

    private static class Segment {
        String data;
        Segment next;

        Segment(String d) {
            data = d;
        }

        Segment(String d, Segment nxt) {
            data = d;
            next = nxt;
        }
    }

    private Segment head;
    private Segment tail;
    private long length;
    private int appendCount;
    private int lastIndex;
    private Segment current;
    private int localIndex;

    public LinkStrand() {
        this("");
    }

    public LinkStrand(String dna) {
        reset(dna);
    }

    @Override
    public void initialize(String source) {
        reset(source);
    }

    private void reset(String src) {
        head = new Segment(src);
        tail = head;
        length = src.length();
        appendCount = 0;
        lastIndex = 0;
        current = head;
        localIndex = 0;
    }

    @Override
    public long size() {
        return length;
    }

    @Override
    public IDnaStrand getInstance(String source) {
        return new LinkStrand(source);
    }

    @Override
    public IDnaStrand append(String dna) {
        Segment node = new Segment(dna);
        tail.next = node;
        tail = node;
        length += dna.length();
        appendCount++;
        return this;
    }

    @Override
    public int getAppendCount() {
        return appendCount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Segment ptr = head; ptr != null; ptr = ptr.next) {
            builder.append(ptr.data);
        }
        return builder.toString();
    }

    @Override
    public IDnaStrand reverse() {
        LinkStrand result = new LinkStrand();
        result.head = null;
        result.tail = null;
        for (Segment node = head; node != null; node = node.next) {
            String revStr = new StringBuilder(node.data).reverse().toString();
            Segment front = new Segment(revStr);
            if (result.head == null) {
                result.head = result.tail = front;
            } else {
                front.next = result.head;
                result.head = front;
            }
        }
        result.length = this.length;
        result.appendCount = this.appendCount;
        result.current = result.head;
        result.lastIndex = 0;
        result.localIndex = 0;
        return result;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(index);
        }
        if (current == null || index < lastIndex) {
            current = head;
            lastIndex = 0;
            localIndex = 0;
        }
        while (lastIndex != index) {
            lastIndex++;
            localIndex++;
            if (localIndex >= current.data.length()) {
                localIndex = 0;
                current = current.next;
            }
        }
        return current.data.charAt(localIndex);
    }
}
