package com.nakao.resolvemate.infrastructure.persistance.repository;

import com.nakao.resolvemate.domain.attachment.Attachment;
import com.nakao.resolvemate.domain.attachment.AttachmentRepository;
import com.nakao.resolvemate.domain.comment.Comment;
import com.nakao.resolvemate.infrastructure.persistance.entity.AttachmentEntity;
import com.nakao.resolvemate.infrastructure.persistance.mapper.AttachmentMapper;
import com.nakao.resolvemate.infrastructure.persistance.mapper.CommentMapper;
import com.nakao.resolvemate.infrastructure.persistance.repository.jpa.JpaAttachmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AttachmentRepositoryImpl implements AttachmentRepository {

    private final JpaAttachmentRepository repository;

    @Override
    @CacheEvict(value = "attachments", key = "#attachment.comment.id")
    public Attachment save(Attachment attachment) {
        AttachmentEntity attachmentEntity = AttachmentMapper.toEntity(attachment);
        return AttachmentMapper.toModel(repository.save(attachmentEntity));
    }

    @Override
    @Transactional
    @Cacheable(value = "attachments", key = "#comment.id")
    public List<Attachment> findAllByComment(Comment comment) {
        return repository.findAllByComment(CommentMapper.toEntity(comment)).stream()
                .map(AttachmentMapper::toModel)
                .collect(Collectors.toList());
    }

}
