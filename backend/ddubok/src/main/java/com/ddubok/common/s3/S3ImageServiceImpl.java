package com.ddubok.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ddubok.common.s3.dto.FileMetaInfo;
import com.ddubok.common.s3.exception.S3Exception;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ImageServiceImpl implements S3ImageService {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final String CARD_IMG_DIR = "card/";
    private final String BANNER_IMG_DIR = "banner/";

    /**
     * {@inheritDoc}
     */
    @Override
    public FileMetaInfo uploadCardImg(MultipartFile file, long seasonId) {
        return getFileMetaInfo(file, seasonId, CARD_IMG_DIR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileMetaInfo uploadBannerImg(MultipartFile file, long seasonId) {
        return getFileMetaInfo(file, seasonId, BANNER_IMG_DIR);
    }

    /**
     * FileMetaInfo를 생성한다.
     *
     * @param file     업로드할 파일
     * @param seasonId 해당하는 season 고유 id
     * @param ImgDir   업로드할 이미지의 디렉토리
     * @return 이미지의 정보 반환
     */
    private FileMetaInfo getFileMetaInfo(MultipartFile file, long seasonId, String ImgDir) {
        String url = upload(file, ImgDir, seasonId);
        String name = file.getOriginalFilename();
        String format = getFileExtension(name);
        long size = file.getSize();
        return FileMetaInfo.builder()
            .url(url)
            .name(name)
            .format(format)
            .size(size)
            .build();
    }

    /**
     * S3로 파일 업로드 하기
     *
     * @param file     변환된 File
     * @param dirName  디렉토리 명
     * @param memberId 업로드하는 멤버 아이디
     * @return 이미지 url
     */
    private String upload(MultipartFile file, String dirName, Long memberId) {
        if (file.isEmpty()) {
            throw new S3Exception("Image file is empty");
        }
        String fileName = dirName + memberId + "/" + UUID.randomUUID() + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new S3Exception("error: MultipartFile -> S3 upload fail");
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
