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

const userToggle = document.getElementById("userToggle");
const chevronIcon = document.getElementById("chevronIcon");
const userMenu = document.getElementById("userMenu");

userToggle.addEventListener("click", () => {
    userMenu.classList.toggle("hidden");
    chevronIcon.classList.toggle("bi-chevron-down");
    chevronIcon.classList.toggle("bi-chevron-up");
});
