package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.admin.SuggestionDetailDTO;
import com.kosa.mini.api.dto.member.ContactUsDTO;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.exception.ResourceNotFoundException;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.SuggestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public SuggestionServiceImpl(SuggestionRepository suggestionRepository, MemberRepository memberRepository) {
        this.suggestionRepository = suggestionRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public ContactUs create(ContactUsDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member ID " + dto.getMemberId() + "존재하지 않음"));
        ContactUs contactUs = dto.toEntity(member);
        return suggestionRepository.save(contactUs);
    }

    @Override
    public Page<ContactUs> getSuggestions(String type, String keyword, Pageable pageable) {
        // 정렬 정보 추가: createdAt 필드를 기준으로 내림차순 정렬
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return switch (type.toLowerCase()) {
            case "title" -> suggestionRepository.findByTitleContaining(keyword, sortedPageable);
            case "content" -> suggestionRepository.findByContentContaining(keyword, sortedPageable);
            default -> suggestionRepository.findAll(sortedPageable);
        };
    }

    @Override
    @Transactional
    public void incrementViews(Integer contactId) throws ResourceNotFoundException {
        ContactUs contactUs = suggestionRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("ContactUs not found with id: " + contactId));
        contactUs.setViews(contactUs.getViews() + 1);
        suggestionRepository.save(contactUs);
    }

    @Override
    public SuggestionDetailDTO getSuggestionById(Integer contactId) throws ResourceNotFoundException {
        ContactUs contactUs = suggestionRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("ContactUs not found with id: " + contactId));
        // 엔티티를 DTO로 변환하여 반환
        return SuggestionDetailDTO.builder()
                .contactId(contactUs.getContactId())
                .title(contactUs.getTitle())
                .storeName(contactUs.getStoreName())
                .createdAt(contactUs.getCreatedAt())
                .updatedAt(contactUs.getUpdatedAt())
                .isModified(contactUs.getIsModified())
                .content(contactUs.getContent())
                .views(contactUs.getViews())
                .memberId(contactUs.getMember() != null ? contactUs.getMember().getMemberId() : null) // 연관된 멤버 ID 추가
                .memberName(contactUs.getMember() != null ? contactUs.getMember().getNickname() : null) // 연관된 멤버 이름 추가
                .build();
    }


    @Override
    @Transactional
    public void deleteSuggestion(Integer contactId) throws ResourceNotFoundException {
        ContactUs contactUs = suggestionRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("ContactUs not found with id: " + contactId));
        suggestionRepository.delete(contactUs);
    }
}
