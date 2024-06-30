package com.nakao.resolvemate.domain.attachment;

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
public class AttachmentDTO implements Serializable {
    private UUID id;
    private UUID commentId;
    private String fileName;
    private String fileType;
    private byte[] data;
}
