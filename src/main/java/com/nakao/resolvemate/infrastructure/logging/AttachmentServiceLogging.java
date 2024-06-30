package com.nakao.resolvemate.infrastructure.logging;

import com.nakao.resolvemate.domain.attachment.AttachmentDTO;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AttachmentServiceLogging {

    private final LogService<AttachmentServiceLogging> logService;

    @AfterReturning(pointcut = "execution(* com.nakao.resolvemate.domain.attachment.AttachmentService.createAttachment(..))",
            returning = "result")
    public void createAttachmentLog(AttachmentDTO result) {
        logService.info(this, "Attachment created: " + result.getId());
    }

}
