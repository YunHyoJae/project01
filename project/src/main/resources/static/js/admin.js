document.addEventListener("DOMContentLoaded", function () {
  // 기본 페이지 로드 (초기 진입 시)
  const defaultPage = document.querySelector('#content-area').getAttribute('data-include');
  if (defaultPage) {
    loadPage(defaultPage);
  }

  // 서브메뉴 active 구현
  const submenus = document.querySelectorAll('.submenu');
  const submenuItems = document.querySelectorAll('.submenu-item');
  
  // if (submenus.length > 0) {  // 기본 선택
  //   submenus[0].classList.add('active');
  // }
  submenuItems.forEach(item => {
    item.addEventListener('click', function(event) {
      event.preventDefault();

      submenuItems.forEach(otherItem => {
          otherItem.classList.remove('active');
      });

      this.classList.add('active');
    });
  });

 // --- 페이지 로드 시 초기 설정 및 클릭 이벤트 리스너 설정 --- //
  submenus.forEach(sub => {
    if (submenus.length > 0) {  // 기본 선택
      submenus[0].classList.add('active');
    }

    submenus.forEach(item => {
      item.addEventListener('click', function(event) {
        event.preventDefault(); // 링크의 기본 동작 방지

        // 클릭된 그룹 내의 모든 항목에서 'active' 클래스 제거
        submenus.forEach(otherItem => {
            otherItem.classList.remove('active');
        });
        this.classList.add('active'); // 클릭된 항목에만 'active' 클래스 추가
        changeMenu();
      });
    });
  });
});

function changeMenu() {
    const submenus = document.querySelectorAll(".submenu");

    // 현재 활성화된(active) 서브메뉴 항목의 가장 가까운 '.submenu' 부모를 찾습니다.
    const currentActiveItem = document.querySelector(".submenu-item.active");

    if (!currentActiveItem) {
        // 만약 active 클래스를 가진 항목이 없다면 함수를 종료합니다.
        console.warn("활성화된 서브메뉴 항목이 없습니다.");
        return;
    }

    let currentSub = currentActiveItem.closest(".submenu");

    // 모든 서브메뉴를 숨깁니다.
    submenus.forEach(sub => {
        sub.style.display = "none";
    });

    // 현재 활성화된 서브메뉴만 보이게 합니다.
    if (currentSub) { // currentSub이 null이 아닌지 다시 한번 확인 (안전장치)
        currentSub.style.display = "block";
    } else {
        console.warn(".submenu-item.active의 부모 중 .submenu를 찾을 수 없습니다.");
    }

    console.log("모든 서브메뉴:", submenus);
    console.log("현재 활성화된 서브메뉴:", currentSub);

    // 주석 처리된 if-else if 블록은 더 이상 필요 없습니다.
    // 현재 활성화된 항목의 부모를 찾아 보여주는 방식으로 변경되었기 때문입니다.
}

/*  공통 - 페이지 로드  */
function loadPage(pageUrl, sub) {
  fetch(pageUrl)
    .then(response => {
      if (!response.ok) throw new Error('페이지를 불러올 수 없습니다.');
      return response.text();
    })
    .then(html => {
      document.getElementById('content-area').innerHTML = html;
    })
    .catch(error => {
      console.error(error);
      document.getElementById('content-area').innerHTML = '<p>페이지 로딩 실패</p>';
    });
}

/* 공통 - 체크박스 전체선택 기능 */
function selectAll(selectAll)  {
  const checkboxes = document.querySelectorAll('input[type="checkbox"]');
  
  checkboxes.forEach((checkbox) => {
    checkbox.checked = selectAll.checked;
  })
}

