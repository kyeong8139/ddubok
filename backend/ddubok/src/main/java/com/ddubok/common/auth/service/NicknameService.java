package com.ddubok.common.auth.service;

import com.ddubok.api.member.repository.MemberRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NicknameService {

    private static final String[] PREFIX = {
        "날쌘", "빠른", "느긋한", "귀여운", "용감한", "명석한", "강인한", "밝은", "행복한", "연약한",
        "조용한", "활기찬", "평화로운", "든든한", "재밌는", "깔끔한", "정다운", "웃는", "씩씩한", "날렵한",
        "상냥한", "푸른", "붉은", "노란", "초록의", "검은", "흰", "깜찍한", "고운", "똑똑한",
        "순수한", "차분한", "냉정한", "포근한", "달콤한", "수려한", "강력한", "신비한", "차가운", "따뜻한",
        "친절한", "어두운", "맑은", "힘찬", "강렬한", "신속한", "재빠른", "경쾌한", "활달한", "고요한",
        "활발한", "능숙한", "유연한", "반짝한", "빛나는", "눈부신", "노련한", "침착한", "현명한", "예쁜",
        "착한", "용맹한", "단호한", "은은한", "소박한", "우아한", "명랑한", "즐거운", "환한", "화난",
        "겁없는", "강한", "힘있는", "하얀", "민첩한", "귀중한", "날카로운", "섬세한", "기민한", "예리한",
        "튼튼한", "당당한", "신선한", "깨끗한", "건강한", "늠름한", "진실된", "순진한", "해맑은", "다정한",
        "상큼한", "공평한", "정직한", "성실한", "온화한", "듬직한", "담대한", "진실한", "깊은", "맑은"
    };
    private static final String[] ANIMALS = {
        "다람쥐", "호랑이", "독수리", "사자", "토끼", "늑대", "여우", "곰", "원숭이", "코끼리",
        "코알라", "판다", "고양이", "개", "말", "기린", "얼룩말", "펭귄", "상어", "고래",
        "돌고래", "물개", "수달", "하마", "코뿔소", "쥐", "벌새", "앵무새", "부엉이", "올빼미",
        "까마귀", "까치", "까투리", "해달", "뱀", "도마뱀", "개구리", "도토리", "다랑어", "해파리",
        "해마", "청설모", "잠자리", "갈매기", "햄스터", "두루미", "소", "제비", "참새", "족제비",
        "스컹크", "사슴", "노루", "표범", "재규어", "치타", "눈표범", "퓨마", "고릴라", "침팬지",
        "종달새", "인간", "소라게", "피라냐", "꽃게", "코요테", "북극곰", "뻐꾸기", "불곰", "까치",
        "물소", "들소", "영양", "산양", "흑곰", "백곰", "라마", "알파카", "미어캣", "치타",
        "낙타", "두더지", "두루미", "두꺼비", "들쥐", "당나귀", "고라니", "흑표범", "반달곰", "아기곰",
        "병아리", "가오리", "얼룩말", "독수리", "가재", "쥐", "기러기", "뿔소라", "비둘기", "물총새"
    };
    private static final Random RANDOM = new Random();

    private final MemberRepository memberRepository;

    public String createNickname() {
        String prefix = PREFIX[RANDOM.nextInt(PREFIX.length)];
        String animal = ANIMALS[RANDOM.nextInt(ANIMALS.length)];
        String number = String.format("%04d", RANDOM.nextInt(1000));

        String nickname = prefix + " " + animal + " " + number;

        while (memberRepository.existsByNickname(nickname)) {
            prefix = PREFIX[RANDOM.nextInt(PREFIX.length)];
            animal = ANIMALS[RANDOM.nextInt(ANIMALS.length)];
            number = String.format("%04d", RANDOM.nextInt(1000));

            nickname = prefix + " " + animal + " " + number;
        }

        return nickname;
    }
}
