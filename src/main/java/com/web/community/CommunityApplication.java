package com.web.community;

import com.web.community.config.RestTemplateLoggingRequestInterceptor;
import com.web.community.domain.Board;
import com.web.community.domain.BoardType;
import com.web.community.domain.User;
import com.web.community.filter.HttpLoggingFilter;
import com.web.community.repository.BoardRepository;
import com.web.community.repository.UserRepository;
import com.web.community.resolver.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class CommunityApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

    @Autowired
    private HttpLoggingFilter httpLoggingFilter;

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Autowired
    RestTemplateLoggingRequestInterceptor restTemplateLoggingRequestInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(httpLoggingFilter);
        registration.addUrlPatterns("*");
        return registration;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();


        restTemplate.setInterceptors(Collections.singletonList(restTemplateLoggingRequestInterceptor));

        return restTemplate;
    }
//
//    @Bean
//    public CommandLineRunner runner(UserRepository userRepository, BoardRepository boardRepository) {
//        return (args) -> {
//            User user = userRepository.save(User.builder()
//                    .name("havi")
//                    .password("test")
//                    .email("havi@gmail.com")
//                    .createdDate(LocalDateTime.now())
//                    .build());
//
//            IntStream.rangeClosed(1, 200).forEach(index ->
//                    boardRepository.save(Board.builder()
//                            .title("게시글" + index)
//                            .subTitle("순서" + index)
//                            .content("컨텐츠")
//                            .boardType(BoardType.FREE)
//                            .createdDate(LocalDateTime.now())
//                            .updatedDate(LocalDateTime.now())
//                            .user(user).build())
//            );
//        };
//    }
}

