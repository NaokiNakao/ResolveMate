package com.nakao.resolvemate.infrastructure.persistance.repository.jpa;

import com.nakao.resolvemate.infrastructure.persistance.entity.AttachmentEntity;
import com.nakao.resolvemate.infrastructure.persistance.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaAttachmentRepository extends JpaRepository<AttachmentEntity, UUID> {

    List<AttachmentEntity> findAllByComment(CommentEntity comment);

}
