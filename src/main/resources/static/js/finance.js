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