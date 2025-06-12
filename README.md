# project
기업실무프로젝트

퍼블경로 : src > main > resources > static 

## admin page structure
[ storeOwner.html - 점주 관리자 페이지 레이아웃 ]
css는 storeOwner.css - 점주 페이지끼리 공통 사용

order_list.html - 주문현황
order_form.html - 주문신청
return_inquiry.html - 반품조회
return_form.html - 반품신청
sales_status.html - 판매현황
help_list.html - 문의 게시판
help_detail.html - 문의 상세내용 (게시글 클릭)
help.html - 문의하기 (질문하기 버튼)


[ headOffice.html - 본사 관리자 페이지 레이아웃 ]
css는 headOffice.css - 본사 페이지끼리 공통 사용

receiving_status.html - 입고현황
receiving_form.html - 입고등록
receiving_order.html - 발주
inventory_status.html - 매장별 재고현황
inventory_product_form.html - 기초품목등록
inventory_menu_form.html - 메뉴등록
shipping_form.html - 출고관리
help_list.html - 고객의소리
franchise_list.html - 프렌차이즈 문의
store_state.html - 매장현황


*javascript는 admin.js 공통 사용 / 컨텐츠 내용은 각 페이지 html에서 수정

*각 관리자 페이지 레이아웃 파일에서 data-include 이용하여 기본 컨텐츠 내용 불러오고,
loadPage 함수 사용하여 바뀌는 컨텐츠 내용만 불러오도록 함