/* 공통 - 메뉴 변경 */
function changeMenu() {
  const submenus = document.querySelectorAll(".submenu");
  let currentSub = document.querySelector(".submenu-item.active").closest(".submenu");
  
  submenus.forEach(sub => {
    sub.style.display = "none";
  });

  currentSub.style.display = "block";

  console.log(submenus);
  console.log(currentSub);
  // if(menu == "order") {
  //   document.querySelector(".sub_order").style.display = "block";
  //   document.querySelector(".sub_return").style.display = "none";
  //   document.querySelector(".sub_sales").style.display = "none";
  //   document.querySelector(".sub_service").style.display = "none";
  // } else if (menu == "return") {
  //   document.querySelector(".sub_return").style.display = "block";
  //   document.querySelector(".sub_order").style.display = "none";
  //   document.querySelector(".sub_sales").style.display = "none";
  //   document.querySelector(".sub_service").style.display = "none";
  // } else if (menu == "sales") {
  //   document.querySelector(".sub_sales").style.display = "block";
  //   document.querySelector(".sub_return").style.display = "none";
  //   document.querySelector(".sub_order").style.display = "none";
  //   document.querySelector(".sub_service").style.display = "none";
  // } else if (menu == "service") {
  //   document.querySelector(".sub_service").style.display = "block";
  //   document.querySelector(".sub_return").style.display = "none";
  //   document.querySelector(".sub_sales").style.display = "none";
  //   document.querySelector(".sub_order").style.display = "none";
  // } else if (menu == "board") {
  //   document.querySelector(".sub_board").style.display = "block";
  // }
}

/* 주문신청서 - 행추가 */
function addRow() {
  const tbody = document.querySelector('#orderFormTable tbody');
  let rows = tbody.rows;

  const newRow = document.createElement('tr');
  newRow.innerHTML = `
    <td><input type="checkbox"/></td>
    <td><input type="text"></td>
    <td><input type="text" class="pd_code"></td>
    <td><input type="text" class="pd_name"></td>
    <td class="search_td"><button class="default_btn" onclick="showModal(this)">검색</button></td>
    <td><input type="number"></td>
    <td><input type="number"></td>
    <td><input type="text"></td>
    <td><button onclick="deleteRow(this)">❌</button></td>
  `;

  // 행이 1개 이상 있을 경우 마지막 전 위치에 추가
  if (rows.length >= 1) {
    tbody.insertBefore(newRow, rows[rows.length - 1]);
  } else {
    tbody.appendChild(newRow); // 행이 없으면 그냥 추가
  }
}
function deleteRow(row) {
  const tbody = document.querySelector('#orderFormTable tbody');
  let rows = tbody.rows;
  let thisRow = row.closest('tr');
  if (rows.length > 2) {
      tbody.removeChild(thisRow);
  }
}

/* 주문신청서 - 검색 버튼 기능 */
let currentRow = null; // 클릭된 행 저장할 변수
function showModal(button) { // 검색 버튼 클릭 시 모달 창 열림
  currentRow = button.closest('tr'); 
  document.getElementById('modalBg').style.display = 'block';
}
function getSelect() { // select된 값에 따라 보이는 input이 달라짐
  const selected = document.getElementById('searchProduct').value;
  const codeBox = document.querySelector('.code_box');
  const nameBox = document.querySelector('.name_box');

  if (selected === 'product_code') {
    codeBox.style.display = 'flex';
    nameBox.style.display = 'none';
  } else if (selected === 'product_name') {
    codeBox.style.display = 'none';
    nameBox.style.display = 'flex';
  }
}
function getInput(e) { // 입력된 값 품목코드, 품목명에 기입
  e.preventDefault();
  let selectedType = document.getElementById('searchProduct').value;

  if (selectedType === 'product_code') {
    let value = document.getElementById('inputCode').value;
    if (currentRow) {
      currentRow.querySelector('.pd_code').value = value;
    }
  } else if (selectedType === 'product_name') {
      let value = document.getElementById('inputName').value;
      if (currentRow) {
        currentRow.querySelector('.pd_name').value = value;
    }
  }
}


/* 반품신청 */
// 필터 버튼
const filterButtons = document.querySelectorAll('.return_Inquiry_filter_buttons label');
const rows = document.querySelectorAll('.return_row');
filterButtons.forEach(button => {
  button.addEventListener('click', () => {
    // 활성화 스타일 토글
    filterButtons.forEach(btn => btn.classList.remove('active'));
    button.classList.add('active');

    const status = button.getAttribute('data-status');

    rows.forEach(row => {
      if (status === 'all') {
        row.classList.remove('hidden');
      } else if (status === 'pending') {
        if (row.classList.contains('pending')) {
          row.classList.remove('hidden');
        } else {
          row.classList.add('hidden');
        }
      } else if (status === 'completed') {
        if (row.classList.contains('completed')) {
          row.classList.remove('hidden');
        } else {
          row.classList.add('hidden');
        }
      }
    });
  });
});