document.addEventListener("DOMContentLoaded", function () {

  // 서브메뉴 변경
  const boxName = document.querySelector(".box");

  switch (true) {
    case boxName.classList.contains("help-box"):
      document.querySelector(".service").style.display = "block";
      break;
    case boxName.classList.contains("salesStatus-box"):
      document.querySelector(".stock").style.display = "block";
      break;
  }
});

// 체크박스 전체선택 기능
function selectAll(selectAll)  {
  const checkboxes = document.querySelectorAll('input[type="checkbox"]');
  
  checkboxes.forEach((checkbox) => {
    checkbox.checked = selectAll.checked;
  })
}

// 매장현황 검색어 폼 변경
function changeForm() {
  const select = document.querySelector("#searchTerm");
  const viewText = document.querySelector(".textForm");
  const viewSelect = document.querySelector(".selectStatus");
  let selectValue = (select.options[select.selectedIndex].value);

  console.log(selectValue);
  if(selectValue == "status") {
    viewText.style.display = "none";
    viewSelect.style.display = "block";
  } else {
    viewSelect.style.display = "none";
    viewText.style.display = "block";
  }
}
