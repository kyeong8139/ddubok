package com.ddubok.common.openai.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ModelIdRepository {

    private static final String MODEL_ID_KEY = "currentModelId";
    private static final String JOB_ID_KEY = "currentJobId";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * Redis에서 현재 활성화된 모델 ID를 가져옴
     *
     * @return 현재 모델 ID 또는 null
     */
    public String getCurrentModelId() {
        return redisTemplate.opsForValue().get(MODEL_ID_KEY);
    }

    /**
     * Redis에서 현재 진행 중인 Fine-Tuning Job ID를 가져옴
     *
     * @return 현재 Job ID 또는 null
     */
    public String getCurrentJobId() {
        return redisTemplate.opsForValue().get(JOB_ID_KEY);
    }

    /**
     * Redis에 새로운 모델 ID를 업데이트
     *
     * @param newModelId 새로운 모델 ID
     */
    public void updateModelId(String newModelId) {
        redisTemplate.opsForValue().set(MODEL_ID_KEY, newModelId);
    }

    /**
     * Redis에 새로운 Fine-Tuning Job ID를 업데이트
     *
     * @param newJobId 새로운 Job ID
     */
    public void updateJobId(String newJobId) {
        redisTemplate.opsForValue().set(JOB_ID_KEY, newJobId);
    }
}
