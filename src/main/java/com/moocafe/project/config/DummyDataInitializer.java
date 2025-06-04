package com.moocafe.project.config;
import com.moocafe.project.constent.Role;
import com.moocafe.project.dao.InventoryItemDao;
import com.moocafe.project.dao.MemberDao;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.service.MemberStoreService;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DummyDataInitializer {
    private final MemberStoreService mss;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final InventoryItemRepository iir;
    public void setDummy(String adminID) throws ParseException {
        Member adminMember = Member.builder().userId(adminID)
                .role(Role.ROLE_ADMIN)
                .userName("관리자")
                .email("phm8568s@naver.com")
                .userPw(bCryptPasswordEncoder.encode("1234"))
                .build();
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
        Member member01 = Member.builder().userId("user01")
                .role(Role.ROLE_USER)
                .userName("user01")
                .email("user01@member.com")
                .userPw(bCryptPasswordEncoder.encode("1234"))
                .build();
        Member member02 = Member.builder().userId("user02")
                .role(Role.ROLE_USER)
                .userName("user02")
                .email("user02@member.com")
                .userPw(bCryptPasswordEncoder.encode("1234"))
                .build();
        Member member03 = Member.builder().userId("user03")
                .role(Role.ROLE_USER)
                .userName("user03")
                .email("user03@member.com")
                .userPw(bCryptPasswordEncoder.encode("1234"))
                .build();
        Store store=Store.builder()
                .name("본사")
                .storeNumber("000-00-00000")
                .space("본사")
                .tel("010-0000-0000")
                .zipcode("00000")
                .address01("본사창고 주소")
                .address02("본사창고 상세주소")
                .state("운영중")
                .build();
        Store store1=Store.builder()
                .name("월담 고양점")
                .storeNumber("224-95-07416")
                .space("경기 일산")
                .tel("010-1111-1111")
                .zipcode("10401")
                .address01("경기도 고양시 일산동구 중앙로1275번길 38-10")
                .address02("201호(장항동, 우림 로데오스위트)")
                .state("운영중")
                .build();
        Store store01=Store.builder()
                .name("홍대점")
                .storeNumber("234-56-78901")
                .space("서울 마포구")
                .tel("02-234-5678")
                .zipcode("04050")
                .address01("서울 마포구 양화로 45")
                .address02("2층")
                .state("운영중")
                .build();
        Store store02=Store.builder()
                .name("부산점")
                .storeNumber("345-67-89012")
                .space("부산 해운대구")
                .tel("02-234-5678")
                .zipcode("48093")
                .address01("부산 해운대구 센텀동로 99")
                .address02("10층")
                .state("점검중")
                .build();
        Store store03=Store.builder()
                .name("일산점")
                .storeNumber("234-56-78931")
                .space("고양시 일산서구")
                .tel("02-3344-5678")
                .zipcode("04220")
                .address01("경기 고양시 일산서구 112-1")
                .address02("2층")
                .state("운영중")
                .build();
        mss.save(adminMember,store);
        mss.save(member1,store);
        mss.save(member2,store1);
        mss.save(member01,store01);
        mss.save(member02,store02);
        mss.save(member03,store03);

        //=================================================================================
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        InventoryItem inventoryItem01 = new InventoryItem("A001", "우유", "식품", "1L", 100, 2000, "냉장", sdf.parse("2025-06-30"));
        InventoryItem inventoryItem02 = new InventoryItem("A002", "계란", "식품", "30구", 50, 5000, "냉장", sdf.parse("2025-06-15"));
        InventoryItem inventoryItem03 = new InventoryItem("A003", "고무장갑", "생활용품", "대형", 200, 1500, "비냉장", sdf.parse("2026-01-01"));
        InventoryItem inventoryItem04 = new InventoryItem("A004", "원두", "원재료", "EA", 0, 2000, "비냉장", sdf.parse("2026-01-01"));
        InventoryItem inventoryItem05 = new InventoryItem("A005", "아라비카원두", "원재료", "EA", 0, 2000, "비냉장", sdf.parse("2026-01-01"));
        iir.save(inventoryItem01);
        iir.save(inventoryItem02);
        iir.save(inventoryItem03);
        iir.save(inventoryItem04);
        iir.save(inventoryItem05);



        System.out.println("더미데이터를 생성하였습니다.");
    }
}