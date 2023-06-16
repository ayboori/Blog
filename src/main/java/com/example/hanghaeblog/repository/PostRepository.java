package com.example.hanghaeblog.repository;


import com.example.hanghaeblog.dto.PostRequestDto;
import com.example.hanghaeblog.dto.PostResponseDto;
import com.example.hanghaeblog.entity.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Post save(Post post) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        // 데이터 삽입
        String sql = "INSERT INTO post (title, userName, password, content) VALUES (?, ?, ? ,?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, post.getTitle());
                    preparedStatement.setString(2, post.getUserName());
                    preparedStatement.setString(3, post.getPassword());
                    preparedStatement.setString(4, post.getContent());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        post.setId(id);

        // 저장한 데이터 리턴
        return post;
    }

    public List<PostResponseDto> findAll() {
        // DB 조회
        String sql = "SELECT * FROM post";

        return jdbcTemplate.query(sql, new RowMapper<PostResponseDto>() {
            @Override
            public PostResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Post 데이터들을 PostResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
               String title = rs.getString("title");
                String password = rs.getString("password");
                String userName = rs.getString("username");
                String content = rs.getString("content");
                String localDate = rs.getString("localDate");
                return new PostResponseDto(id,title, userName, password,content, localDate);
            }
        });
    }

    // 수정
    public void update(Long id, String password, PostRequestDto requestDto) {
        String sql = "UPDATE Post SET title = ?, username = ?, content = ? WHERE id = ?, password = ?";
        jdbcTemplate.update(sql, requestDto, requestDto.getTitle(), requestDto.getUserName(), requestDto.getContent(), id, password);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM Post WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Post findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM post WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Post post = new Post();
                post.setUserName(resultSet.getString("username"));
                post.setContent(resultSet.getString("contents"));
                return post;
            } else {
                return null;
            }
        }, id);
    }
}