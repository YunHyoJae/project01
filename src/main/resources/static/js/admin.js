document.addEventListener("DOMContentLoaded", function () {

  // 서브메뉴 변경
  const contentName = document.querySelector(".content");

  switch (true) {
    case contentName.classList.contains("help"):
      document.querySelector(".inquiry").style.display = "block";
      break;
    case contentName.classList.contains("salesStatus"):
      document.querySelector(".stock").style.display = "block";
      break;
    case contentName.classList.contains("franchiseInquiry"):
      document.querySelector(".service").style.display = "block";
  }
  // 검색어 폼 초기화
  document.querySelectorAll('.searchTerm').forEach(select => changeForm(select));
});

// 체크박스 전체선택 기능
function selectAll(selectAll)  {
  const checkboxes = document.querySelectorAll('input[type="checkbox"]');
  
  checkboxes.forEach((checkbox) => {
    checkbox.checked = selectAll.checked;
  })
}

// 검색어 폼 변경
function changeForm(selectElement) {
  const form = selectElement.closest("form");
  const viewText = form.querySelector(".textBox");
  const viewSelect = form.querySelector(".statusBox");
  const viewDate = form.querySelector(".dateBox");
  let selectValue = selectElement.value;

  if(selectValue === "status") {
    // 상태검색 셀렉트 선택 시 select 폼
    viewText.style.display = "none";
    viewDate && (viewDate.style.display = "none");
    viewSelect.style.display = "block";
  } else if(selectValue === "date"){
    // 날짜 검색 셀렉트 선택 시 date 폼
    viewSelect.style.display = "none";
    viewText.style.display = "none";
    viewDate && (viewDate.style.display = "block");
  } else {
    // 나머지 선택 시 text 폼
    viewSelect.style.display = "none";
    viewDate && (viewDate.style.display = "none");
    viewText.style.display = "block";
  }
}