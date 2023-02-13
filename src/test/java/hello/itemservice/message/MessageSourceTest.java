package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest     // - 스프링 부트는 @SpringBootTest 어노테이션을 통해 스프링부트 어플리케이션 테스트에 필요한 거의 모든 의존성을 제공한다.
                    //@SpringBootTest를 사용하면 @SpringBootApplication이 있는 CoreApplication을 찾아서 사용합니다.
                    //@SpringBootTest가 있으면 해당 테스트 클래스는 특수하게 @Autowired를 허용해줍니다. 이것은 스프링 빈으로 등록되어서 그런 것은 아니고, JUnit과 스프링이 예외적으로 테스트를 편리하게 하도록 허용하는 기능입니다.
public class MessageSourceTest {

    @Autowired
    MessageSource ms;        //스프링부트가 MessageSource를 자동으로 등록해준다.우리는 그냥 가져다 쓰면된다. 메시지와 국제화 가능.    //그러면 messages.properties와 messages_en.properties를 들고있다.

    @Test
    void helloMessage(){
        String result = ms.getMessage("hello", null, null);     //locale이 null이면 defult인 messages.properties가 실행됨.
        //argument는 hello.name=hello 뒤에  {0}
        Assertions.assertThat(result).isEqualTo("안녕");

    }

    @Test
    void notFoundMessageCode(){
        //메시지 코드를 찾을 수 없다면
        //못 찾으면 예외가 발생한다.
        //ms.getMessage("no_code", null, null);
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);            //no_code라는건 messages.properties에 없다.
    }

    @Test
    void notFoundMessageCodeDefaultMessage(){
        String result = ms.getMessage("no_code", null, "기본 메시지", null);//메시지를 못찾으면 defualtMessage를 준다. "기본 메시지"
        assertThat(result).isEqualTo("기본 메시지");
    }

    //messages.properties에 있는 매개변수를 써보자
    @Test
    void argumentMessage(){
        String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);//값을 넘겨서 messages.properties의 {0}부분을 치환 해야된다. args파라미터 자리는 Object[] 타입이다.
        assertThat(message).isEqualTo("안녕 Spring");
    }


    // 국제화 부분
    @Test
    void defaultLang(){
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
        //둘다 같은 messages.properties
    }

    @Test
    void enLang(){
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }


}
