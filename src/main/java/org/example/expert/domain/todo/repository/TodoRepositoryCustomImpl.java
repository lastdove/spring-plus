package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.user.entity.QUser;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public TodoRepositoryCustomImpl(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Override
  public Optional<Todo> findByIdWithUser(Long todoId) {
    QTodo todo = QTodo.todo;
    QUser user = QUser.user;

    Todo result = queryFactory
        .selectFrom(todo)
        .leftJoin(todo.user, user) // User와 조인
        .where(todo.id.eq(todoId)) // todoId로 필터링
        .fetchOne(); // 단일 결과 가져오기

    return Optional.ofNullable(result);
  }
}
