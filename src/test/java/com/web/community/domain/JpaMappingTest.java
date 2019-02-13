package com.web.community.domain;

import com.web.community.repository.BoardRepository;
import com.web.community.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaMappingTest {

    private final String boardTestTitle = "테스트";
    private final String email = "test@gmail.com";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Before
    public void init() {
        User user = User.builder()
                .name("가나다")
                .password("123")
                .email(email)
                .createdDate(LocalDateTime.now())
                .build();

        userRepository.save(user);


        Board board = Board.builder()
                .title(boardTestTitle)
                .subTitle("서브 타이틀")
                .content("콘텐츠")
                .boardType(BoardType.FREE)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .user(user)
                .build();
        boardRepository.save(board);
    }

    @Test
    public void test() {
        User user = userRepository.findByEmail(email);

        assertThat(user.getName(), is("가나다"));
        assertThat(user.getPassword(), is("123"));
        assertThat(user.getEmail(), is(email));

        Board board = boardRepository.findByUser(user);

        assertThat(board.getTitle(), is(boardTestTitle));
        assertThat(board.getSubTitle(), is("서브 타이틀"));
        assertThat(board.getContent(), is("콘텐츠"));
        assertThat(board.getBoardType(), is(BoardType.FREE));

    }

}
