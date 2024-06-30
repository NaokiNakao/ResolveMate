package com.nakao.resolvemate.infrastructure.logging;

import com.nakao.resolvemate.domain.comment.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CommentServiceLogging {

    private final LogService<CommentServiceLogging> logService;

    @AfterReturning(pointcut = "execution(* com.nakao.resolvemate.domain.comment.CommentService.createComment(..))",
            returning = "result")
    public void createCommentLog(CommentDTO result) {
        logService.info(this, "Comment created: " + result.getId());
    }

}
