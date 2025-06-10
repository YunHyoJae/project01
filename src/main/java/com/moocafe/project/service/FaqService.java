package com.moocafe.project.service;

import com.moocafe.project.dao.FaqDao;
import com.moocafe.project.dao.FaqReplyDao;
import com.moocafe.project.dao.FranchiseDao;
import com.moocafe.project.dao.FranchiseReplyDao;
import com.moocafe.project.dto.*;
import com.moocafe.project.entity.Faq;
import com.moocafe.project.entity.FranchiseBoard;
import com.moocafe.project.entity.FranchiseReply;
import com.moocafe.project.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqDao dao;
    private final FaqReplyDao replyDao;
    public int save(FaqBoardSaveDto dto){
        return dao.save(FaqBoardSaveDto.toEntity(dto));
    }
    public FaqBoardDto get(int id){
        Optional<Faq> entity = dao.findById(id);
        return entity.map(FaqBoardDto::toDto).orElse(null);
    }
    public Page<FaqBoardDto> page(String type, String keyword, Pageable pageable){
        Page<Faq> faqBoardPage;
        List<FaqBoardDto> dtoList = switch (type) {
            case "title" -> {
                faqBoardPage = dao.findByTitle(keyword, pageable);
                yield faqBoardPage.getContent().stream()
                        .map(FaqBoardDto::toDto)
                        .collect(Collectors.toList());
            }
            case "category" -> {
                faqBoardPage = dao.findByCategory(keyword, pageable);
                yield faqBoardPage.getContent().stream()
                        .map(FaqBoardDto::toDto)
                        .collect(Collectors.toList());
            }
            case "state" -> {
                faqBoardPage = dao.findByState(keyword, pageable);
                yield faqBoardPage.getContent().stream()
                        .map(FaqBoardDto::toDto)
                        .collect(Collectors.toList());
            }
            case "member" -> {
                faqBoardPage = dao.findByMember_NameContaining(keyword, pageable);
                yield faqBoardPage.getContent().stream()
                        .map(FaqBoardDto::toDto)
                        .collect(Collectors.toList());
            }
            default -> {
                faqBoardPage = dao.findAll(pageable);
                yield faqBoardPage.getContent().stream()
                        .map(FaqBoardDto::toDto)
                        .collect(Collectors.toList());
            }
        };
        return new PageImpl<>(dtoList, pageable, faqBoardPage.getTotalElements());
    }
    public Page<FaqBoardDto> memberPage(String type, String keyword, Member member, Pageable pageable){
        Page<Faq> faqBoardPage;
        List<FaqBoardDto> dtoList = switch (type) {
            case "title" -> {
                faqBoardPage = dao.findByTitleAndMember(keyword, member, pageable);
                yield faqBoardPage.getContent().stream()
                        .map(FaqBoardDto::toDto)
                        .collect(Collectors.toList());
            }
            case "category" -> {
                faqBoardPage = dao.findByCategoryAndMember(keyword, member, pageable);
                yield faqBoardPage.getContent().stream()
                        .map(FaqBoardDto::toDto)
                        .collect(Collectors.toList());
            }
            default -> {
                faqBoardPage = dao.findByMember(member, pageable);
                yield faqBoardPage.getContent().stream()
                        .map(FaqBoardDto::toDto)
                        .collect(Collectors.toList());
            }
        };
        return new PageImpl<>(dtoList, pageable, faqBoardPage.getTotalElements());
    }
}