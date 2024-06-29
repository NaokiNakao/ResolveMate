package com.nakao.resolvemate.domain.attachment;

import com.nakao.resolvemate.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment implements Serializable {
    private UUID id;
    private Comment comment;
    private String fileName;
    private String fileType;
    private byte[] data;
}
