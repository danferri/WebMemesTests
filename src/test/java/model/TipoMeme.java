package model;

public enum TipoMeme {
    IMAGE("image"),
    VIDEO("video");
    private final String value;

    TipoMeme(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }

}
