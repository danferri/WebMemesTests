package model;

import faker.MemeFaker;

public class Meme {
    private TipoMeme tipo;
    private String url;
    private String titulo;
    private String descricao;

    public Meme() {

    }

    public Meme(TipoMeme tipo, String url, String titulo, String descricao) {
        this.tipo = tipo;
        this.url = url;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public static Meme FromFakerVideo() {
        Meme meme = new Meme();

        meme.tipo = TipoMeme.VIDEO;
        meme.url = MemeFaker.getUrlVideos();
        meme.titulo = MemeFaker.getTitle();
        meme.descricao =MemeFaker.getDescricao();
        return meme;
    }

    public static Meme FromFakerImage() {
        Meme meme = new Meme();

        meme.tipo = TipoMeme.IMAGE;
        meme.url = MemeFaker.getUrlImage();
        meme.titulo = MemeFaker.getTitle();
        meme.descricao =MemeFaker.getDescricao();
        return meme;
    }

    public static Meme FromFaker() {
        Meme meme = new Meme();

        meme.tipo = MemeFaker.getMemeType();
        if(meme.tipo == TipoMeme.IMAGE) {
            meme.url = MemeFaker.getUrlImage();
        } else {
            meme.url = MemeFaker.getUrlVideos();
        }
        meme.titulo = MemeFaker.getTitle();
        meme.descricao =MemeFaker.getDescricao();
        return meme;
    }


    public TipoMeme getTipo() {
        return tipo;
    }

    public void setTipo(TipoMeme tipo) {
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Meme{" +
                "tipo=" + tipo +
                ", url='" + url + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
