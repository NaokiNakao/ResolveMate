package com.nakao.resolvemate.domain.attachment;

import com.nakao.resolvemate.domain.comment.Comment;

import java.util.List;

public interface AttachmentRepository {

    Attachment save(Attachment attachment);

    List<Attachment> findAllByComment(Comment comment);

}
