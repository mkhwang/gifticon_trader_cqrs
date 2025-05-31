package com.mkhwang.trader.query.review.infra;

import com.mkhwang.trader.common.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ReviewQueryRepositoryImpl.class) // 수동 등록
class ReviewQueryRepositoryImplTest {

  @Autowired
  private ReviewQueryRepositoryImpl reviewQueryRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  void getUserReviewSummary_정상동작() {
    // given
    User user = User.of(
            "testUser",
            "testUser",
            "https://example.com/profile.jpg");
    em.persist(user);


  }
}