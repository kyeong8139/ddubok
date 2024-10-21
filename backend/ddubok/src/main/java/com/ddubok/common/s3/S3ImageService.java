package com.ddubok.common.s3;

import com.ddubok.common.s3.dto.FileMetaInfo;
import org.springframework.web.multipart.MultipartFile;

public interface S3ImageService {

    /**
     * 행성 이미지 업로드
     *
     * @param file     이미지 파일
     * @param seasonId 카드의 시즌 id
     * @return 저장된 이미지 파일에 대한 정보
     */
    FileMetaInfo uploadCardImg(MultipartFile file, long seasonId);

    /**
     * 파일 확장자 추출
     *
     * @param fileName 파일명
     * @return 추출된 확장자 반환
     */
    default String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
