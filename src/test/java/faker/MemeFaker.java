package faker;

import com.github.javafaker.Faker;
import model.TipoMeme;
import java.util.List;
import java.util.Random;

public class MemeFaker {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private static final List<String> urlImages = List.of(
            "https://quatrorodas.abril.com.br/wp-content/uploads/2022/09/Charge-67-Mustang-electric-06.webp?crop=1&resize=1212,909",
            "https://images.unsplash.com/photo-1629446488105-122120352a03?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDZ8RmtXWEc1VFIyMDR8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1731354233513-60e9edaddc5d?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxmZWF0dXJlZC1waG90b3MtZmVlZHw3fHx8ZW58MHx8fHx8",
            "https://images.unsplash.com/photo-1680861173988-5ab3deabb950?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDEzfEZrV1hHNVRSMjA0fHxlbnwwfHx8fHw%3D",
            "https://images.unsplash.com/photo-1691264122434-3b5a1dac81d5?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDI2fEZrV1hHNVRSMjA0fHxlbnwwfHx8fHw%3D"
            );
    private static final List<String> urlVideos = List.of(
            "https://www.youtube.com/watch?v=PFooIMCTXG4&t=5s&ab_channel=CoisadeNerd",
            "https://www.youtube.com/watch?v=Q0aQBijF8OY&ab_channel=Futurices",
            "https://www.youtube.com/watch?v=ChjPU9wOEzg&ab_channel=Futurices",
            "https://www.youtube.com/watch?v=7ESB0N4WyXM&ab_channel=Avi%C3%B5eseM%C3%BAsicas",
            "https://www.youtube.com/watch?v=us5LYaxjhGM&ab_channel=Sess%C3%A3oNerd"
            );


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
        return faker.lorem().characters(3,50);
    }

    public static String getShortTitle() {
        return faker.lorem().characters(2);
    }

    public static String getLongTitle() {
        return faker.lorem().characters(52);
    }

    public static String getLongDescricao() {
        return faker.lorem().characters(52);
    }

    public static String getDescricao() {
        return faker.lorem().characters(1,50);
    }

}
