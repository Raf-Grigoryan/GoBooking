document.addEventListener("DOMContentLoaded", function () {
    const tableRows = document.querySelectorAll("#bookingsTable tr");
    const rowsPerPage = 8;
    let currentPage = 1;

    function showPage(page) {
        let start = (page - 1) * rowsPerPage;
        let end = start + rowsPerPage;

        tableRows.forEach((row, index) => {
            row.style.display = index >= start && index < end ? "" : "none";
        });

        document.getElementById("currentPage").innerText = page;
    }

    function updateButtons() {
        document.getElementById("prevPage").parentElement.classList.toggle("disabled", currentPage === 1);
        document.getElementById("nextPage").parentElement.classList.toggle("disabled", currentPage * rowsPerPage >= tableRows.length);
    }

    document.getElementById("prevPage").addEventListener("click", function (event) {
        event.preventDefault();
        if (currentPage > 1) {
            currentPage--;
            showPage(currentPage);
            updateButtons();
        }
    });

    document.getElementById("nextPage").addEventListener("click", function (event) {
        event.preventDefault();
        if (currentPage * rowsPerPage < tableRows.length) {
            currentPage++;
            showPage(currentPage);
            updateButtons();
        }
    });

    showPage(currentPage);
    updateButtons();
});
document.addEventListener("DOMContentLoaded", function () {
    let totalPrice = 0;

    document.querySelectorAll("td[data-price]").forEach(td => {
        let price = parseFloat(td.getAttribute("data-price")) || 0;
        totalPrice += price;
    });

    let totalPriceElement = document.getElementById("totalPrice");
    totalPriceElement.setAttribute("data-total-price", totalPrice.toFixed(2));
    totalPriceElement.innerText = totalPrice.toFixed(2);
});