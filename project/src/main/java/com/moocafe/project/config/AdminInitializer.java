package com.moocafe.project.config;


import com.moocafe.project.constent.Role;
import com.moocafe.project.dao.MemberDao;
import com.moocafe.project.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final MemberDao memberDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public void run(String... args) throws Exception {
        String adminID ="admin";
        Optional<Member> optionalMember = memberDao.findByUserId(adminID);
        if(!optionalMember.isPresent()){
            Member adminMember = Member.builder().userId(adminID)
                    .role(Role.ROLE_ADMIN)
                    .userName("관리자")
                    .email("phm8568s@naver.com")
                    .userPw(bCryptPasswordEncoder.encode("1234"))
                    .build();
            memberDao.save(adminMember);

            Member member1 = Member.builder().userId("manager")
                    .role(Role.ROLE_MANAGER)
                    .userName("매니저")
                    .email("manager@member.com")
                    .userPw(bCryptPasswordEncoder.encode("1234"))
                    .build();
            Member member2 = Member.builder().userId("user")
                    .role(Role.ROLE_USER)
                    .userName("user")
                    .email("user@member.com")
                    .userPw(bCryptPasswordEncoder.encode("1234"))
                    .build();
            memberDao.save(member1);
            memberDao.save(member2);
        }else{
            System.out.println("관리자 계정이 이미 있습니다.");
        }
    }
}
