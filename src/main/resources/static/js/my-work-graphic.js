document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".edit-btn").forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();
            let workGraphicId = this.getAttribute("data-id");
            let weekday = this.getAttribute("data-weekday");
            let startWorkDate = this.getAttribute("data-start");
            let endWorkDate = this.getAttribute("data-end");
            let active = this.getAttribute("data-active") === "true";
            document.getElementById("workGraphicId").value = workGraphicId;
            document.getElementById("weekdayLabel").innerText = weekday;
            document.getElementById("startWorkDate").value = startWorkDate;
            document.getElementById("endWorkDate").value = endWorkDate;
            document.getElementById("active").value = active.toString();
        });
    });
});