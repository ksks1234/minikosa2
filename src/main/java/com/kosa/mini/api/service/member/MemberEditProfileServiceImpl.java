package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.RoleRepository;
import com.kosa.mini.api.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MemberEditProfileServiceImpl {

    @Autowired
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberEditProfileServiceImpl(MemberRepository memberRepository,
                                        RoleRepository roleRepository,
                                        PasswordEncoder passwordEncoder,
                                        StoreRepository storeRepository) {

        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member update(Integer memberId, MemberEditProfileDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        log.info("암호화  pw"+ dto.getPassword());
        Member member = dto.toEntity();
        log.info("password 인코딩"+member.getPassword());
        log.info("id: "+memberId.toString()+"member: "+member.toString());
        // 2. id 조회하기
        Member members = memberRepository.findById(memberId).orElse(null);
        // 3. 예외 처리
        if(members == null || memberId != member.getMemberId()) {
            //400, 잘못된 요청 응답!
            log.info("잘못된 요청! id : "+memberId.toString()+"member: "+member.toString());
            return null;
        }
        //비밀번호 암호화
        if(dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            members.setPassword(dto.getPassword());
        }

        //4. 업데이트 및 정상 응답코드(200)날리기
        members.put(member);
        Member updated = memberRepository.save(members);
        return updated;
    }

    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
