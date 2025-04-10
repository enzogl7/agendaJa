const toggleBtn = document.getElementById("toggleSidebar");
const sidebar = document.getElementById("sidebar");
const icon = toggleBtn.querySelector("i");

toggleBtn.addEventListener("click", () => {
    sidebar.classList.toggle("-translate-x-full");

    if (sidebar.classList.contains("-translate-x-full")) {
        icon.className = "bi bi-arrow-bar-right text-base";
    } else {
        icon.className = "bi bi-arrow-bar-left text-base";
    }
});
