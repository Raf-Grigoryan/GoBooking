document.addEventListener("DOMContentLoaded", function () {
    console.log("Script loaded!");
    document.querySelectorAll(".card-number").forEach(el => {
        let num = el.textContent.replace(/\D/g, "");
        el.textContent = num.replace(/(\d{4})/g, "$1-").slice(0, -1);
    });

    document.querySelectorAll(".expiration-date").forEach(el => {
        let dateText = el.textContent.trim();
        console.log("Original Expiration Date:", dateText);

        let dateObj = new Date(dateText);

        if (!isNaN(dateObj)) { // Проверяем, корректная ли дата
            let month = String(dateObj.getMonth() + 1).padStart(2, "0");
            let year = String(dateObj.getFullYear()).slice(-2);
            el.textContent = `${month}/${year}`;
            console.log("Formatted Expiration Date:", el.textContent);
        } else {
            el.textContent = "MM/YY";
            console.log("Failed to parse date!");
        }
    });

    document.querySelectorAll(".cvv").forEach(el => {
        el.addEventListener("click", function () {
            if (el.dataset.visible === "true") {
                el.textContent = "***";
                el.dataset.visible = "false";
            } else {
                el.textContent = el.dataset.cvv;
                el.dataset.visible = "true";
            }
        });
        el.dataset.cvv = el.textContent;
        el.textContent = "***";
        el.dataset.visible = "false";
    });


    let totalBalance = 0;
    document.querySelectorAll(".balance").forEach(el => {
        let balance = parseFloat(el.textContent.replace(/[^0-9.]/g, ""));
        if (!isNaN(balance)) {
            totalBalance += balance;
        }
    });
    console.log("Total Balance:", totalBalance);

    let totalBalanceElement = document.getElementById("total-balance");
    if (totalBalanceElement) {
        totalBalanceElement.innerHTML = ` <span style="white-space: nowrap;">${totalBalance.toFixed(2)} $</span>`;
        totalBalanceElement.style.color = "black";
        totalBalanceElement.style.marginRight = "270px";
        totalBalanceElement.style.display = "inline-block";
    }
});
document.addEventListener("DOMContentLoaded", function () {
    let totalSpending = 0;
    document.querySelectorAll(".price").forEach(function (element) {
        let price = parseFloat(element.getAttribute("data-price")) || 0;
        totalSpending += price;
    });
    document.getElementById("total-spending").textContent = "-$ " + totalSpending.toFixed(2);
});
document.addEventListener("DOMContentLoaded", function () {
    const rowsPerPage = 7; // Количество строк на страницу
    const table = document.getElementById("bookingTable");
    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.querySelectorAll("tr"));
    const totalRows = rows.length;
    const totalPages = Math.ceil(totalRows / rowsPerPage);
    let currentPage = 1;

    const paginationContainer = document.getElementById("paginationControls");

    function displayRows(page) {
        // Скрываем все строки
        rows.forEach(row => row.style.display = "none");

        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        for (let i = start; i < end && i < totalRows; i++) {
            rows[i].style.display = "";
        }
    }

    function generatePagination() {
        let pageItems = Array.from(paginationContainer.querySelectorAll("li.page-item.number"));
        pageItems.forEach(item => item.remove());

        for (let i = 1; i <= totalPages; i++) {
            const li = document.createElement("li");
            li.classList.add("page-item", "number");
            if (i === currentPage) {
                li.classList.add("active");
            }
            const a = document.createElement("a");
            a.classList.add("page-link");
            a.href = "#";
            a.textContent = i;
            a.addEventListener("click", function (e) {
                e.preventDefault();
                currentPage = i;
                updatePagination();
            });
            li.appendChild(a);
            paginationContainer.insertBefore(li, document.getElementById("nextPage"));
        }
    }


    function updatePagination() {
        displayRows(currentPage);
        generatePagination();
    }

    document.getElementById("prevPage").addEventListener("click", function (e) {
        e.preventDefault();
        if (currentPage > 1) {
            currentPage--;
            updatePagination();
        }
    });

    document.getElementById("nextPage").addEventListener("click", function (e) {
        e.preventDefault();
        if (currentPage < totalPages) {
            currentPage++;
            updatePagination();
        }
    });

    updatePagination();
});