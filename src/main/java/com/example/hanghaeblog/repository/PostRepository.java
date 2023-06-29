package com.example.hanghaeblog.repository;

import com.example.hanghaeblog.dto.PostRequestDto;
import com.example.hanghaeblog.dto.PostResponseDto;
import com.example.hanghaeblog.entity.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
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
               String title = rs.getString("title");
                String userName = rs.getString("username");
                String content = rs.getString("content");

                // 작성 시간 가져오기
                Timestamp timestamp = rs.getTimestamp("localDate");
                LocalDateTime LocalDateTime = timestamp.toLocalDateTime();
                return new PostResponseDto(title, userName, content, LocalDateTime);
            }
        });
    }

    // 수정
    public void update(Long id, PostRequestDto requestDto) {
        String sql = "UPDATE Post SET title = ?, username = ?, content = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getUserName(), requestDto.getContent(), requestDto.getPassword(), id);
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
                post.setTitle(resultSet.getString("title"));
                post.setUserName(resultSet.getString("username"));
                post.setContent(resultSet.getString("content"));

                // 작성 시간 가져오기
                Timestamp timestamp = resultSet.getTimestamp("localDate");
                LocalDateTime localDate = timestamp.toLocalDateTime();
                post.setLocalDate(localDate);
                return post;
            } else {
                return null;
            }
        }, id);
    }
}