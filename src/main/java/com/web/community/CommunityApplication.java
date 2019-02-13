package com.web.community;

import com.web.community.repository.BoardRepository;
import com.web.community.repository.UserRepository;
import com.web.community.resolver.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
public class CommunityApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

//    @Bean
//    public CommandLineRunner runner(UserRepository userRepository, BoardRepository boardRepository) throw Exception {
//        return (args) -> {
//
//        }
//    }

}

