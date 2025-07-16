package com.josh.entity.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "file_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String userId;
    private String fileHash;
    private int totalChunks;
    private LocalDateTime uploadedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "file_chunk_info", joinColumns = @JoinColumn(name = "file_id"))
    private List<ChunkInfo> chunks = new ArrayList<>();

    @Column(nullable = false)
    private boolean synced = false;

}
