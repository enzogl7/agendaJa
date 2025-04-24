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

function paginacaoTabela(tabela) {
    $('#' + tabela).DataTable({
        "paging": true,
        "lengthMenu": [10, 15, 25],
        "searching": false,
        "ordering": false,
        "info": false,
        "language": {
            "lengthMenu": "_MENU_ registros por página",
            "zeroRecords": "Nenhum registro encontrado",
            "info": "Página _PAGE_ de _PAGES_",
            "infoEmpty": "Nenhum registro disponível",
            "infoFiltered": "(Filtrado de _MAX_ registros no total)",
            "paginate": {
                "previous": "<",
                "next": ">"
            }
        },
        "initComplete": function() {
            $('#' + tabela + '_paginate [aria-current="page"]').css({
                'background-color': '#27AE60',
                'color': '#fff',
                'border': 'none',
                'outline': 'none',
                'text-decoration': 'none'
            });
            $('#' + tabela + ' th').css('text-align', 'center');
        }
    });
}
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))

function formatarTelefone(input) {
    let telefone = input.value.replace(/\D/g, '');
    telefone = telefone.substring(0, 11);

    if (telefone.length >= 2 && telefone.length <= 6) {
        input.value = `(${telefone.substring(0, 2)}) ${telefone.substring(2)}`;
    } else if (telefone.length > 6 && telefone.length <= 10) {
        input.value = `(${telefone.substring(0, 2)}) ${telefone.substring(2, 3)} ${telefone.substring(3, 7)}-${telefone.substring(7)}`;
    } else if (telefone.length === 11) {
        input.value = `(${telefone.substring(0, 2)}) ${telefone.substring(2, 3)} ${telefone.substring(3, 7)}-${telefone.substring(7)}`;
    } else {
        input.value = telefone;
    }
}