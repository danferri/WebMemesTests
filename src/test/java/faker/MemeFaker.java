package faker;

import com.github.javafaker.Faker;
import model.TipoMeme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MemeFaker {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private static final List<String> urlImages = List.of("https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909");
    private static final List<String> urlVideos = List.of("https://www.youtube.com/watch?v=PFooIMCTXG4&t=5s&ab_channel=CoisadeNerd");


    public static TipoMeme getMemeType() {
        if(random.nextInt(2) == 0) {
            return TipoMeme.IMAGE;
        }
        return TipoMeme.VIDEO;
    }

    public static String getUrlImage() {
        int index = random.nextInt(urlImages.size());
        return urlImages.get(index);
    }

    public static String getUrlVideos() {
        int index = random.nextInt(urlVideos.size());
        return urlVideos.get(index);
    }

    public static String getTitle() {
        return faker.book().title();
    }

    public static String getDescricao() {
        return faker.dune().quote();
    }

}
