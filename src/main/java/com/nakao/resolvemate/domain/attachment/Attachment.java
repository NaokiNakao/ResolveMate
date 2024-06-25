package com.nakao.resolvemate.domain.attachment;

import com.nakao.resolvemate.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    private UUID id;
    private Comment comment;
    private String filename;
    private byte[] data;
}
