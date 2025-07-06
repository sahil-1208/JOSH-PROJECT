package com.josh.entity.file;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChunkInfo {
    private int chunkNumber;
    private String containerName;
    private String remotePath;
}
