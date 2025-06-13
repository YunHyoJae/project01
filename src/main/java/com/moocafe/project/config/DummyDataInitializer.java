package com.moocafe.project.config;
import com.moocafe.project.constent.Role;
import com.moocafe.project.dao.*;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.repository.InventoryStoreRepository;
import com.moocafe.project.repository.PurchaseItemRepository;
import com.moocafe.project.repository.PurchaseRepository;
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
    private final InventoryStoreRepository isr;
    private final PurchaseDao pd;
    private final PurchaseItemDao pid;
    private final MenuDao md;
    private final MenuPriceDao mpd;
    private final SalesDao sd;
    private final OutBoundDao obd;
    private final OutBoundItemDao obid;
    private final StoreOrderDao sod;
    private final StoreOrderDetailDao sodd;
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
                .userName("유저")
                .email("user@member.com")
                .userPw(bCryptPasswordEncoder.encode("1234"))
                .build();
        Member member01 = Member.builder().userId("user01")
                .role(Role.ROLE_USER)
                .userName("유저01")
                .email("user01@member.com")
                .userPw(bCryptPasswordEncoder.encode("1234"))
                .build();
        Member member02 = Member.builder().userId("user02")
                .role(Role.ROLE_USER)
                .userName("유저02")
                .email("user02@member.com")
                .userPw(bCryptPasswordEncoder.encode("1234"))
                .build();
        Member member03 = Member.builder().userId("user03")
                .role(Role.ROLE_USER)
                .userName("유저03")
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
                .state("영업중")
                .build();
        Store store1=Store.builder()
                .name("월담 고양점")
                .storeNumber("224-95-07416")
                .space("경기 일산")
                .tel("010-1111-1111")
                .zipcode("10401")
                .address01("경기도 고양시 일산동구 중앙로1275번길 38-10")
                .address02("201호(장항동, 우림 로데오스위트)")
                .state("영업중")
                .build();
        Store store01=Store.builder()
                .name("홍대점")
                .storeNumber("234-56-78901")
                .space("서울 마포구")
                .tel("02-234-5678")
                .zipcode("04050")
                .address01("서울 마포구 양화로 45")
                .address02("2층")
                .state("영업중")
                .build();
        Store store02=Store.builder()
                .name("부산점")
                .storeNumber("345-67-89012")
                .space("부산 해운대구")
                .tel("02-234-5678")
                .zipcode("48093")
                .address01("부산 해운대구 센텀동로 99")
                .address02("10층")
                .state("휴업")
                .build();
        Store store03=Store.builder()
                .name("일산점")
                .storeNumber("234-56-78931")
                .space("고양시 일산서구")
                .tel("02-3344-5678")
                .zipcode("04220")
                .address01("경기 고양시 일산서구 112-1")
                .address02("2층")
                .state("영업중")
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

        //=================================================================================
        InventoryStore inventoryStore01 = new InventoryStore("A001",1,50,sdf.parse("2025-05-01"));
        InventoryStore inventoryStore02 = new InventoryStore("A001",2,30,sdf.parse("2025-05-01"));
        InventoryStore inventoryStore03 = new InventoryStore("A002",3,40,sdf.parse("2025-05-01"));
        InventoryStore inventoryStore04 = new InventoryStore("A003",4,100,sdf.parse("2025-05-01"));
        isr.save(inventoryStore01);
        isr.save(inventoryStore02);
        isr.save(inventoryStore03);
        isr.save(inventoryStore04);

        //=================================================================================
        Purchase purchase01 = Purchase.builder()
                .purchaseNumber("P20240603-004")
                .typeOrder("발주")
                .build();
        pd.savePurchase(purchase01);
        //=================================================================================
        PurchaseItem purchaseItem01 = PurchaseItem.builder()
                .itemCode(inventoryItem01)
                .purchaseNumber(purchase01)
                .receivedQuantity(100)
                .dueDate(sdf.parse("2025-06-10"))
                .expirationDate(sdf.parse("2025-06-30"))
                .supplier("매일유업")
                .status("진행중")
                .build();
        PurchaseItem purchaseItem02 = PurchaseItem.builder()
                .itemCode(inventoryItem02)
                .purchaseNumber(purchase01)
                .receivedQuantity(200)
                .dueDate(sdf.parse("2025-06-10"))
                .expirationDate(sdf.parse("2025-06-30"))
                .supplier("패키지업체")
                .status("진행중")
                .build();
        PurchaseItem purchaseItem03 = PurchaseItem.builder()
                .itemCode(inventoryItem03)
                .purchaseNumber(purchase01)
                .receivedQuantity(100)
                .dueDate(sdf.parse("2025-06-10"))
                .expirationDate(sdf.parse("2025-06-30"))
                .supplier("패키지업체")
                .status("진행중")
                .build();
        pid.savePurchaseItem(purchaseItem01);
        pid.savePurchaseItem(purchaseItem02);
        pid.savePurchaseItem(purchaseItem03);
        //=================================================================================
        Menu menu01 = new Menu("M001","아메리카노","A002",100);
        Menu menu02 = new Menu("M001","아메리카노","A003",1);
        md.insertMenu(menu01);
        md.insertMenu(menu02);
        //=================================================================================
        MenuPrice menuPrice01 = new MenuPrice("M001",3500);
        mpd.insertMenuPrice(menuPrice01);
        //=================================================================================
        Sales sales01 = new Sales(2,"M001","아메리카노",3);
        sd.insertSale(sales01);
        //=================================================================================
        OutBound outBound01 = OutBound.builder()
                .storeId(1)
                .approved('Y')
                .dueDate(sdf.parse("2025-06-10"))
                .status("출고완료")
                .build();
        obd.save(outBound01);
        //=================================================================================
        OutBoundItem outBoundItem01 = OutBoundItem.builder()
                .outBound(outBound01)
                .itemCode("A001")
                .receivedQuantity(30)
                .build();
        OutBoundItem outBoundItem02 = OutBoundItem.builder()
                .outBound(outBound01)
                .itemCode("A002")
                .receivedQuantity(20)
                .build();
        OutBoundItem outBoundItem03 = OutBoundItem.builder()
                .outBound(outBound01)
                .itemCode("A003")
                .receivedQuantity(50)
                .build();
        obid.save(outBoundItem01);
        obid.save(outBoundItem02);
        obid.save(outBoundItem03);
        //=================================================================================
        StoreOrder storeOrder01 = StoreOrder.builder()
                .orderNumber("20240602-007")
                .storeId(2)
                .build();
        StoreOrder storeOrder= sod.save(storeOrder01);
        StoreOrderDetailId storeOrderDetailId01 = new StoreOrderDetailId(storeOrder.getId(), "A001");
        StoreOrderDetailId storeOrderDetailId02 = new StoreOrderDetailId(storeOrder.getId(), "A002");
        StoreOrderDetailId storeOrderDetailId03 = new StoreOrderDetailId(storeOrder.getId(), "A003");
        //=================================================================================
        StoreOrderDetail storeOrderDetail01 = StoreOrderDetail.builder()
                .orderId(storeOrderDetailId01.getOrderId())
                .itemCode(storeOrderDetailId01.getItemCode())
                .status("출고중")
                .orderedQuantity(1000)
                .build();
        sodd.save(storeOrderDetail01);
        StoreOrderDetail storeOrderDetail02 = StoreOrderDetail.builder()
                .orderId(storeOrderDetailId02.getOrderId())
                .itemCode(storeOrderDetailId02.getItemCode())
                .status("출고중")
                .orderedQuantity(1000)
                .build();
        sodd.save(storeOrderDetail02);
        StoreOrderDetail storeOrderDetail03 = StoreOrderDetail.builder()
                .orderId(storeOrderDetailId03.getOrderId())
                .itemCode(storeOrderDetailId03.getItemCode())
                .status("출고중")
                .orderedQuantity(1000)
                .build();
        sodd.save(storeOrderDetail03);
        //=================================================================================
        //MenuPrice menuPrice01 = new MenuPrice("M001",3500);
        //mpd.insertMenuPrice(menuPrice01);
        //=================================================================================


        System.out.println("더미데이터를 생성하였습니다.");
    }
}