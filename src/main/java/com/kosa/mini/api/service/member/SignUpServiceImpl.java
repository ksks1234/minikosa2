package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.SignupDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Role;
import com.kosa.mini.api.exception.DuplicateEmailException;
import com.kosa.mini.api.exception.DuplicateNicknameException;
import com.kosa.mini.api.exception.SignupException;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class SignUpServiceImpl implements SignUpService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired

    public SignUpServiceImpl(MemberRepository memberRepository,
                RoleRepository roleRepository,
                PasswordEncoder passwordEncoder) {
            this.memberRepository = memberRepository;
            this.roleRepository = roleRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public boolean signUp(SignupDTO dto) throws DuplicateEmailException, DuplicateNicknameException, SignupException {
            try {
                if (memberRepository.existsByEmail(dto.getEmail())) {
                    throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
                }
                if (memberRepository.existsByNickname(dto.getNickname())) {
        throw new DuplicateNicknameException("이미 사용 중인 닉네임입니다.");
    }
    Role userRole = roleRepository.findByRoleName("ROLE_USER")
            .orElseThrow(() -> new SignupException("사용자 역할을 찾을 수 없습니다."));

    Member member = new Member();
            member.setName(dto.getName());
            member.setEmail(dto.getEmail());
            member.setNickname(dto.getNickname());
            member.setPassword(passwordEncoder.encode(dto.getPassword()));
            member.setPhoneNumber(dto.getPhone_number());
            member.setRole(userRole);
            member.setCreatedAt(LocalDateTime.now());

            memberRepository.save(member);
            log.info("회원가입 성공: " + member.toString());
                return true;
            } catch (DuplicateEmailException | DuplicateNicknameException e) {
                throw e;
            } catch (Exception e) {
                throw new SignupException("회원가입 중 오류가 발생했습니다.", e);
            }
        }
    @Override
    public boolean isEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public boolean isNicknameExists(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
