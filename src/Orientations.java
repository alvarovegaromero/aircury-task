public enum Orientations {
    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3);

    private final int value;

    Orientations(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}